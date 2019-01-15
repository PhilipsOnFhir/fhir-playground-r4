package com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.service;

public class FhirCastException extends Throwable {
    public FhirCastException(String unknownSessionId) {
        super( unknownSessionId );
    }

}
