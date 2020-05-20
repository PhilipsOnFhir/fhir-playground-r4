package org.github.philipsonfhir.smartsuite.fhircast.client;

import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

public class FhirCastNotificationException extends FhirCastException {
    public FhirCastNotificationException(String message) {
        super( message );
    }
}
