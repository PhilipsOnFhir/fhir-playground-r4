package com.github.philipsonfhir.fhircast;

import com.github.philipsonfhir.fhircast.server.FhirCastServerApplication;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhircastEventType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FhirCastServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FhirCastWebsocketEndpoint {
    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String SEND_FHICAST_EVENT_ENDPOINT = "/app/fhircast/";
    private static final String SUBSCRIBE_FHICAST_EVENT_ENDPOINT = "/hub/fhircast/";

    @Before
    public void setup() {
        URL = "ws://localhost:" + port + "/fhicast/websocket";
    }

    @Test
    public void testFcSendEvent() throws InterruptedException, ExecutionException, TimeoutException {
        String uuid = UUID.randomUUID().toString();

        //////////////////////////////////////////////////////////////////////
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        FhircastSendFrameHandler fhircastSendFrameHandler = new FhircastSendFrameHandler();
        stompSession.subscribe(SUBSCRIBE_FHICAST_EVENT_ENDPOINT + uuid+"/event", fhircastSendFrameHandler);

//        //////////////////////////////////////////////////////////////////////
        WebSocketStompClient stompClient2 = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient2.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession2 = stompClient2.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        FhircastSendFrameHandler fhircastSendFrameHandler2 = new FhircastSendFrameHandler();
        stompSession2.subscribe(SUBSCRIBE_FHICAST_EVENT_ENDPOINT + uuid+"/event", fhircastSendFrameHandler2);

        //////////////////////////////////////////////////////////////////////
        FhirCastWorkflowEventEvent fhirCastEvent = new FhirCastWorkflowEventEvent();
        fhirCastEvent.setHub_topic( "topic" );
        fhirCastEvent.setHub_event( FhircastEventType.OPEN_PATIENT_CHART );

        stompSession.send(SEND_FHICAST_EVENT_ENDPOINT + uuid+"/event", fhirCastEvent);
        FhirCastWorkflowEventEvent receivedFhirCastEvent  = fhircastSendFrameHandler.getResult();
        FhirCastWorkflowEventEvent receivedFhirCastEvent2 = fhircastSendFrameHandler2.getResult();

        assertNotNull(receivedFhirCastEvent);
        assertNotNull(receivedFhirCastEvent2);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class FhircastSendFrameHandler implements StompFrameHandler {
        private CompletableFuture<FhirCastWorkflowEventEvent> fhircastCompletableFuture = new CompletableFuture<>();

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println("FhircastSendFrameHandler.getPayloadType "+stompHeaders.toString());
            return FhirCastWorkflowEventEvent.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println("FhircastSendFrameHandler.handleFrame "+ o );
            this.fhircastCompletableFuture.complete((FhirCastWorkflowEventEvent) o);
        }

        FhirCastWorkflowEventEvent getResult() throws InterruptedException, ExecutionException, TimeoutException {
            return fhircastCompletableFuture.get(5, SECONDS);
        }
    }
}
