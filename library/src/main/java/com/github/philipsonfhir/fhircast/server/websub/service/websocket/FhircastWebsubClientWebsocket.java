package com.github.philipsonfhir.fhircast.server.websub.service.websocket;

import com.github.philipsonfhir.fhircast.server.websub.model.FhirCastSessionSubscribe;
import com.github.philipsonfhir.fhircast.server.websub.model.FhircastEventType;
import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubClient;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.server.websub.model.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.support.NotImplementedException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FhircastWebsubClientWebsocket extends FhirCastWebsubClient {

    private final SocketHandler socketHandler;
    private final String topicId;
    private String websocketId = "WS"+System.currentTimeMillis();
    private Logger logger = Logger.getLogger( this.getClass().getName() );

    private FhirCastSessionSubscribe lastSubscribe = null ;
    private List<FhirCastWebsocketSession> websocketSessions = new ArrayList<>();

    public void addWebsocketSession(WebSocketSession session) throws IOException {
        FhirCastWebsocketSession fhirCastWebsocketSession = new FhirCastWebsocketSession(session, socketHandler);
        this.websocketSessions.add( fhirCastWebsocketSession );
        if ( lastSubscribe!=null ){
            String subscriptionString = "";
            for ( FhircastEventType subscription: super.getSubscriptions() ){
                subscriptionString+=subscription.getName()+", ";
            }
            if ( subscriptionString.length()>2){
                subscriptionString.substring(0, subscriptionString.length()-2);
            }
            fhirCastWebsocketSession.sendChallenge( lastSubscribe, subscriptionString );
        }
    }

    public void challengeConfirm( TextMessage message) {
        this.websocketSessions.stream()
                .forEach( fhirCastWebsocketSession -> {
                    fhirCastWebsocketSession.challengeConfirm( message );
                });
    }


    public FhircastWebsubClientWebsocket(SocketHandler socketHandler, String topicId ) {
        super( "secret" );
        this.socketHandler = socketHandler;
        this.topicId = topicId;
        socketHandler.register( websocketId, this );
        // verification
        // 1 create websocket
        // 2 return url

        // 3 wait for connection
        // 4 return challenge
        // 5 wait for response
        // 6 verify response
        // 7 set verified
    }

    @Override
    public String getSubscriptionReturn() {
        return websocketId;
    }

    @Override
    public void subscribe( String subscriptions) throws NotImplementedException {
        throw new NotImplementedException();
    }

    public void subscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe ) throws NotImplementedException {
        super.subscribe( fhirCastSessionSubscribe.getHub_events() );
        this.lastSubscribe = fhirCastSessionSubscribe;
    }

    @Override
    public void unsubscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        super.unsubscribe( fhirCastSessionSubscribe.getHub_events() );
    }


    @Override
    public void sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent ) throws FhirCastException {
// send model events
        logger.info("eventReceived " + fhirCastWorkflowEvent);
        logger.info("Sending event to " + websocketId );
        this.websocketSessions.stream()
                .forEach( websocketSession -> {
                    try {
                        websocketSession.sendEvent( fhirCastWorkflowEvent );
                    } catch (IOException e) {
                        logger.info("Error sending event "+websocketSession );
                    }
                });

//        if (isVerified() && hasSubscription(fhirCastWorkflowEvent.getEvent().getHub_event())) {
//            logger.info("Sending event to " + websocketId );

//            HttpHeaders httpHeaders = new HttpHeaders();
//            ObjectMapper objectMapper = new ObjectMapper();
//            String content = null;
//            try {
//                content = objectMapper.writeValueAsString(fhirCastWorkflowEvent);
//            } catch (JsonProcessingException e) {
//                throw new FhirCastException("parsing Event failed");
//            }
//
//            httpHeaders.add("X-Hub-Signature", calculateHMAC(content));
//
//            HttpEntity<String> entity = new HttpEntity<>(content, httpHeaders);
//            ResponseEntity<String> response = restTemplate.exchange(
//                    getClientCallbackUrl(),
//                    HttpMethod.POST, entity, String.class
//            );
//            logger.info("Sending event to " + getClientCallbackUrl() + " : " + response.getStatusCode());

//        }
    }

    public String getWebsocketId() {
        return websocketId;
    }

//    public FhirCastWorkflowEvent getVerificationIntent() {
//        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
//        fhirCastWorkflowEvent.initialize();
//
//
//        String result = String.format(
//                "{\n" +
//                "      \"timestamp\":\"%s\",\n" +
//                "      \"id\":\"%s\",\n" +
//                "      \"subscription\":{\n" +
//                "         \"hub.mode\":\"subscribe\",\n" +
//                "         \"hub.topic\":\"%s\",\n" +
//                "         \"hub.events\":\"%s\",\n" +
//                "         \"hub.secret\":\"%s\"\n" +
//                "      }\n" +
//                "}", fhirCastWorkflowEvent.getTimestamp(), fhirCastWorkflowEvent.getId(), this.
//        )


}
