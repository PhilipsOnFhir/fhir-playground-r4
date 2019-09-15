package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.github.philipsonfhir.fhirproxy.async.service.BundleRetriever;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PatientExportFhirCall implements FhirCall {
    private Logger logger = LoggerFactory.getLogger( this.getClass() );
    private final FhirServer fhirServer;
    private final String type;
    private String outputFormat = null;

    private Bundle resultBundle = null;
    private String progressDescription = "undefined";
    private List< OperationOutcome> errors = new ArrayList<>();
    private Map<String,OperationOutcome> errorMap = new HashMap<>();
    private InstantType since;

    public PatientExportFhirCall(FhirServer fhirServer, String outputFormat, InstantType since, String type) {
        this.fhirServer = fhirServer;
        this.outputFormat = outputFormat;
        this.type = type;
        this.since=since;
    }

    @Override
    public void execute()  {
        this.progressDescription = "retrieve Patient data";
        Bundle bundle = (Bundle) this.fhirServer.doGet("Patient","$everything",null);
        this.progressDescription = "retrieve Patient data from sub bundles";
        BundleRetriever bulkDataHelper = new BundleRetriever( this.fhirServer, bundle );
        List<Resource> resources = bulkDataHelper.retrieveAllResources();

        this.progressDescription = "create result";

        resultBundle = BulkdataResult.getResultBundle(resources,type,since);
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
        return this.fhirServer;
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return Collections.unmodifiableMap(this.errorMap);
    }

}
