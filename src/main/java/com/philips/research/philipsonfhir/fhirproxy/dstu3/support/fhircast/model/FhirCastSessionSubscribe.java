package com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FhirCastSessionSubscribe {
    @JsonProperty("hub.callback")      String hub_callback;
    @JsonProperty("hub.mode")          String hub_mode;
    @JsonProperty("hub.topic")         String hub_topic;
    @JsonProperty("hub.secret")        String hub_secret;
    @JsonProperty("hub.events")        String hub_events;
    @JsonProperty("hub.lease_seconds") String hub_lease_seconds;
}
