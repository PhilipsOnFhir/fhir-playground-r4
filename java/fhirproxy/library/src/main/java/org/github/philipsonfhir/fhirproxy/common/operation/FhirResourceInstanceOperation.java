package org.github.philipsonfhir.fhirproxy.common.operation;

import org.hl7.fhir.r4.model.ResourceType;

public interface FhirResourceInstanceOperation extends FhirOperation{
    public ResourceType getResourceType();
}
