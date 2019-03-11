package com.github.philipsonfhir.fhircast.server.wsstomp;

import com.github.philipsonfhir.fhircast.server.topic.FhirCastTopicEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastContext;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.hl7.fhir.dstu3.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class FhircastWebsocketService implements ApplicationListener<FhirCastTopicEvent>  {

    @Autowired
    Environment environment;

    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private String websocketUrl;
    private static final String SEND_FHIRCAST_EVENT_ENDPOINT = "/app/fhircast/";
    private static final String SUBSCRIBE_FHIRCAST_EVENT_ENDPOINT = "/hub/fhircast/";


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



    @Override
    public void onApplicationEvent(FhirCastTopicEvent event) {
        logger.info( "onApplicationEvent "+event.getTopic()+" "+event.getEventType() );
        //        stompSession.subscribe( SUBSCRIBE_FHIRCAST_EVENT_ENDPOINT + uuid+"/event", this);
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        fhirCastWorkflowEvent.setId( "WS"+System.currentTimeMillis() );
        fhirCastWorkflowEvent.setTimestamp( new Date().toString() );

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
        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );

        this.sendEvent( fhirCastWorkflowEventEvent );
    }

//    private Map<String, FhirCastSessionSubscribe> fhirCastSubscriptions = new TreeMap<>();
//    private Map<String, FhirCastWebsubClientData> fhirCastClientMap = new TreeMap<>( );
//
//    public void updateSubscriptions(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
//        FhirCastWebsubClientData fhirCastWebsubClientData = getFhirCastClientData( fhirCastSessionSubscribe );
//        if ( fhirCastSessionSubscribe.getHub_mode().equals("subscribe")){
//            this.fhirCastSubscriptions.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe );
//            fhirCastWebsubClientData.subscribe( fhirCastSessionSubscribe.getHub_events() );
//            WebsubVerificationProcess.verify( fhirCastWebsubClientData, fhirCastSessionSubscribe );
//        } else if ( fhirCastSessionSubscribe.getHub_mode().equals("unsubscribe")) {
//            this.fhirCastSubscriptions.remove( fhirCastSessionSubscribe.getHub_callback() );
//            fhirCastWebsubClientData.unsubscribe( fhirCastSessionSubscribe.getHub_events() );
//        } else {
//            throw new FhirCastException("Unknown value for hub.mode "+fhirCastSessionSubscribe.getHub_mode());
//        }
//    }
//
//    private FhirCastWebsubClientData getFhirCastClientData(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
//        FhirCastWebsubClientData fhirCastWebsubClientData = this.fhirCastClientMap.get( fhirCastSessionSubscribe.getHub_callback() );
//        if ( fhirCastWebsubClientData == null ){
//            fhirCastWebsubClientData = new FhirCastWebsubClientData( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe.getHub_secret() );
//            this.fhirCastClientMap.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastWebsubClientData );
//        }
//        return fhirCastWebsubClientData;
//    }

}
