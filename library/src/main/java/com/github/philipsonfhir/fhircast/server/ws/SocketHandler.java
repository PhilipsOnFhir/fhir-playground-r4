package com.github.philipsonfhir.fhircast.server.ws;

import com.github.philipsonfhir.fhircast.server.topic.FhirCastTopicEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler implements ApplicationListener<FhirCastTopicEvent> {

    static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        for(WebSocketSession webSocketSession : sessions) {
//            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            webSocketSession.sendMessage(new TextMessage(message.getPayload() ));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        System.out.println(" new  session");
        sessions.add(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed");
    }

    @Override
    public void onApplicationEvent(FhirCastTopicEvent event) {
        System.out.println("ws message "+event.getEventType());
        sessions.forEach( session ->{
            MyMessage myMessage = new MyMessage( event.getTopic()+" = "+event.getEventType() );
            TextMessage textMessage = new TextMessage( event.getTopic()+" = "+event.getEventType() );
            try {
                session.sendMessage( textMessage );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        } );
    }

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