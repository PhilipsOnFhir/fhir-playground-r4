package com.github.philipsonfhir.fhircast.server.websocket;

import com.github.philipsonfhir.fhircast.server.service.FhirCastTopicEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastContext;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.hl7.fhir.dstu3.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class FhircastWebsocketService implements ApplicationListener<FhirCastTopicEvent>  {

    @Autowired
    Environment environment;

    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private String websocketUrl;
    private static final String SEND_FHIRCAST_EVENT_ENDPOINT = "/app/fhircast/";
    private static final String SUBSCRIBE_FHIRCAST_EVENT_ENDPOINT = "/hub/fhircast/";

    public FhircastWebsocketService() throws InterruptedException, ExecutionException, TimeoutException {
//        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

//        String URL = "ws://localhost:" + 9080 + "/fhircast/websocket";
//        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
//        }).get(1, SECONDS);


    }

    public void sendEvent(FhirCastWorkflowEventEvent fhirCastEvent) {
        String port = environment.getProperty("local.server.port");
        websocketUrl = "ws://localhost:" + port + "/fhircast/websocket";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = null;
        try {
            stompSession = stompClient.connect(
                websocketUrl,
                new StompSessionHandlerAdapter() {}
            ).get(1, SECONDS);
//            FhircastSendFrameHandler fhircastSendFrameHandler = new FhircastSendFrameHandler();

            String destination = SEND_FHIRCAST_EVENT_ENDPOINT + fhirCastEvent.getHub_topic()+"/"+fhirCastEvent.getHub_event().getName();
            stompSession.send( destination, fhirCastEvent);
            stompSession.disconnect();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }


//    @Override
//    public Type getPayloadType(StompHeaders stompHeaders) {
//        System.out.println("FhircastSendFrameHandler.getPayloadType "+stompHeaders.toString());
//        return FhirCastWorkflowEventEvent.class;
//    }
//
//    @Override
//    public void handleFrame(StompHeaders stompHeaders, Object o) {
//        System.out.println("FhircastSendFrameHandler.handleFrame "+ o );
//    }

    @Override
    public void onApplicationEvent(FhirCastTopicEvent event) {
        logger.info( "onApplicationEvent "+event.getTopic()+" "+event.getEventType() );
        //        stompSession.subscribe( SUBSCRIBE_FHIRCAST_EVENT_ENDPOINT + uuid+"/event", this);
        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_topic( event.getTopic() );
        fhirCastWorkflowEventEvent.setHub_event( event.getEventType() );
        event.getContext().entrySet().stream()
            .forEach( entry -> {
                FhirCastContext fhirCastContext = new FhirCastContext();
                fhirCastContext.setKey( entry.getKey() );
                fhirCastContext.setResource( (Resource) entry.getValue() );
                fhirCastWorkflowEventEvent.getContext().add( fhirCastContext );
            } );
        this.sendEvent( fhirCastWorkflowEventEvent );
    }


//    private class FhircastSendFrameHandler implements StompFrameHandler {
//        private CompletableFuture<FhirCastWorkflowEventEvent> fhircastCompletableFuture = new CompletableFuture<>();
//
//        @Override
//        public Type getPayloadType(StompHeaders stompHeaders) {
//            System.out.println("FhircastSendFrameHandler.getPayloadType "+stompHeaders.toString());
//            return FhirCastWorkflowEventEvent.class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders stompHeaders, Object o) {
//            System.out.println("FhircastSendFrameHandler.handleFrame "+ o );
//            this.fhircastCompletableFuture.complete((FhirCastWorkflowEventEvent) o);
//        }
//
//        FhirCastWorkflowEventEvent getResult() throws InterruptedException, ExecutionException, TimeoutException {
//            return fhircastCompletableFuture.get(5, SECONDS);
//        }
//    }
}
