package org.github.philipsonfhir.fhirproxy.common;

public class FhirProxyError extends Error {
    public FhirProxyError() {
        super();
    }

    public FhirProxyError(String msg) {
        super(msg );
    }
}
