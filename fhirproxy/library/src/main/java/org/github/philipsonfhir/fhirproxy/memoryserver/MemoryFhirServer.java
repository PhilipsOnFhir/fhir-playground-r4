package org.github.philipsonfhir.fhirproxy.memoryserver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedError;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.common.util.ReferenceUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemoryFhirServer implements IFhirServer {
    private Map<String, Map<String, Resource>> resourceIdMap = new HashMap<>();
    private Map<String, Map<String, Resource>> resourceUrlMap = new HashMap<String, Map<String, Resource>>();
    private FhirContext ourCtx = FhirContext.forR4();
    private static final String urlField = "url";
    private SearchParameters searchParameters = new SearchParameters();
    private Logger logger = Logger.getLogger( this.getClass().getName() );

    public MemoryFhirServer(){
        logger.info("MemoryFhirServer create");
        searchParameters.getSearchParam().stream().forEach( searchParameter -> {
            try {
                Level level = logger.getLevel();
                logger.setLevel( Level.OFF );
                doPut(searchParameter);
                logger.setLevel(level);

            } catch (FhirProxyException e) {
            }
        });
    }

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
        logger.info("MemoryFhirServer get CapabilityStatement");
        CapabilityStatement capabilityStatement = new CapabilityStatement();
        capabilityStatement
            .setFhirVersion( Enumerations.FHIRVersion._4_0_1 )
            .setStatus(Enumerations.PublicationStatus.ACTIVE)
            .addFormat( "xml")
            .addFormat( "json")

        ;
        CapabilityStatement.CapabilityStatementRestComponent restServer = new CapabilityStatement.CapabilityStatementRestComponent()
                .setMode(CapabilityStatement.RestfulCapabilityMode.SERVER);
        for( ResourceType resourceType: ResourceType.values() ){
            CapabilityStatement.CapabilityStatementRestResourceComponent resourceDef =
                    new CapabilityStatement.CapabilityStatementRestResourceComponent()
                    .setType(resourceType.name())
                    .addInteraction(new CapabilityStatement.ResourceInteractionComponent()
                            .setCode(CapabilityStatement.TypeRestfulInteraction.READ)
                    )
                    .addInteraction(new CapabilityStatement.ResourceInteractionComponent()
                            .setCode(CapabilityStatement.TypeRestfulInteraction.CREATE)
                    )
                    .addInteraction(new CapabilityStatement.ResourceInteractionComponent()
                            .setCode(CapabilityStatement.TypeRestfulInteraction.UPDATE)
                    )
                    .setVersioning(CapabilityStatement.ResourceVersionPolicy.NOVERSION)
                    .setReadHistory(false)
                    .setUpdateCreate(false)
                    .setConditionalCreate(false)
                    .setConditionalRead(CapabilityStatement.ConditionalReadStatus.NOTSUPPORTED)
                    .setConditionalUpdate(false)
                    .setConditionalDelete(CapabilityStatement.ConditionalDeleteStatus.NOTSUPPORTED);;
            for (SearchParameter searchParameter : searchParameters.getSearchParam(resourceType)) {
                resourceDef.addSearchParam()
                    .setName( searchParameter.getName() )
                    .setDefinition( searchParameter.getUrl() )
                ;
            }
            restServer.addResource( resourceDef );
        }
        capabilityStatement.addRest( restServer );
        return  capabilityStatement;
    }

    @Override
    public IBaseResource doGet(ResourceType resourceType, Map<String, String> queryParams) {
        logger.info("MemoryFhirServer get "+resourceType+" - "+queryParams);
        return doGet( resourceType.name(), queryParams );
    }

    @Override
    public IBaseResource doGet(String resourceType, Map<String, String> queryParams) {
        logger.info("MemoryFhirServer get "+resourceType+" - "+queryParams);
        Bundle bundle = (Bundle) doGet(resourceType);
        List<Resource> match = new ArrayList<>();
        Iterator<Bundle.BundleEntryComponent> it = bundle.getEntry().iterator();
        while ( it.hasNext() ){
            Bundle.BundleEntryComponent entry = it.next();
            Resource resource = entry.getResource();
            if ( !searchParameters.checkResource( resource, queryParams )){
                it.remove();
            }
        }

        bundle.setTotal( bundle.getEntry().size() );
        return bundle;
    }

    @Override
    public IBaseResource doGet(ResourceType resourceType, String resourceId, Map<String, String> queryParams) throws ResourceNotFoundException {
        logger.info("MemoryFhirServer get "+resourceType+" - "+resourceId+" - "+queryParams);
        Map<String, Resource> map = getResourceIdMap(resourceType);
        Resource result = map.get(resourceId);
        if ( result==null ){
            throw new ResourceNotFoundException( "failed");
        }
        return result;
    }

    @Override
    public IBaseResource doGet(String resourceType, String resourceId, Map<String, String> queryParams) {
        logger.info("MemoryFhirServer get "+resourceType+" - "+resourceId+" - "+queryParams);
        Map<String, Resource> map = getResourceIdMap( ResourceType.fromCode(resourceType));
        return map.get( resourceId  );
    }

    @Override
    public IBaseResource doGet(String resourceType, String resourceId, String operationName, Map<String, String> queryParams) {
        logger.info("MemoryFhirServer get "+resourceType+" - "+resourceId+" - "+operationName+"- "+queryParams);
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doGet(String resourceName ) {
        logger.info("MemoryFhirServer get "+resourceName);
        Bundle bundle = new Bundle();
        bundle.setTimestamp(DateTimeType.now().getValue());
        bundle.setType( Bundle.BundleType.SEARCHSET );

        getResourceIdMap( ResourceType.fromCode(resourceName) )
            .values()
            .forEach( resource -> bundle.addEntry( new Bundle.BundleEntryComponent().setResource( resource )));

        bundle.setTotal( bundle.getEntry().size() );
        return bundle;
    }

    @Override
    public IBaseResource doGet(Reference subject) {
        logger.info("MemoryFhirServer get "+subject);
        ReferenceUtil.ParsedReference pr = ReferenceUtil.parseReference(subject);
        return doGet( pr.getResourceType(), pr.getResourceId(), new HashMap<>() );
    }

    @Override
    public Bundle loadPage(Bundle resultBundle) throws FhirProxyNotImplementedException {
        throw new FhirProxyNotImplementedException();
    }

    @Override
    public Bundle doGetCannonical(ResourceType resourceType, CanonicalType canonical) throws FhirProxyNotImplementedException {
        logger.info(String.format( "MemoryFhirServer get %s cannonical %s.",resourceType,canonical));
        Bundle bundle = new Bundle();
        bundle.setType( Bundle.BundleType.SEARCHSET );

        bundle.addEntry( new Bundle.BundleEntryComponent()
                .setResource( getResourceUrlMap( resourceType ).get( canonical ) )
        );
        return bundle;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public IBaseOperationOutcome doPut(Resource resource) throws FhirProxyException {
        logger.info(String.format( "MemoryFhirServer put %s.",resource.getId()));
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
    public IBaseResource doPost(String resourceType, String resourceId, String operationName, IBaseResource parseResource, Map<String, String> queryParams) throws FHIRException {
        logger.info("MemoryFhirServer post ");
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doPost(String resourceType, String resourceId, IBaseResource body, Map<String, String> queryParams) throws FHIRException {
        logger.info("MemoryFhirServer post ");
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public IBaseResource doPost(String resourceType, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException {
        logger.info("MemoryFhirServer post ");
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
        logger.info("MemoryFhirServer post ");
        throw new FhirProxyNotImplementedError();
    }

    @Override
    public MethodOutcome doPost(IBaseResource iBaseResource) throws FhirProxyException {
        logger.info("MemoryFhirServer post ");
        iBaseResource.setId( UUID.randomUUID().toString() );
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setCreated( true );
        methodOutcome.setId( iBaseResource.getIdElement() );

        if ( iBaseResource instanceof Resource ){
            Resource resource = (Resource)iBaseResource;
            getResourceIdMap( resource.getResourceType() ).put( resource.getId(), resource );
        } else {
            throw new MemoryServerException( "Post only supports resources, not "+ iBaseResource );
        }

        return methodOutcome;
    }

    @Override
    public String doPost(String requestBody) throws FhirProxyNotImplementedException {
        logger.info("MemoryFhirServer post ");
        throw new FhirProxyNotImplementedException();
    }

    // -----------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------
    @Override
    public FhirContext getCtx() {
        return this.ourCtx;
    }

    @Override
    public String getServerUrl()  {
        throw new FhirProxyNotImplementedError();
    }

}
