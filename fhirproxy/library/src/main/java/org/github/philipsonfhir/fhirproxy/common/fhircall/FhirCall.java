package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;

import java.util.Map;

public interface FhirCall {
    public void execute() throws FhirProxyException;

    public String getStatusDescription();

    IBaseResource getResource() throws FhirProxyException;

    IFhirServer getFhirServer();

    Map<String, OperationOutcome> getErrors();

//    List<OperationOutcome> getErrors();

}
