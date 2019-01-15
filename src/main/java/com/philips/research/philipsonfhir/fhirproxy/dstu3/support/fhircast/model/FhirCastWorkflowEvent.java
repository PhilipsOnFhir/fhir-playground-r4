package com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FhirCastWorkflowEvent {
    String timestamp;
    String id;
    FhirCastWorkflowEventEvent event;
}
