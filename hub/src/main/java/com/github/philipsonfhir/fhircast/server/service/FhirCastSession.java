package com.github.philipsonfhir.fhircast.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import lombok.ToString;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.github.philipsonfhir.fhircast.support.websub.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

@ToString
public class FhirCastSession {
    private Map<String, FhirCastSessionSubscribe> fhirCastSubscriptions = new TreeMap<>();
    private Map<String, FhirCastClientData> fhirCastClientMap = new TreeMap<>( );
    private String topicId;
    Logger logger = Logger.getLogger( this.getClass().getName() );
    Map<String, String> context = new TreeMap<>(  );
    private boolean verified;
    private EventChannelListener webSocketListener;

    public FhirCastSession(String topicId ){
        this.topicId = topicId;
    }

    public void updateSubscriptions(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        FhirCastClientData fhirCastClientData = getFhirCastClientData( fhirCastSessionSubscribe );
        if ( fhirCastSessionSubscribe.getHub_mode().equals("subscribe")){
            this.fhirCastSubscriptions.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe );
            fhirCastClientData.subscribe( fhirCastSessionSubscribe.getHub_events() );
            VerificationProcess.verify( fhirCastClientData, fhirCastSessionSubscribe );
        } else if ( fhirCastSessionSubscribe.getHub_mode().equals("unsubscribe")) {
            this.fhirCastSubscriptions.remove( fhirCastSessionSubscribe.getHub_callback() );
            fhirCastClientData.unsubscribe( fhirCastSessionSubscribe.getHub_events() );
        } else {
            throw new FhirCastException("Unknown value for hub.mode "+fhirCastSessionSubscribe.getHub_mode());
        }

    }

    private FhirCastClientData getFhirCastClientData(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        FhirCastClientData fhirCastClientData = this.fhirCastClientMap.get( fhirCastSessionSubscribe.getHub_callback() );
        if ( fhirCastClientData == null ){
            fhirCastClientData = new FhirCastClientData( fhirCastSessionSubscribe.getHub_callback(), fhirCastSessionSubscribe.getHub_secret() );
            this.fhirCastClientMap.put( fhirCastSessionSubscribe.getHub_callback(), fhirCastClientData );
        }
        return fhirCastClientData;
    }


    public String getTopicId() {
        return topicId;
    }


    RestTemplate restTemplate = new RestTemplate();
    public void sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) throws FhirCastException {
        //TODO check permission to send
        updateContext( fhirCastWorkflowEvent );

        // send websub events
        logger.info( "sendEvent " + fhirCastWorkflowEvent );
        for ( FhirCastClientData fhirCastClientData: this.fhirCastClientMap.values() ) {
            if ( fhirCastClientData.isVerified() && fhirCastClientData.hasSubscription( fhirCastWorkflowEvent.getEvent().getHub_event() ) ) {
                logger.info( "Sending event to " + fhirCastClientData.getClientCallbackUrl() );

                HttpHeaders httpHeaders = new HttpHeaders();
                ObjectMapper objectMapper = new ObjectMapper();
                String content = null;
                try {
                    content = objectMapper.writeValueAsString( fhirCastWorkflowEvent );
                } catch ( JsonProcessingException e ) {
                    throw new FhirCastException( "parsing Event failed" );
                }

                httpHeaders.add( "X-Hub-Signature", calculateHMAC( fhirCastClientData.getHubSecret(), content ) );

                HttpEntity<String> entity = new HttpEntity<>( content, httpHeaders );
                ResponseEntity<String> response = restTemplate.exchange(
                    fhirCastClientData.getClientCallbackUrl(),
                    HttpMethod.POST, entity, String.class
                );
                logger.info( "Sending event to " + fhirCastClientData.getClientCallbackUrl() + " : " + response.getStatusCode() );
            }
        }
    }


    private void updateContext(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        fhirCastWorkflowEvent.getEvent().getContext().stream()
            .forEach( fhirCastContext -> this.context.put( fhirCastContext.getKey(), fhirCastContext.getResource() ) );
        //TODO check whether this is valid.....
    }


    public static String calculateHMAC(String secret, String content ) throws FhirCastException {
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

    public void setCorrect(boolean success) {
        this.verified = success ;
    }
}
