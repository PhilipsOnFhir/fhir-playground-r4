package org.github.philipsonfhir.fhirproxy.common;

import org.hl7.fhir.r4.model.CapabilityStatement;

public interface ImplementationGuide {

    public CapabilityStatement updateCapabilityStatement( CapabilityStatement capabilityStatement );
}
