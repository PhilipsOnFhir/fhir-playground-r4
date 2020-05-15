package org.github.philipsonfhir.smartsuite.fhircast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebSocketSubscribtionRequest;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

@ClientEndpoint
public class FhirCastWebsocketClient {

    private final String hubUrl;
    private final String topicId;
    private FhirCastWebSocketSubscribtionRequest currentSubscriptionRequest;
    private RestTemplate restTemplate = new RestTemplate();
    private String webSocketUrl = null;
    private Logger logger = Logger.getLogger( this.getClass().getName()  );
    private boolean eventReceived = false;
    private String events;
    private ContextEvent currentEvent;
    private boolean connected = false;

    public FhirCastWebsocketClient(String hubUrl, String topicId ) {
        this.hubUrl = hubUrl;
        this.topicId = topicId;
    }

    /** subscribe to all events */
    public void subscribeWebsubAll() throws FhirCastException {
        FhirCastWebSocketSubscribtionRequest subscribtionRequest = new FhirCastWebSocketSubscribtionRequest();
        subscribtionRequest.setHub_mode("subscribe");
        subscribtionRequest.setHub_topic(this.topicId);
        subscribtionRequest.setHub_events("*-*,*");
        this.events = subscribtionRequest.getHub_events();
//        subscribtionRequest.setHub_secret( "secret"+random.nextLong() );
//        this.communicationListener.setSecret( subscribtionRequest.getHub_secret() );

        updateSubscription( subscribtionRequest);
    }

    private void updateSubscription(FhirCastWebSocketSubscribtionRequest subscribtionRequest) throws FhirCastException {
        this.currentSubscriptionRequest = subscribtionRequest;

        // register form message converter
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new FormHttpMessageConverter() );

        // create form parameters as a MultiValueMap
        final MultiValueMap<String, String> formVars = subscribtionRequest.getFormVars();

        // send the request -- our service returns a String in the body
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity( hubUrl, formVars, String.class );

//        ResponseEntity<String> responseEntity = restTemplate.postForEntity( hubUrl, fhirCastWebHookSubscribtionRequest, String.class );
        if ( responseEntity.getStatusCode()!= HttpStatus.ACCEPTED ) {
            throw new FhirCastException("Subscription update failed");
        }
        this.webSocketUrl = responseEntity.getBody();
        if ( webSocketUrl==null ){
            throw new FhirCastException("no url returned");
        }
        openWebsocket( webSocketUrl );
    }

    private void openWebsocket(String webSocketUrl) throws FhirCastException {
        logger.info("Connect to "+webSocketUrl);
        try{
            container= ContainerProvider.
                    getWebSocketContainer();
            Session session = container.connectToServer(this, new URI(webSocketUrl));
        }catch(Exception ex){
            ex.printStackTrace();
            throw new FhirCastException( ex );
        }
    }

    private Session session;
    private WebSocketContainer container;


    @OnOpen
    public void onOpen(Session session){
        logger.info("onOpen "+session );
        this.session=session;
//        String msg = String.format( "{\n" +
//                "  \"hub.mode\": \"subscribe\",\n" +
//                "  \"hub.topic\": \"%s\",\n" +
//                "  \"hub.events\": \"%s\",\n" +
//                "  \"hub.lease-seconds\": %d\n" +
//                "}", this.topicId, this.events, 100);
//        sendMessage(msg);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        logger.info( "new Message received: "+ message );
        synchronized ( this ){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if ( this.connected ) {
                    this.currentEvent = objectMapper.readValue(message, ContextEvent.class);
                    ;
                    this.eventReceived = true;
                    this.sendMessage("{ \"id\": \"" + this.currentEvent.getId()+"\","+
                            "\"status\":\"200\""+
                            "}");
                    this.notifyAll();
                }else {
                    this.connected = true;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(String message){
        logger.info("sending event "+ message);
        try {
            session.getBasicRemote().sendText(message);
        } catch ( IOException ex) {

        }
    }


    public void waitForEvent() throws FhirCastException {
        synchronized ( this ) {
            if (!this.eventReceived) {
                try {
                    this.wait(10000);
                } catch (InterruptedException e) {
                    throw new FhirCastException("No event received time out");
                }
            }
            if (!this.eventReceived) {
                throw new FhirCastException("No event received time out");
            }
        }
    }

    public void resetEventWait() {
        this.eventReceived=false;
        this.currentEvent = null;
    }

    public ContextEvent getLastEvent() {
        return this.currentEvent;
    }
}
