package org.github.philipsonfhir.fhircast.server.websub.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.logging.Logger;

public class FhirCastWebsocketSession {
    private final WebSocketSession session;
    private final SocketHandler socketHandler;
    private StateEnum state = StateEnum.IDLE;
    private Logger logger = Logger.getLogger( this.getClass().getName() );



    private enum StateEnum { IDLE, CONNECTED, VERIFYING, VERIFIED, UNVERIFIED };


    public FhirCastWebsocketSession(WebSocketSession session, SocketHandler socketHandler) {
        this.session = session;
        state = StateEnum.CONNECTED;
        this.socketHandler = socketHandler;
    }

    public void sendChallenge(FhirCastSessionSubscribe lastSubscribe, String subscriptions ) throws IOException {
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        fhirCastWorkflowEvent.initialize();

        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_mode("subscribe");
        fhirCastSessionSubscribe.setHub_topic( lastSubscribe.getHub_topic() );
        String subscriptionString = "";
        fhirCastSessionSubscribe.setHub_events( subscriptions );
        fhirCastSessionSubscribe.setHub_secret( lastSubscribe.getHub_secret() );

        fhirCastWorkflowEvent.setSubscription(fhirCastSessionSubscribe);

        ObjectMapper objectMapper = new ObjectMapper(  );
        String message = objectMapper.writeValueAsString( fhirCastWorkflowEvent );

        TextMessage textMessage = new TextMessage( message );

        logger.info( "send challenge "+message );
        session.sendMessage( textMessage );
        state = StateEnum.VERIFYING;
    }

    public void challengeConfirm(TextMessage message) {
        if ( state.equals( StateEnum.VERIFYING )){
            logger.info( " response " + message.getPayload() );
            this.state = StateEnum.VERIFIED;
        }
    }

    public void sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) throws IOException {
        if ( state == StateEnum.VERIFIED ) {
            logger.info( "event send" );
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(fhirCastWorkflowEvent);
            TextMessage textMessage = new TextMessage(message);
            session.sendMessage(textMessage);
        } else {
            logger.info( "event ignored" );
        }
    }

}
