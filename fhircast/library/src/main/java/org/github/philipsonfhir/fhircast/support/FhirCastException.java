package org.github.philipsonfhir.fhircast.support;

public class FhirCastException extends Throwable {
    public FhirCastException(String unknownSessionId) {
        super( unknownSessionId );
    }

}
