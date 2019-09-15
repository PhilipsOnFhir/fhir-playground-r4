package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.github.philipsonfhir.fhirproxy.async.service.BundleRetriever;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ExportFhirCall implements FhirCall {
    private final InstantType since;
    private Logger logger = LoggerFactory.getLogger( this.getClass() );
    private final FhirServer fhirServer;
    private final String type;
    private String outputFormat = null;

    private Bundle resultBundle = null;
    private String progressDescription = "undefined";
    private Status status = Status.UNKNOWN;
    private List< OperationOutcome> errors = new ArrayList<>();
    private Map<String,OperationOutcome> errorMap = new HashMap<>();

    public ExportFhirCall(FhirServer fhirServer, String outputFormat, InstantType since, String type) {
        this.fhirServer = fhirServer;
        this.outputFormat = outputFormat;
        this.type = type;
        this.since = since;
    }

    @Override
    public void execute()  {
       this.status = Status.PROCESSING;

        Set<String> allresources = new TreeSet<>();
        HashMap<String, IBaseResource> resultHashMap = new HashMap<>();
        Map<String, OperationOutcome> errors = new HashMap<>();

        this.progressDescription = "Retrieving resource list";
        fhirServer.getCapabilityStatement().getRest().stream()
            .forEach( capabilityStatementRestComponent -> capabilityStatementRestComponent.getResource().stream()
                .map( capabilityStatementRestResourceComponent -> capabilityStatementRestResourceComponent.getType() )
                .filter( rType -> ( type == null || type.contains( rType )) )
                .forEach( rType -> allresources.add( rType ) ) );

        this.progressDescription = "Processing resources";
        allresources.stream().forEach( resourceName -> {
            try {
                this.progressDescription = "Processing resource " + resourceName;
                IBaseResource result = fhirServer.doGet( resourceName, null );
                if ( result instanceof Bundle ) {
                    BundleRetriever bundleRetriever = new BundleRetriever( fhirServer, (Bundle) result );
                    bundleRetriever.retrieveAllResources().stream().forEach( resource ->
                        resultHashMap.put( resource.fhirType() + "/" + resource.getId(), resource ) );
                }
                resultHashMap.put( resourceName, result );
            } catch ( Exception e ) {
                logger.info( resourceName + " error" );
                OperationOutcome operationOutcome = new OperationOutcome()
                    .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                        .setSeverity( OperationOutcome.IssueSeverity.ERROR )
                        .setDiagnostics( e.getMessage() )
                        .addLocation( resourceName )
                    );
                this.errorMap.put( resourceName, operationOutcome );
            }
        } );

        this.progressDescription = "Creating result";

        Bundle completeBundle = new Bundle()
            .setType( Bundle.BundleType.SEARCHSET );

        resultHashMap.values().stream()
            .filter( iBaseResource -> iBaseResource instanceof Resource )
            .map( iBaseResource -> (Resource) iBaseResource )
            .forEach( resource -> completeBundle.addEntry(
                new Bundle.BundleEntryComponent().setResource( resource )
            ) );

        this.resultBundle = BulkdataResult.getResultBundle( completeBundle,type,since);
        this.resultBundle.setTotal( resultBundle.getEntry().size() );

        this.progressDescription = "finished";
        this.status = Status.DONE;
    }

   @Override
    public String getStatusDescription() {
        return this.progressDescription += ".";
    }

    @Override
    public IBaseResource getResource() throws FhirProxyException {
        if ( resultBundle == null ) {
            execute();
        }
        return resultBundle;
    }

    @Override
    public FhirServer getFhirServer() {
        return fhirServer;
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return Collections.unmodifiableMap(this.errorMap);
    }

    public enum Status {UNKNOWN, PROCESSING, DONE}


}
