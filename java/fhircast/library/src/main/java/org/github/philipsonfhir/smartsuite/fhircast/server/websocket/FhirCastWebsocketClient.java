package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebSocketSubscribtionResponse;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.WorkflowEventFactory;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.util.SubscriptionManager;
import org.github.philipsonfhir.smartsuite.fhircast.server.websub.MostRecentEventHandler;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class FhirCastWebsocketClient {
    private final SocketHandler websocketHandler;
    private final SubscriptionManager subscriptionManager = new SubscriptionManager();
    private final MostRecentEventHandler mostRecentEventHandler;
    private final String topicId;
    private final FhirCastService fhirCastService;
    private String websocketId = "WS"+System.currentTimeMillis();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Timer timer = new Timer("WebsocketTimer");;
    private long timeOutTime;
    private boolean initialised = false;

    public FhirCastWebsocketClient(FhirCastService fhirCastService, SocketHandler websocketHandler, MostRecentEventHandler mostRecentEventHandler, String topicId) {
        this.mostRecentEventHandler = mostRecentEventHandler;
        this.websocketHandler = websocketHandler;
        this.topicId = topicId;
        this.fhirCastService = fhirCastService;
        websocketHandler.register( websocketId, this );
    }

    public String getEndpointId() {
        return websocketId;
    }

    public void challengeConfirm() {
        logger.info("Confirm message" );
        this.initialised = true;
        mostRecentEventHandler.getSortedEvents().forEach( contextEvent -> {
            try {
                sendEvent(contextEvent);
            } catch (FhirCastException e) {
                logger.warning("failed sending event "+contextEvent );
            }
        });

    }

    public void sendEvent(ContextEvent fhirCastEvent) throws FhirCastException {
        if ( this.initialised && subscriptionManager.hasSubscription( fhirCastEvent.getEvent().getHub_event() )){
            logger.info("send message = " + fhirCastEvent );
            websocketHandler.sendEvent( this.websocketId, fhirCastEvent );
        }
    }

    public void updateSubscription(SubscriptionUpdate subscriptionUpdate) throws FhirCastException {
        String events = subscriptionUpdate.getHub_events();

        timeOutTime = ( subscriptionUpdate.getHub_lease_seconds()==null || subscriptionUpdate.getHub_lease_seconds().isBlank()
                ? 3600*1000
                : Math.round( Double.parseDouble(subscriptionUpdate.getHub_lease_seconds())*1000)
        ) ;
        TimerTask task = new TimerTask() {
            public void run() {
                logger.info("Time-out on scubscription for " + websocketId);
                timer.cancel();

                websocketHandler.sendDenial( websocketId, subscriptionUpdate.getHub_topic(), events );
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
    }

    public FhirCastWebSocketSubscribtionResponse getSubscriptionResponse() {
        FhirCastWebSocketSubscribtionResponse response = new FhirCastWebSocketSubscribtionResponse();
        response.setHub_mode("subscribe");
        response.setHub_topic(topicId);
        response.setHub_leaseSeconds( ""+timeOutTime );
        response.setHub_events( subscriptionManager.getEvents() );
        // TODO{
        //  "hub.mode": "subscribe",
        //  "hub.topic": "fdb2f928-5546-4f52-87a0-0648e9ded065",
        //  "hub.events": "patient-open,patient-close",
        //  "hub.lease-seconds": 7200
        //} add subscription response
        return response;
    }

    public void sendSyncError(String id) {
        fhirCastService.sendEvent( WorkflowEventFactory.createSyncErrorEvent( topicId, id, "websocket client indicated it was not able to handle event") );
    }
}
