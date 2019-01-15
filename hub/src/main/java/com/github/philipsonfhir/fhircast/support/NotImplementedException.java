package com.github.philipsonfhir.fhircast.support;

import org.hl7.fhir.exceptions.FHIRException;

public class NotImplementedException extends FHIRException {
    public NotImplementedException() {
        super(  );
    }
    public NotImplementedException(String s) {
        super( s );
    }
}
