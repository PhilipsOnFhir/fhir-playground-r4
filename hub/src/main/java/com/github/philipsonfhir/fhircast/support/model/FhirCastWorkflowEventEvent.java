package com.github.philipsonfhir.fhircast.support.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class FhirCastWorkflowEventEvent {
    @JsonProperty("hub.topic")      String hub_topic;
//    String hub_event;
    @JsonProperty("hub.event")
FhircastEventType hub_event;
    List<FhirCastContext> context = new ArrayList<>();

}
