package org.github.philipsonfhir.smartsuite.fhircast.server.websub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastDeliveryException;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.util.Hmac;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.util.SubscriptionManager;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class FhirCastWebhookClient {
    private final String clientCallbackUrl;
    private final SubscriptionManager subscriptionManager;
    private final MostRecentEventHandler mostRecentEventHandler;
    //    private Set<String> subscriptions = new TreeSet<>();
    private String secret;
    private boolean verified = false;
    private RestTemplate restTemplate = new RestTemplate();
    private Logger logger;
    private Timer timer = new Timer("WebsubTimer");;

    public FhirCastWebhookClient(String callback, MostRecentEventHandler mostRecentEventHandler) {
        this.mostRecentEventHandler = mostRecentEventHandler;
        this.clientCallbackUrl = callback;
        logger = Logger.getLogger( this.getClass().getName()+"-"+callback );
        this.subscriptionManager = new SubscriptionManager();
    }

    void updateSubscriptions(SubscriptionUpdate subscriptionUpdate) throws FhirCastException {
        String events = subscriptionUpdate.getHub_events();
        this.secret = subscriptionUpdate.getHub_secret();

        long timeOutTime = Math.round( Double.parseDouble(subscriptionUpdate.getHub_lease_seconds())*1000 ) ;
        TimerTask task = new TimerTask() {
            public void run() {
                logger.info("Time-out on scubscription for " + clientCallbackUrl);
                timer.cancel();

                String denialUrl = clientCallbackUrl
                        +String.format("?hub.mode=denied&hub.topic=%s&hub.events=%s&hub.reason=timeout",
                            subscriptionUpdate.getHub_topic(),subscriptionManager.getEvents());
                subscriptionManager.removeAllSubscriptions();
                restTemplate.getForEntity( denialUrl,String.class  );
            }
        };
        timer.cancel();
        timer = new Timer("Timer");
        timer.schedule(task, timeOutTime);

        switch ( subscriptionUpdate.getHub_mode()){
            case "subscribe":
                subscriptionManager.addSubscriptions( events );
                break;
            case "unsubscribe":
                subscriptionManager.removeSubscriptions( events );
                break;
            default:
                throw new FhirCastException("Illegal mode "+subscriptionUpdate.getHub_mode() );
        }
//        body.setHub_events( subscriptionManager.getEvents() );
        WebhookVerificationProcess.verify( this, subscriptionUpdate );
    }

    void setVerified(boolean success) {
        this.verified = success;
        // TODO only send most recent events
        mostRecentEventHandler.getSortedEvents().forEach( contextEvent -> {
            try {
                sendEvent(contextEvent);
            } catch (FhirCastException e) {
                logger.warning("failed sending event "+contextEvent );
            }
        });
    }

    private boolean isVerified() {
        return this.verified;
    }

    public void sendEvent(ContextEvent event) throws FhirCastException {
        if ( isVerified() && subscriptionManager.hasSubscription( event.getEvent().getHub_event()) ){
            logger.info("Sending event "+event);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String eventStr = objectMapper.writeValueAsString(event );
                // calculate HMAC and add to header X-Hub-Signature:
                String hmac= Hmac.calculateHMAC( this.secret, eventStr );
                // create headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Hub-Signature", hmac );

                //Create a new HttpEntity
                final HttpEntity<String> entity = new HttpEntity<>(eventStr, headers);

                restTemplate.exchange(this.clientCallbackUrl, HttpMethod.POST, entity, String.class );
            } catch (JsonProcessingException | FhirCastException e) {
                throw new FhirCastException( e );
            } catch(HttpServerErrorException.InternalServerError e){
                throw new FhirCastDeliveryException( e.getMessage() );
            }


        } else {
            if ( !isVerified() ){ logger.warning("Not verified "+event);}
        }
    }



}

