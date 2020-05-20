package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HubPostData {
    @JsonProperty("hub.channel.type")     String hub_channel_type;          // subscribe unsubscribe
    @JsonProperty("hub.mode")             String hub_mode;                  // subscribe unsubscribe
    @JsonProperty("hub.topic")            String hub_topic;                 // subscribe unsubscribe
    @JsonProperty("hub.events")           String hub_events;                // subscribe unsubscribe
    @JsonProperty("hub.lease_seconds")    String hub_lease_seconds;         // subscribe unsubscribe
    @JsonProperty("hub.callback")         String hub_callback;              // subscribe unsubscribe
    @JsonProperty("hub.secret")           String hub_secret;                // subscribe unsubscribe
    @JsonProperty("hub.challenge")        String hub_challenge;             // subscribe unsubscribe
    @JsonProperty("hub.channel.endpoint") String hub_channel_endpoint;      // subscribe unsubscribe

    String timestamp;                                                       // request context change
    String id;                                                              // request context change
    ContextEventEvent event;                                                            // request context change

    public SubscriptionUpdate getFhirCastSubscriptionRequest(){
        SubscriptionUpdate subscriptionRequest = new SubscriptionUpdate();
        subscriptionRequest.setHub_callback( hub_callback );
        subscriptionRequest.setHub_challenge( hub_challenge );
        subscriptionRequest.setHub_channel_type(hub_channel_type);
        subscriptionRequest.setHub_callback( hub_callback );
        subscriptionRequest.setHub_events( hub_events );
        subscriptionRequest.setHub_mode( hub_mode );
        subscriptionRequest.setHub_topic( hub_topic );
        subscriptionRequest.setHub_lease_seconds( hub_lease_seconds );
        subscriptionRequest.setHub_secret(hub_secret);
        return subscriptionRequest;
    }

    public boolean isSubscribeUnsubscribe() {
        return  this.hub_channel_type!=null && this.id==null;
    }

    public ContextEvent getFhirCastEvent() {
        ContextEvent contextEvent = new ContextEvent();
        contextEvent.setId( this.id );
        contextEvent.setTimestamp( this.timestamp );
        contextEvent.setEvent( this.event );
        return contextEvent;
    }
}
