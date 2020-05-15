package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Getter
@Setter
public class SubscriptionUpdate {
    @JsonProperty("hub.channel.type")       String hub_channel_type ="undefined";
    @JsonProperty("hub.mode")               String hub_mode;
    @JsonProperty("hub.topic")              String hub_topic;
    @JsonProperty("hub.events")             String hub_events;
    @JsonProperty("hub.lease_seconds")      String hub_lease_seconds;
    @JsonProperty("hub.callback")           String hub_callback;
    @JsonProperty("hub.secret")             String hub_secret;
    @JsonProperty("hub.challenge")          String hub_challenge;
    @JsonProperty("hub.channel.endpoint")   String hub_channel_endpoint;
    @JsonProperty("hub.reason")             String hub_reason;

    public SubscriptionUpdate(){}
    public SubscriptionUpdate(MultiValueMap<String, String> multipleFormVars) {
        Map<String, String> formVars = multipleFormVars.toSingleValueMap();
        hub_channel_type = formVars.get("hub.channel.type");
        hub_mode = formVars.get("hub.mode");
        hub_topic = formVars.get("hub.topic");
        hub_events = formVars.get("hub.events");
        hub_lease_seconds = formVars.get("hub.lease_seconds");
        hub_callback = formVars.get("hub.callback");
        hub_secret = formVars.get("hub.secret");
        hub_challenge = formVars.get("hub.challenge");
        hub_channel_endpoint = formVars.get("hub.channel.endpoint");
        hub_reason = formVars.get("hub.reason");
    }
}
