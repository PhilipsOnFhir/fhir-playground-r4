package org.github.philipsonfhir.fhirproxy.common.fhirserver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.Map;

public interface IFhirServer {
    public CapabilityStatement getCapabilityStatement() throws FhirProxyException;
    public IBaseResource doGet(ResourceType resourceType,                                          Map<String, String> queryParams);
    public IBaseResource doGet(String resourceType,                                                Map<String, String> queryParams);
    public IBaseResource doGet(ResourceType resourceType, String resourceId,                       Map<String, String> queryParams);
    public IBaseResource doGet(String resourceType,       String resourceId,                       Map<String, String> queryParams);
    public IBaseResource doGet(String resourceType,       String resourceId, String operationName, Map<String, String> queryParams);
    public IBaseResource doGet(String url);
    IBaseResource doGet(Reference subject);

    public Bundle loadPage(Bundle resultBundle) throws FhirProxyException;

    public IBaseOperationOutcome doPut(Resource iBaseResource) throws FhirProxyException;

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public IBaseResource doPost(
            String resourceType,
            String resourceId,
            String operationName,
            IBaseResource parseResource,
            Map<String, String> queryParams
    ) throws FHIRException, FhirProxyException;
    
    public IBaseResource doPost(String resourceType, String resourceId, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException;
    public IBaseResource doPost(String resourceType, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException;
    public IBaseResource doPost(IBaseResource bodyResource, Map<String, String> queryMap);

    public MethodOutcome doPost(IBaseResource iBaseResource ) throws FhirProxyException;

    public String doPost(String requestBody) throws FhirProxyException;

    public FhirContext getCtx();

    public IBaseResource doDelete(String resourceType, Map<String, String> queryMap) throws FhirProxyException;

    public IBaseResource doDelete(String s, String s1, Map<String, String> queryMap) throws FhirProxyException;

    public IBaseResource doDelete(String s, String s1, String s2, Map<String, String> queryMap) throws FhirProxyException;

    public String getServerUrl();

    public Bundle doGetCannonical(ResourceType resourceType, CanonicalType canonical ) throws FhirProxyNotImplementedException;

}

