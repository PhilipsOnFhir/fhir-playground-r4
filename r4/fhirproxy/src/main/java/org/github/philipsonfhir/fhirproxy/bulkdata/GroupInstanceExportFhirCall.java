package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.github.philipsonfhir.fhirproxy.async.service.BundleRetriever;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GroupInstanceExportFhirCall implements FhirCall {
    private final InstantType since;
    private final String groupId;
    private Logger logger = LoggerFactory.getLogger( this.getClass() );
    private final FhirServer fhirServer;
    private final String type;
    private String outputFormat = null;

    private Bundle resultBundle = null;
    private String progressDescription = "undefined";
    private List< OperationOutcome> errors = new ArrayList<>();
    private Map<String,OperationOutcome> errorMap = new HashMap<>();

    public GroupInstanceExportFhirCall(FhirServer fhirServer, String groupId, String outputFormat, InstantType since, String type) {
        this.fhirServer = fhirServer;
        this.outputFormat = outputFormat;
        this.type = type;
        this.since = since;
        this.groupId = groupId;
    }

    @Override
    public void execute()  {
        this.progressDescription = "retrieve Group";
        Group group = (Group) this.fhirServer.doGet( "Group", groupId, null );

        Bundle bundle = new Bundle()
                .setType( Bundle.BundleType.SEARCHSET );

        this.progressDescription = "retrieve Group data";
        group.getMember().stream().forEach( groupMemberComponent -> {
            Reference reference = groupMemberComponent.getEntity();
            IdType idType = new IdType( reference.getReference() );
            if ( idType.getResourceType().equals( "Patient" ) ) {
                try {
                    this.progressDescription = "retrieve data for patient "+idType.getIdPart();
                    Bundle initialPatientBundle = (Bundle) this.fhirServer.doGet( "Patient", idType.getIdPart(),"$everything", null);

                    this.progressDescription = "retrieve data for patient "+idType.getIdPart()+" from sub bundles";
                    BundleRetriever bulkDataHelper = new BundleRetriever( this.fhirServer, initialPatientBundle );
                    List<Resource> patientBundle = bulkDataHelper.retrieveAllResources();

                    this.progressDescription = "adding results for patient "+idType.getIdPart();
                    Bundle groupResultBundle = BulkdataResult.getResultBundle(patientBundle,type,since);

                    groupResultBundle.getEntry().stream()
                            .forEach( bundleEntryComponent -> bundle.addEntry( bundleEntryComponent ) );
                } catch ( FHIRException e ) {
                    this.errors.add( new OperationOutcome()
                            .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                                    .setSeverity( OperationOutcome.IssueSeverity.ERROR)
                                    .setDiagnostics(e.getMessage())
                            )
                    );
                }
            }
        } );

        bundle.setTotal(bundle.getEntry().size());
        this.resultBundle = bundle;

        this.progressDescription = "done";
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
        return this.getFhirServer();
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return Collections.unmodifiableMap(this.errorMap);
    }

}
