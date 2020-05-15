package org.github.philipsonfhir.fhirproxy.common.operation;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.hl7.fhir.r4.model.OperationDefinition;

public interface FhirOperation {
    FhirCall createFhirCall(IFhirServer fhirServer, FhirRequest fhirRequest) throws FhirProxyException;

    public String getOperationName();

    OperationDefinition getOperation();
}
