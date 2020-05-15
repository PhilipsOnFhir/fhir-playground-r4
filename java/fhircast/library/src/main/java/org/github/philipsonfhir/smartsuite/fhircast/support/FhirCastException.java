package org.github.philipsonfhir.smartsuite.fhircast.support;

import org.github.philipsonfhir.smartsuite.FHIRException;

public class FhirCastException extends FHIRException{
    public FhirCastException(String msg) {
        super( msg );
    }

    public FhirCastException(Exception e) {
        this( e.getMessage() );
    }


    public static class NotImplementedException extends FHIRException {
        public NotImplementedException() {
            super();
        }
        public NotImplementedException(String s) {
            super( s );
        }
    }

}
