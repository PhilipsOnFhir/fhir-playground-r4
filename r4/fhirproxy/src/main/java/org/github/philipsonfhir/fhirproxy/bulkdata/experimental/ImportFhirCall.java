package org.github.philipsonfhir.fhirproxy.bulkdata.experimental;

import ca.uhn.fhir.context.FhirContext;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

// https://github.com/smart-on-fhir/bulk-import/blob/master/import.md
public class ImportFhirCall implements FhirCall {
    private final ImportData importData;
    private static final FhirContext ourCtx = FhirContext.forR4();
    private final FhirServer fhirServer;

    public ImportFhirCall(FhirServer fhirServer, ImportData importData) {
        this.importData = importData;
        this.fhirServer = fhirServer;
    }

    @Override
    public void execute()  {
        this.importData.getInput().forEach( input -> {
            try {
                retrieveAndStoreResources( input.getUrl() );
            } catch (FhirProxyException e) {
                e.printStackTrace();
            }
        });
    }

    private void retrieveAndStoreResources(String urlStr) throws FhirProxyException {
        try {
            URL url = new URL( urlStr );
            Scanner scanner = new Scanner( url.openStream() );
            while( scanner.hasNextLine() ){
                String json = scanner.nextLine();
                // TODO what if failed due to missing references?
                IBaseResource res = ourCtx.newJsonParser().parseResource(json);
                fhirServer.doPut(res);
            }
        } catch (Exception e) {
            throw new FhirProxyException("something went wromg");
        }

    }

    @Override
    public String getStatusDescription() {
        return null;
    }

    @Override
    public IBaseResource getResource() throws FhirProxyException {
        return null;
    }

    @Override
    public FhirServer getFhirServer() {
        return this.fhirServer;
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return Collections.emptyMap();
    }
}
