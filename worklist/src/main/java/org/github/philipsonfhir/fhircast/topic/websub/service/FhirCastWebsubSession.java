package org.github.philipsonfhir.fhircast.topic.websub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
import lombok.ToString;
import org.github.philipsonfhir.fhircast.topic.service.FhirCastTopicEvent;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastContext;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastWorkflowEventEvent;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.logging.Logger;

@ToString
public class FhirCastWebsubSession {
    private Map<String, FhirCastSessionSubscribe> fhirCastSubscriptions = new TreeMap<>();
    private Map<String, FhirCastWebsubClientData> fhirCastClientMap = new TreeMap<>( );
    private String topicId;
    Logger logger = Logger.getLogger( this.getClass().getName() );
    Map<String, String> context = new TreeMap<>(  );
    private boolean verified;

    FhirCastWebsubSession(String topicId ){
        this.topicId = topicId;
    }

    void updateSubscriptions(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        FhirCastWebsubClientData fhirCastWebsubClientData = getFhirCastClientData( fhirCastSessionSubscribe );
        if ( fhirCastSessionSubscribe.getHub_mode().equals("subscribe")){
            this.fhirCastSubscriptions.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe );
            fhirCastWebsubClientData.subscribe( fhirCastSessionSubscribe.getHub_events() );
            WebsubVerificationProcess.verify( fhirCastWebsubClientData, fhirCastSessionSubscribe );
        } else if ( fhirCastSessionSubscribe.getHub_mode().equals("unsubscribe")) {
            this.fhirCastSubscriptions.remove( fhirCastSessionSubscribe.getHub_callback() );
            fhirCastWebsubClientData.unsubscribe( fhirCastSessionSubscribe.getHub_events() );
        } else {
            throw new FhirCastException("Unknown value for hub.mode "+fhirCastSessionSubscribe.getHub_mode());
        }

    }

    private FhirCastWebsubClientData getFhirCastClientData(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        FhirCastWebsubClientData fhirCastWebsubClientData = this.fhirCastClientMap.get( fhirCastSessionSubscribe.getHub_callback() );
        if ( fhirCastWebsubClientData == null ){
            fhirCastWebsubClientData = new FhirCastWebsubClientData( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe.getHub_secret() );
            this.fhirCastClientMap.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastWebsubClientData );
        }
        return fhirCastWebsubClientData;
    }


    public String getTopicId() {
        return topicId;
    }


    RestTemplate restTemplate = new RestTemplate();
    private void sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) throws FhirCastException {
        //TODO check permission to send
        updateContext( fhirCastWorkflowEvent );

        // send websub events
        logger.info( "eventReceived " + fhirCastWorkflowEvent );
        for ( FhirCastWebsubClientData fhirCastWebsubClientData : this.fhirCastClientMap.values() ) {
            if ( fhirCastWebsubClientData.isVerified() && fhirCastWebsubClientData.hasSubscription( fhirCastWorkflowEvent.getEvent().getHub_event() ) ) {
                logger.info( "Sending event to " + fhirCastWebsubClientData.getClientCallbackUrl() );

                HttpHeaders httpHeaders = new HttpHeaders();
                ObjectMapper objectMapper = new ObjectMapper();
                String content = null;
                try {
                    content = objectMapper.writeValueAsString( fhirCastWorkflowEvent );
                } catch ( JsonProcessingException e ) {
                    throw new FhirCastException( "parsing Event failed" );
                }

                httpHeaders.add( "X-Hub-Signature", calculateHMAC( fhirCastWebsubClientData.getHubSecret(), content ) );

                HttpEntity<String> entity = new HttpEntity<>( content, httpHeaders );
                ResponseEntity<String> response = restTemplate.exchange(
                    fhirCastWebsubClientData.getClientCallbackUrl(),
                    HttpMethod.POST, entity, String.class
                );
                logger.info( "Sending event to " + fhirCastWebsubClientData.getClientCallbackUrl() + " : " + response.getStatusCode() );
            }
        }
    }


    private void updateContext(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        fhirCastWorkflowEvent.getEvent().getContext()
            .forEach( fhirCastContext -> this.context.put( fhirCastContext.getKey(), fhirCastContext.getResource() ) );
        //TODO check whether this is valid.....
    }


    private static String calculateHMAC(String secret, String content ) throws FhirCastException {
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            String magicKey = "hellothere";
            mac.init(new SecretKeySpec(magicKey.getBytes(), "HmacSHA256"));

            byte[] hash = mac.doFinal(secret.getBytes());
            return DatatypeConverter.printHexBinary(hash);
        } catch ( Exception e ) {
            throw new FhirCastException( "Error generating HMAC" );
        }
    }


    public Map<String, String> getContext() {
        return context;
    }

    public void newFhirCastTopicEvent(FhirCastTopicEvent event) throws FhirCastException {
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        fhirCastWorkflowEvent.setTimestamp( String.valueOf( new Date() ) );
        fhirCastWorkflowEvent.setId( "FC"+System.currentTimeMillis() );

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
        sendEvent( fhirCastWorkflowEvent );
    }
}
