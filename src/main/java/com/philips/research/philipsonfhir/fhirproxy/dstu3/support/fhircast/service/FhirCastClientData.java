package com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.service;

import com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model.FhircastEventType;

import java.util.*;

public class FhirCastClientData {
    String clientCallbackUrl;
    Set<FhircastEventType> subscriptions = new TreeSet<>();
    private String secret;
    private boolean verified;

    public FhirCastClientData(String clientCallbackUrl, String secret) {
        this.clientCallbackUrl = clientCallbackUrl;
        this.secret = secret;
    }

    public String getClientCallbackUrl() {
        return clientCallbackUrl;
    }

    public void subscribe(String subscribtions) {
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
}
