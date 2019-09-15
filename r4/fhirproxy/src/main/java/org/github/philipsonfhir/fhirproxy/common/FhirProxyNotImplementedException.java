package org.github.philipsonfhir.fhirproxy.common;

public class FhirProxyNotImplementedException extends FhirProxyException{
    public FhirProxyNotImplementedException(){
        super();
    }

    public FhirProxyNotImplementedException(String s) {
        super((s));
    }
}
