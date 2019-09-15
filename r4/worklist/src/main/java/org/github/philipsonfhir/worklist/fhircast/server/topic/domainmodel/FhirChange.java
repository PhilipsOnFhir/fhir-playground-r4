package org.github.philipsonfhir.worklist.fhircast.server.topic.domainmodel;

import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.Resource;

@Getter
@Setter
public class FhirChange {
    public enum ChangeEnum { CREATE, UPDATE, DELETE } ;

    ChangeEnum change;
    Resource resource;
}
