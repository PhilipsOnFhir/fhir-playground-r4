package org.github.philipsonfhir.fhirproxy.common;

import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperation;
import org.hl7.fhir.r4.model.CapabilityStatement;

import java.util.List;

public interface ImplementationGuide {

    public CapabilityStatement updateCapabilityStatement( CapabilityStatement capabilityStatement );

    List<FhirOperation> getOperations();
}
