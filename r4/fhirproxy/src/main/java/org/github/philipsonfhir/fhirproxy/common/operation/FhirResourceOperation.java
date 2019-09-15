package org.github.philipsonfhir.fhirproxy.common.operation;

public interface FhirResourceOperation extends FhirOperation{
    public String getResourceType();
    public String getOperationName();
}
