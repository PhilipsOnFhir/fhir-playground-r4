package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;//package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebSocketSubscribtionResponse;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*******************
 * Socket created on subscription
 * Socket removed on unsubscribe or denial
 */

@Component
public class SocketHandler extends TextWebSocketHandler {

    static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Map<String, FhirCastWebsocketClient> clientMap = new HashMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // new connnection
        logger.info("new session");
        sessions.add(session);
        String url = session.getUri().toString();
        String sessionId = url.substring( url.indexOf("/WS")+1);
        FhirCastWebsocketClient fhirCastWebsocketClient = this.clientMap.get(sessionId);
//        fhirCastWebsocketClient.addWebsocketSession( session );

        FhirCastWebSocketSubscribtionResponse response =
                fhirCastWebsocketClient.getSubscriptionResponse();

        session.sendMessage(new TextMessage( objectMapper.writeValueAsString( response ) ));
        fhirCastWebsocketClient.challengeConfirm();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {
        logger.info("new message "+ message);
        // check for the response - if wrong - send error message
        // if http status != 2XX - send synceerror with id
        try {
            TypeFactory factory = TypeFactory.defaultInstance();
            MapType type = factory.constructMapType(HashMap.class, String.class, String.class);
            Map<String, String> result = objectMapper.readValue(message.getPayload(), type);
            String statusCode = result.get("status");
            String id = result.get("id");
            if ( statusCode==null || !statusCode.equals("200")){
                String url = session.getUri().toString();
                String sessionId = url.substring( url.indexOf("/WS")+1);
                FhirCastWebsocketClient fhirCastWebsocketClient = this.clientMap.get(sessionId);
                fhirCastWebsocketClient.sendSyncError(id);
            }
        } catch( com.fasterxml.jackson.core.io.JsonEOFException e) {
            logger.warning(e.toString());
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info("handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("afterConnectionClosed");
    }

    public void register(String websocketId, FhirCastWebsocketClient fhircastWebsubClientWebsocket) {
        this.clientMap.put( websocketId, fhircastWebsubClientWebsocket );
    }

    public SendEventResult sendEvent(String websocketId, ContextEvent contextEvent ) throws FhirCastException {
        SendEventResult sendEventResult = new SendEventResult();
        ObjectMapper objectMapper = new ObjectMapper(  );
        String message = null;
        try {
            message = objectMapper.writeValueAsString(contextEvent);
        } catch (JsonProcessingException e) {
            throw new FhirCastException(e);
        }

        return sendMessage( websocketId, message );
    }

    public void sendDenial(String websocketId, String hub_topic, String events) {
        String message = String.format("{\n" +
                "   \"hub.mode\": \"denied\",\n" +
                "   \"hub.topic\":\" \"%s\",\n" +
                "   \"hub.events\": \"%s\",\n" +
                "   \"hub.reason\": \"session expired\"\n" +
                "}", websocketId, hub_topic, events );
        sendMessage( websocketId, message );

        // close socket on denial - new subscription - new WS
        List<WebSocketSession> toBeClosed = sessions.stream()
                .filter( webSocketSession -> getWebsocketIdFromUri(webSocketSession.getUri()).equals(websocketId))
                .collect(Collectors.toList());
        for ( WebSocketSession webSocketSession: toBeClosed ) {
            sessions.remove(webSocketSession);
        }
    }

    private SendEventResult sendMessage(String websocketId, String message) {
        SendEventResult sendEventResult = new SendEventResult();
        TextMessage textMessage = new TextMessage( message );
        sessions.stream()
                .filter( webSocketSession -> getWebsocketIdFromUri(webSocketSession.getUri()).equals(websocketId))
                .forEach( webSocketSession -> {
                    try {
                        logger.info(String.format("send ws to %s message %s", websocketId, message ));
                        webSocketSession.sendMessage( textMessage );
                    } catch (IOException e) {
                        sendEventResult.addError( websocketId+"send failed");
                    }
                });
        return sendEventResult;
    }


    private String getWebsocketIdFromUri( URI uri ){
        String uriStr = uri.toString();
        return uriStr.substring( uriStr.lastIndexOf("/WS")+1);
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
