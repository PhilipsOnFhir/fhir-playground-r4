package org.github.philipsonfhir.fhirproxy.common;

public class FhirProxyException extends Exception {
    public FhirProxyException(){
        super();
    }
    public FhirProxyException(Exception e){
        super(e);
    }

    public FhirProxyException(String s) {
        super(s);
    }
}
