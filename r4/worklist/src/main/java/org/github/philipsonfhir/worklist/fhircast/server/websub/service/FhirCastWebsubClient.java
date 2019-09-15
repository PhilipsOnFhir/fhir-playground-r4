package org.github.philipsonfhir.worklist.fhircast.server.websub.service;

import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhircastEventType;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.github.philipsonfhir.worklist.fhircast.support.NotImplementedException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public abstract class FhirCastWebsubClient {
    Set<FhircastEventType> subscriptions = new TreeSet<>();
    private String secret;
    private boolean verified;

    public FhirCastWebsubClient( String secret) {
        this.secret = secret;
    }


    public void subscribe( String subscribtions) throws NotImplementedException {
        String[] subscriptions =  subscribtions.split( "," );
        for( String subscription: subscriptions){
            FhircastEventType fhircastEventType = FhircastEventType.get( subscription );
            this.subscriptions.add( fhircastEventType );
        }
    }

    public boolean hasSubscription(FhircastEventType eventType) {
        return subscriptions.contains( eventType );
    }

    public void unsubscribe(String subscribtions) {
        String[] subscriptions =  subscribtions.split( "," );
        for( String subscription: subscriptions){
            FhircastEventType fhircastEventType = FhircastEventType.get( subscription );
            this.subscriptions.remove( fhircastEventType );
        }
    }

    public String getHubSecret() {
        return this.secret;
    }

    public void setVerified(boolean success) {
        this.verified = success;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public String calculateHMAC(String content ) throws FhirCastException {
        return calculateHMAC( getHubSecret(), content );
    }

    String calculateHMAC(String secret, String content ) throws FhirCastException {
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

    public abstract String getSubscriptionReturn();

    public abstract void sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEventEvent) throws FhirCastException;

    protected Set<FhircastEventType> getSubscriptions() {
        return Collections.unmodifiableSet( this.subscriptions );
    }

    public abstract void subscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws NotImplementedException;

    public abstract  void unsubscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws NotImplementedException;
}
