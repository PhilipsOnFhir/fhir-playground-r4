package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FhirCastWebHookSubscribtionResponse {
    @JsonProperty("hub.channel.type")  String hub_channel_type ="webhook";
    @JsonProperty("hub.mode")          String hub_mode;
    @JsonProperty("hub.topic")         String hub_topic;
    @JsonProperty("hub.events")        String hub_events;
    @JsonProperty("hub.callback")      String hub_callback;
    @JsonProperty("hub.secret")        String hub_secret;
    @JsonProperty("hub.challenge")     String hub_challenge;
    @JsonProperty("hub.lease_seconds") String hub_leaseSeconds;
}
