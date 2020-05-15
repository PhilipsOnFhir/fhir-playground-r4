package org.github.philipsonfhir.fhirproxy.common.operation;

import org.hl7.fhir.r4.model.ResourceType;

public interface FhirResourceOperation extends FhirOperation{
    public ResourceType getResourceType();
}
