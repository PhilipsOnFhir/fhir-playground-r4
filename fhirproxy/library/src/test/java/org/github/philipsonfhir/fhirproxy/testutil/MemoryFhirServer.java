package org.github.philipsonfhir.fhirproxy.testutil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedError;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.common.util.ReferenceUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemoryFhirServer implements IFhirServer {
    Map<String, Map<String, Resource>> resourceIdMap = new HashMap<>();
    Map<String, Map<String, Resource>> resourceUrlMap = new HashMap<String, Map<String, Resource>>();
    private FhirContext ourCtx = FhirContext.forR4();
    private static final String urlField = "url";

    private Map<String, Resource> getResourceIdMap(ResourceType type ){
        Map<String, Resource> map = resourceIdMap.get( type.name() );
        if ( map==null ){
            map = new HashMap<>();
            resourceIdMap.put(type.name(), map );
        }
        return map;
    }

    private Map<String, Resource> getResourceUrlMap(ResourceType type ){
        Map<String, Resource> map = resourceUrlMap.get( type.name() );
        if ( map==null ){
            map = new HashMap<>();
            resourceUrlMap.put(type.name(), map );
        }
        return map;
    }

    @Override
    public CapabilityStatement getCapabilityStatement() throws FhirProxyException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public IBaseResource doGet(ResourceType resourceType, Map<String, String> queryParams) {
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doGet(String resourceType, Map<String, String> queryParams) {
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doGet(ResourceType resourceType, String resourceId, Map<String, String> queryParams) throws ResourceNotFoundException {
        Map<String, Resource> map = getResourceIdMap(resourceType);
        Resource result = map.get(resourceId);
        if ( result==null ){
            throw new ResourceNotFoundException( "failed");
        }
        return result;
    }

    @Override
    public IBaseResource doGet(String resourceType, String resourceId, Map<String, String> queryParams) {
        Map<String, Resource> map = getResourceIdMap( ResourceType.fromCode(resourceType));
        return map.get( resourceId  );
    }

    @Override
    public IBaseResource doGet(String resourceType, String resourceId, String operationName, Map<String, String> queryParams) {
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doGet(String url) {
        return null;
    }

    @Override
    public IBaseResource doGet(Reference subject) {
        ReferenceUtil.ParsedReference pr = ReferenceUtil.parseReference(subject);
        return doGet( pr.getResourceType(), pr.getResourceId(), new HashMap<>() );
    }

    @Override
    public Bundle loadPage(Bundle resultBundle) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public IBaseOperationOutcome doPut(Resource resource) throws FhirProxyException {
        getResourceIdMap(resource.getResourceType())
                .put(resource.getIdElement().getIdPart(), resource);

        Map<String, Resource> urlMap = getResourceUrlMap( resource.getResourceType());
        Property property = resource.getNamedProperty(urlField.hashCode(), urlField, false);
        if (property == null) {
        } else {
            Optional<Base> res = property.getValues().stream().findFirst();
            if (res.isPresent()) {
                urlMap.put(res.get().primitiveValue(), resource);
            }
        }
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public IBaseResource doPost(String resourceType, String resourceId, String operationName, IBaseResource parseResource, Map<String, String> queryParams) throws FHIRException, FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public IBaseResource doPost(String resourceType, String resourceId, IBaseResource body, Map<String, String> queryParams) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public IBaseResource doPost(String resourceType, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException {
        if ( !(body instanceof Resource)){
            return new OperationOutcome()
                    .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                            .setSeverity( OperationOutcome.IssueSeverity.ERROR )
                            .setDiagnostics("body does not contain a resource")
                    );
        }
        Resource resource = (Resource)body;
        resource.setId( UUID.randomUUID().toString() );
        this.doPut( resource );
        return resource;
    }

    @Override
    public IBaseResource doPost(IBaseResource bodyResource, Map<String, String> queryMap) {
        return null;
    }

    @Override
    public MethodOutcome doPost(IBaseResource iBaseResource) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public String doPost(String requestBody) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public FhirContext getCtx() {
        return this.ourCtx;
    }

    @Override
    public IBaseResource doDelete(String resourceType, Map<String, String> queryMap) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public IBaseResource doDelete(String s, String s1, Map<String, String> queryMap) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public IBaseResource doDelete(String s, String s1, String s2, Map<String, String> queryMap) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public String getServerUrl()  {
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public Bundle doGetCannonical(ResourceType resourceType, CanonicalType canonical) throws FhirProxyNotImplementedException {
        Bundle bundle = new Bundle();
        bundle.setType( Bundle.BundleType.SEARCHSET );

        bundle.addEntry( new Bundle.BundleEntryComponent()
                .setResource( getResourceUrlMap( resourceType ).get( canonical ) )
        );
        return bundle;
    }
}
