package org.github.philipsonfhir.worklist.fhircast.server.websub.service;

import lombok.ToString;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.FhirCastTopicEvent;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastContext;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastWorkflowEventEvent;
import org.github.philipsonfhir.worklist.fhircast.server.websub.service.websocket.FhircastWebsubClientWebsocket;
import org.github.philipsonfhir.worklist.fhircast.server.websub.service.websocket.SocketHandler;
import org.github.philipsonfhir.worklist.fhircast.server.websub.service.websub.FhircastWebsubClientRest;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.Resource;
import java.util.*;
import java.util.logging.Logger;

@ToString
public class FhirCastWebsubSession {
    private final SocketHandler socketHandler;
    //    private Map<String, FhirCastSessionSubscribe> fhirCastSubscriptions = new TreeMap<>();
    private Map<String, FhircastWebsubClientRest>      fhirCastRestClientMap      = new TreeMap<>( );
    private Map<String, FhircastWebsubClientWebsocket> fhirCastWebsocketClientMap = new TreeMap<>( );
    private String topicId;
    Logger logger = Logger.getLogger( this.getClass().getName() );
    Map<String, String> context = new TreeMap<>(  );
    private boolean verified;

    FhirCastWebsubSession(String topicId, SocketHandler socketHandler){
        this.topicId = topicId;
        this.socketHandler = socketHandler;
    }

    String updateSubscriptions(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        FhirCastWebsubClient fhirCastWebsubClient = getFhirCastClientData( fhirCastSessionSubscribe );
        if ( fhirCastSessionSubscribe.getHub_mode().equals("subscribe")){
            fhirCastWebsubClient.subscribe( fhirCastSessionSubscribe );
        } else if ( fhirCastSessionSubscribe.getHub_mode().equals("unsubscribe")) {
            fhirCastWebsubClient.unsubscribe( fhirCastSessionSubscribe );
        } else {
            throw new FhirCastException("Unknown value for hub.mode "+fhirCastSessionSubscribe.getHub_mode());
        }

        return  ( fhirCastWebsubClient.getSubscriptionReturn() );
    }


    private FhirCastWebsubClient getFhirCastClientData(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        if( fhirCastSessionSubscribe.hasWebsocketChannelType() ){
            FhircastWebsubClientWebsocket fhirCastWebsubClient =
                  this.fhirCastWebsocketClientMap.get( fhirCastSessionSubscribe.getHub_channel_endpoint() );
            if ( fhirCastWebsubClient == null ){
                fhirCastWebsubClient = new FhircastWebsubClientWebsocket( socketHandler, fhirCastSessionSubscribe.getHub_topic() );
                this.fhirCastWebsocketClientMap.put( fhirCastSessionSubscribe.getHub_channel_endpoint(), fhirCastWebsubClient);
            }
            return  fhirCastWebsubClient;
        } else {
            FhircastWebsubClientRest fhirCastWebsubClient = this.fhirCastRestClientMap.get( fhirCastSessionSubscribe.getHub_callback() );
            if ( fhirCastWebsubClient == null ){
                fhirCastWebsubClient = new FhircastWebsubClientRest( fhirCastSessionSubscribe );
                this.fhirCastRestClientMap.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastWebsubClient);
            }
            return  fhirCastWebsubClient;
        }
    }


    public String getTopicId() {
        return topicId;
    }



    public Map<String, String> getContext() {
        return Collections.unmodifiableMap( context );
    }
    private void updateContext(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        fhirCastWorkflowEvent.getEvent().getContext()
                    .forEach(fhirCastContext -> this.context.put(fhirCastContext.getKey(), fhirCastContext.getResource()));
            //TODO check whether this is valid.....
    }



    public void newFhirCastTopicEvent(FhirCastTopicEvent event) throws FhirCastException {
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
//        fhirCastWorkflowEvent.setTimestamp( String.valueOf( new Date() ) );
//        fhirCastWorkflowEvent.setId( "FC"+System.currentTimeMillis() );
        fhirCastWorkflowEvent.initialize();

        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( event.getEventType() );
        fhirCastWorkflowEventEvent.setHub_topic( event.getTopic() );

        List<FhirCastContext> contextList = new ArrayList<>(  );
        event.getContext().entrySet().forEach( entry ->{
            FhirCastContext fhirCastContext = new FhirCastContext();
            fhirCastContext.setKey( entry.getKey() );
            fhirCastContext.setResource( (Resource) entry.getValue() );
            contextList.add( fhirCastContext );
        } );
        fhirCastWorkflowEventEvent.setContext( contextList );
        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );

        updateContext( fhirCastWorkflowEvent );

        for ( FhirCastWebsubClient fhircastWebsubClient: this.fhirCastWebsocketClientMap.values() ) {
            fhircastWebsubClient.sendEvent(fhirCastWorkflowEvent);
        }
        for ( FhirCastWebsubClient fhircastWebsubClient: this.fhirCastRestClientMap.values() ) {
            fhircastWebsubClient.sendEvent(fhirCastWorkflowEvent);
        }
    }
}
