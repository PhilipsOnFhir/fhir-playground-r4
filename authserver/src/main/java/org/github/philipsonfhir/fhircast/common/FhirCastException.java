package org.github.philipsonfhir.fhircast.common;

public class FhirCastException extends Throwable {
    public FhirCastException(String unknownSessionId) {
        super( unknownSessionId );
    }

}
