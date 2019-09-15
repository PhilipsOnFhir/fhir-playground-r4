package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.bulkdata.BulkDataImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.ImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperationRepository;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FhirCallFactory {
    private final String base;
    private final String fhirServerUrl;
    private final FhirServer fhirServer;
    private final FhirOperationRepository fhirOperationRepository;
    private List<ImplementationGuide> implementationGuides = new ArrayList<>();

    public FhirCallFactory(String base, String fhirServerUrl) {
        this.base = base;
        this.fhirServerUrl = fhirServerUrl;
        this.fhirServer = new FhirServer(fhirServerUrl);
        this.fhirOperationRepository = new FhirOperationRepository();
    }


    public FhirCall createGetFhirCall(HttpServletRequest request, HttpServletResponse response) throws FhirProxyException, MalformedURLException {


//        String subPath = url.getPath().substring(baseUrl.getPath().length());

        FhirRequest fhirRequest = new FhirRequest( base, request, response );

        FhirCall fhirCall;
        if ( fhirRequest.isMetadataCall()) {
            fhirCall = new CapabilityStatementRequestCall(fhirServer, request, response, implementationGuides );
        } else
            if ( fhirRequest.isOperationCall() ){
                fhirCall = this.fhirOperationRepository.getFhirOperation( fhirServer, fhirRequest );
                if ( fhirCall==null ){
                    fhirCall = new GetFhirCall( fhirServer, fhirRequest );
                }
            } else {
                fhirCall = new GetFhirCall( fhirServer, fhirRequest );
            }
        return fhirCall;


    }


//    public FhirCall createNonForwardFhirCall(HttpServletRequest request, HttpServletResponse response) throws FhirProxyException {
//        FhirRequest fhirRequest = new FhirRequest( base, request, response );
//
//        FhirCall fhirCall;
//        if ( fhirRequest.isMetadataCall()) {
//            fhirCall = new CapabilityStatementRequestCall(fhirServerUrl, request, response );
//        } else
//        if ( fhirRequest.isOperationCall() && this.fhirOperationRepository.holdsOperation(fhirRequest) ){
//            //TODO
//            fhirCall = null;
////            fhirCall = new OperationFhirCall( this.fhirOperationRepository.getOperation(fhirRequest));
//        } else {
//            fhirCall = null; //new CallServerFhirCall( fhirServerUrl, fhirRequest );
//        }
//        return fhirCall;
//    }

    public FhirCall createPutFhirCall(HttpServletRequest req, HttpServletResponse resp) {
return null;
    }

    public FhirCall createPostFhirCall(HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }

    public void addImplementationGuide(BulkDataImplementationGuide bulkDataImplementationGuide) {
        this.implementationGuides.add( bulkDataImplementationGuide );
        fhirOperationRepository.registerOperation(bulkDataImplementationGuide.getOperations());
    }
}
