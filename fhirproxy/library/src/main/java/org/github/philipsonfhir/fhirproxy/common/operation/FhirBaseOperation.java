package org.github.philipsonfhir.fhirproxy.common.operation;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;

public interface FhirBaseOperation extends FhirOperation {
    public FhirCall createFhirCall(IFhirServer fhirServer, FhirRequest fhirRequest) throws FhirProxyException;

    String getOperationName();
}
