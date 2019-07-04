package org.github.philipsonfhir.fhircast.server.websub.domain;

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
