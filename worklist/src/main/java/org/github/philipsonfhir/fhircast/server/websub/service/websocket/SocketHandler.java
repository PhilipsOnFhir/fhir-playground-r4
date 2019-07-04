package org.github.philipsonfhir.fhircast.server.websub.service.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Map<String, FhircastWebsubClientWebsocket> clientMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // new connnection
        System.out.println("new session");
        sessions.add(session);
        String url = session.getUri().toString();
        String sessionId = url.substring( url.indexOf("/WS")+1);
        FhircastWebsubClientWebsocket fhircastWebsubClientWebsocket = this.clientMap.get(sessionId);
        fhircastWebsubClientWebsocket.addWebsocketSession( session );

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        String url = session.getUri().toString();
        String sessionId = url.substring( url.indexOf("/WS")+1);
        FhircastWebsubClientWebsocket fhircastWebsubClientWebsocket = this.clientMap.get(sessionId);
        fhircastWebsubClientWebsocket.challengeConfirm( message );

//        for(WebSocketSession webSocketSession : sessions) {
////            Map value = new Gson().fromJson(message.getPayload(), Map.class);
//            webSocketSession.sendMessage(new TextMessage(message.getPayload() ));
//        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed");
        sessions.remove( session );
    }

    public void register(String websocketId, FhircastWebsubClientWebsocket fhircastWebsubClientWebsocket) {
        this.clientMap.put( websocketId, fhircastWebsubClientWebsocket );
    }

//    @Override
//    public void onApplicationEvent(FhirCastTopicEvent event) {
//        System.out.println("ws message "+event.getEventType());
//        sessions.forEach( session ->{
//            try {
//                String prefix = "/fhircast/";
//                String postfix = Prefix.WEBSOCKET;
//                String url = session.getUri().toString();
//                String topicId = url.substring( prefix.length() );
//                if ( url.endsWith( postfix )){
//                    topicId = url.substring( prefix.length(), url.length()-( postfix.length()+1) );
//                    if ( event.getTopic().equals( topicId )) {
//
//                        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
//                        fhirCastWorkflowEvent.setId( "WS"+System.currentTimeMillis() );
//                        fhirCastWorkflowEvent.setTimestamp( new Date().toString() );
//
//                        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
//                        fhirCastWorkflowEventEvent.setHub_topic( event.getTopic() );
//                        fhirCastWorkflowEventEvent.setHub_event( event.getEventType() );
//                        event.getContext().entrySet().stream()
//                            .forEach( entry -> {
//                                FhirCastContext fhirCastContext = new FhirCastContext();
//                                fhirCastContext.setKey( entry.getKey() );
//                                fhirCastContext.setResource( (Resource) entry.getValue() );
//                                fhirCastWorkflowEventEvent.getContext().add( fhirCastContext );
//                            } );
//
//                        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );
//
//                        ObjectMapper objectMapper = new ObjectMapper(  );
//                        String message = objectMapper.writeValueAsString( fhirCastWorkflowEvent );
//
//                        TextMessage textMessage = new TextMessage( message );
//
//                        session.sendMessage( textMessage );
//                    }
//                }
//            } catch ( IOException e ) {
//                e.printStackTrace();
//            }
//        } );
//    }

    class MyMessage implements WebSocketMessage<String> {
        private final String str;

        MyMessage(String str ) {
            this.str = str;
        }

        @Override
        public String getPayload() {
            return str;
        }

        @Override
        public int getPayloadLength() {
            return str.length();
        }

        @Override
        public boolean isLast() {
            return false;
        }
    }
}
