package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;

import java.util.HashMap;
import java.util.Map;

public class CannonicalSearchFhirCall implements FhirCall {

//    private final FhirRequest fhirRequest;
    private final IFhirServer fhirServer;
    private final String[] typeAr;
    private Map<String, String> qp = new HashMap<>();
    private String description = "";
    private IBaseResource result = null;

    public CannonicalSearchFhirCall(IFhirServer fhirServer, FhirRequest fhirRequest) {
        this.fhirServer = fhirServer;

        String types = fhirRequest.getQueryMap().get("_type");
        log(fhirRequest.toString());
        typeAr = types.split(",");
        qp = fhirRequest.getQueryMap();
        qp.remove("_type");
    }

    public CannonicalSearchFhirCall(IFhirServer fhirServer, String cannonical, String[] types) {
        this.fhirServer = fhirServer;
        this.typeAr = types;
        qp.put("url",cannonical);
    }

    @Override
    public void execute() throws FhirProxyException {
        Bundle resultBundle = new Bundle()
                .setType(Bundle.BundleType.SEARCHSET);

//        String types = this.fhirRequest.getQueryMap().get("_type");
//        log(fhirRequest.toString());
//        String[] typeAr = types.split(",");
//        Map<String, String> qp = fhirRequest.getQueryMap();
//        qp.remove("_type");
//        Bundle resultBundle = new Bundle()
//                .setType(Bundle.BundleType.SEARCHSET);
        for ( String resourceType: typeAr ){
            Bundle tb = (Bundle) fhirServer.doGet(resourceType,qp);
            tb.getEntry().forEach( entry -> resultBundle.addEntry( entry ));
        }
        result = resultBundle;
    }

    private void log( String message ){
        System.out.println(message);
        this.description = message;
    }

    @Override
    public String getStatusDescription() {
        return description;
    }

    @Override
    public IBaseResource getResource() throws FhirProxyException {
        return result;
    }

    @Override
    public IFhirServer getFhirServer() {
        return null;
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return null;
    }
}
