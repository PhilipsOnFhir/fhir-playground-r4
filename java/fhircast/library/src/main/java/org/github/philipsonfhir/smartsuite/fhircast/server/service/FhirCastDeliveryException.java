package org.github.philipsonfhir.smartsuite.fhircast.server.service;

import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

public class FhirCastDeliveryException extends FhirCastException {
    public FhirCastDeliveryException(String message) {
        super( message );
    }
}
