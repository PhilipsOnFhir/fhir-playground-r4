package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface FhirCall {
    public void execute();

    public String getStatusDescription();

    IBaseResource getResource() throws FhirProxyException;

    FhirServer getFhirServer();

    Map<String, OperationOutcome> getErrors();

//    List<OperationOutcome> getErrors();

}
