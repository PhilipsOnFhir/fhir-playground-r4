package org.github.philipsonfhir.smartsuite.fhircast.server.service.util;

import java.util.*;

public class SubscriptionManager {
    private Set<String> subscriptions = new TreeSet<>();

    public SubscriptionManager(){
    }

    public void addSubscriptions(Set<String> newSubscriptions ) {
        this.subscriptions.addAll( newSubscriptions  );
    }

    public void addSubscriptions(String subscriptionString ) {
        String[] events = subscriptionString.split(",");
        addSubscriptions( new HashSet<>(Arrays.asList(events)) );
    }

    public void removeSubscriptions(Set<String> newSubscriptions ) {
        this.subscriptions.removeAll( newSubscriptions  );
    }

    public void removeSubscriptions(String subscriptionString ) {
        String[] events = subscriptionString.split(",");
        removeSubscriptions( new HashSet<>(Arrays.asList(events)) );
    }

    public boolean hasSubscription(String hub_event) {
        return hasSubscription( this.subscriptions, hub_event );
    }

    private static final String SEPARATOR = "-";
    private static final String WILDCARD = "*";
    static boolean hasSubscription( Set<String> subscriptions, String event ){
        boolean found;
        found =  subscriptions.contains("*") || subscriptions.contains(event) ;
        if ( !found && event.contains(SEPARATOR)){
            String resource = event.substring(0, event.indexOf(SEPARATOR));
            String action = event.substring(event.indexOf(SEPARATOR)+1);
            Iterator<String> it = subscriptions.iterator();
            while( !found && it.hasNext() ){
                String subscription = it.next();
                if ( subscription.contains(SEPARATOR)) {
                    String subscriptionResource = subscription.substring(0, subscription.indexOf(SEPARATOR));
                    String subscriptionAction   = subscription.substring(subscription.indexOf(SEPARATOR) + 1);
                    found=  ( subscriptionResource.equals(WILDCARD) || subscriptionResource.equals(resource) ) &&
                            ( subscriptionAction.equals(WILDCARD)   || subscriptionAction.equals(action) ) ;
                }
            }
        }
        return found;
    }

    public String getEvents() {
        String result="";
        Iterator<String> it = this.subscriptions.iterator();
        while ( it.hasNext() ){
            result+=it.next();
            if ( it.hasNext() ){ result+=","; }
        }
        return result;
    }

    public void removeAllSubscriptions() {
        this.subscriptions.clear();
    }
}
