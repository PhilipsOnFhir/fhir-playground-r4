package org.github.philipsonfhir.smartonfhir.basic.controller.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class FhirServer {
    private final FhirContext ourCtx;
    private final IGenericClient ourClient;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final String fhirUrl;
//    private FhirOperationRepository fhirOperationRepository = new FhirOperationRepository();

    public FhirServer( @Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        this.fhirUrl = fhirServerUrl;
        ourCtx = FhirContext.forR4();
        // Set how long to try and establish the initial TCP connection (in ms)
        ourCtx.getRestfulClientFactory().setConnectTimeout(20 * 1000);

        // Set how long to block for individual read/write operations (in ms)
        ourCtx.getRestfulClientFactory().setSocketTimeout(120 * 1000);
        ourClient = ourCtx.newRestfulGenericClient(fhirUrl );
    }

    public CapabilityStatement getCapabilityStatement() {
        CapabilityStatement capabilityStatement =
                ourClient.capabilities().ofType(CapabilityStatement.class).execute();
//        capabilityStatement.getRest().get(0).getOperation().addAll( this.getFhirOperationRepository().getCapabilityOperations() );
        return capabilityStatement;
    }

//    public FhirOperationRepository getFhirOperationRepository(){return fhirOperationRepository;};

    public IBaseResource doSearch(String resourceType, Map<String, String> queryParams) {
        String url = getUrl( resourceType, null, null, queryParams );
        logger.info( "GET " + fhirUrl + url );

        return ourClient.search()
                .byUrl( url )
                .execute();
    }

    public IBaseResource doGet(String resourceType, String resourceId, Map<String, String> queryParams) throws FHIRException {
        String url = getUrl( resourceType, resourceId, null, queryParams );
        logger.info( "GET " + fhirUrl + url );

//        FhirOperationCall operation =
//                fhirOperationRepository.doGetOperation( this, resourceType, resourceId, queryParams );
//
//        if ( operation!=null ){
//            return  operation.getResult();
//        }

        IBaseResource iBaseResource = null;
        if ( resourceId.startsWith("$") ){
            try {
                Class resourceClass = Class.forName( "org.hl7.fhir.r4.model." + resourceType );
                // TODO parameters
                Parameters parameters = getParameters(queryParams);
                // operation on resource and not a retrieval
                IBaseResource result = ourClient.operation()
                        .onType( resourceClass )
                        .named(resourceId)
                        .withParameters( parameters )
                        .useHttpGet()
                        .execute();
                iBaseResource = ((Parameters) result).getParameterFirstRep().getResource();
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
                throw new FHIRException( "Unknown resource type " + resourceType );
            }
//            return getResourceOperation( resourceType, resourceId, queryParams );
        } else {
            if ( queryParams != null && queryParams.size() != 0 ) {
                logger.severe( "queryParams on get resourcetype/id is not supported" );
            }

            iBaseResource = ourClient
                .read()
                .resource( resourceType )
                .withId( resourceId )
                .execute();
        }
        return iBaseResource;
    }

    private Parameters getParameters(Map<String, String> queryParams) {
        Parameters parameters = new Parameters();
        if ( queryParams != null ) {
            queryParams.forEach((key, value) -> parameters.addParameter(new Parameters.ParametersParameterComponent()
                    .setName(key)
                    .setValue(new StringType(value))
            ));
//            parameters = getParameters(queryParams);
        }
        return parameters;
    }

    public IBaseResource doGet(String resourceType, String resourceId, String operationName, Map<String, String> queryParams) throws FHIRException {
        String url = getUrl( resourceType, resourceId, operationName, queryParams );
        logger.info( "GET " + fhirUrl + url );

//        FhirOperationCall operation =
//            fhirOperationRepository.doGetOperation( this, resourceType, resourceId, operationName, queryParams );
//
//        if ( operation!=null ){
//            return  operation.getResult();
//        }
        // operation not found, call sever
        Parameters parameters = getParameters(queryParams);

        Parameters outParams = ourClient
            .operation()
            .onInstance( new IdDt( resourceType, resourceId ) )
            .named( operationName )
            .withParameters( parameters )
            .useHttpGet()
            .execute();

        List<Parameters.ParametersParameterComponent> response = outParams.getParameter();
        return response.get(0).getResource();
    }

    public Bundle loadPage(Bundle resultBundle) {
        return  ourClient.loadPage().next( resultBundle ).execute();
    }

    private String getUrl(String resourceType, String id, String params, Map<String, String> queryParams) {
        StringBuilder url = new StringBuilder(resourceType);
        url.append(id != null ? "/" + id : "");
        url.append(params != null ? "/" + params : "");

        if ( queryParams!=null ) {
            Iterator<Map.Entry<String, String>> iterator = queryParams.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                url.append("?").append(entry.getKey()).append("=").append(entry.getValue());
            }
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return url.toString();
    }

    public IBaseOperationOutcome updateResource(IBaseResource iBaseResource) {
        return ourClient.update().resource(iBaseResource).execute().getOperationOutcome();
    }

    public IBaseResource doPost(String resourceType, String resourceId, IBaseResource body, Map<String, String> queryParams)
            throws FHIRException {
        String url = getUrl( resourceType, resourceId, null, queryParams );
        logger.info( "POST " + fhirUrl + "/"+ url );

        if ( resourceId.startsWith("$") ){
            String operationName = resourceId;
//            FhirOperationCall operation =
//                    fhirOperationRepository.doPostOperation( this, resourceType, operationName, body, queryParams );
//
//            if ( operation!=null ){
//                return  operation.getResult();
//            }

            // operation on resource and not a retrieval
            Class resourceClass = null;
            try {
                resourceClass = Class.forName( "org.hl7.fhir.dstu3.model." + resourceType );
            } catch (ClassNotFoundException e) {
                throw new FHIRException(e);
            }

            IBaseResource result = ourClient.operation()
                    .onType( resourceClass )
                    .named( operationName )
                    .withParameters( (Parameters)body )
                    .execute();
            result =((Parameters) result).getParameterFirstRep().getResource();
            return result;
        } else {
            MethodOutcome methodOutcome = ourClient.create().resource(body).execute();

            return (methodOutcome.getResource()!=null? methodOutcome.getResource():methodOutcome.getOperationOutcome());
        }
    }

    public IBaseResource doPost(
            String resourceType,
            String resourceId,
            IBaseResource parseResource,
            String operationName,
            Map<String, String> queryParams
    ) throws FHIRException {
        String url = getUrl( resourceType, operationName, operationName, queryParams );
        logger.info( "POST " + fhirUrl + "/"+ url );
        // id given so operation

//        FhirOperationCall operation =
//            fhirOperationRepository.doPostOperation( this, resourceType, resourceId, operationName, parseResource, queryParams );
//
//        if ( operation!=null ){
//            return  operation.getResult();
//        }

        Parameters outParams = ourClient
            .operation()
            .onInstance( new IdDt( resourceType, resourceId ) )
            .named( operationName )
            .withParameters( (Parameters)parseResource )
            .execute();

        List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

        return response.get( 0 ).getResource();
    }

    public MethodOutcome doPost(String resourceType, IBaseResource iBaseResource, Map<String, String> queryParams) {
        return this.ourClient.create().resource(iBaseResource).execute();
    }

    public String doPost(String requestBody) {
        return this.ourClient.transaction().withBundle(requestBody).execute();
    }

    public FhirContext getCtx() {
        return ourCtx;
    }
}
