package com.github.philipsonfhir.fhircast.support.websub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FhirCastBody {
    // updateSubscriptions
    @JsonProperty("hub.callback")      String hub_callback;
    @JsonProperty("hub.mode")          String hub_mode;
    @JsonProperty("hub.topic")         String hub_topic;
    @JsonProperty("hub.secret")        String hub_secret;
    @JsonProperty("hub.events")        String hub_events;
    @JsonProperty("hub.lease_seconds") String hub_lease_seconds;

    // event
    String timestamp;
    String id;
    FhirCastWorkflowEventEvent event;

    public boolean isSubscribe(){
        return hub_callback!=null;
    }

    public FhirCastSessionSubscribe getFhirCastSessionSubscribe(){
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.hub_callback = hub_callback;
        fhirCastSessionSubscribe.hub_mode     = hub_mode;
        fhirCastSessionSubscribe.hub_topic    = hub_topic;
        fhirCastSessionSubscribe.hub_secret   = hub_secret;
        fhirCastSessionSubscribe.hub_events   = hub_events;
        fhirCastSessionSubscribe.hub_lease_seconds = hub_lease_seconds;
        return fhirCastSessionSubscribe;
    }

    public boolean isEvent(){
        return event!=null;
    }

    public FhirCastWorkflowEvent getFhirCastWorkflowEvent(){
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        fhirCastWorkflowEvent.timestamp = timestamp;
        fhirCastWorkflowEvent.id = id;
        fhirCastWorkflowEvent.event = event;
        return  fhirCastWorkflowEvent;
    }
}
