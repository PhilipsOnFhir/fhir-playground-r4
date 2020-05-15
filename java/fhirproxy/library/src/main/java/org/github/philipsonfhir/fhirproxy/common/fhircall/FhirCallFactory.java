package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.ImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.FhirServer;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FhirCallFactory {
    private final String base;
    private String fhirServerUrl = null;
    private final IFhirServer fhirServer;
    private final FhirOperationRepository fhirOperationRepository;
    private List<ImplementationGuide> implementationGuides = new ArrayList<>();

    public FhirCallFactory(String base, IFhirServer fhirServer ) {
        this.base = base;
        this.fhirServer = fhirServer;
        this.fhirOperationRepository = new FhirOperationRepository();
    }

    public FhirCallFactory(String base, String fhirServerUrl) {
        this( base, new FhirServer( fhirServerUrl ) );
        this.fhirServerUrl = fhirServerUrl;
    }

    public FhirCall createGetFhirCall(HttpServletRequest request, HttpServletResponse response) throws FhirProxyException, MalformedURLException {


//        String subPath = url.getPath().substring(baseUrl.getPath().length());

        FhirRequest fhirRequest = new FhirRequest( base, request, response );

        FhirCall fhirCall;
        if ( fhirRequest.isMetadataCall()) {
            fhirCall = new CapabilityStatementRequestCall(fhirServer, request, response, implementationGuides );
        } else if ( fhirRequest.isOperationCall() ){
            fhirCall = this.fhirOperationRepository.getFhirOperation( fhirServer, fhirRequest );
            if ( fhirCall==null ){
                fhirCall = new GetFhirCall( fhirServer, fhirRequest );
            }
        } else if ( fhirRequest.getResourceId()==null && fhirRequest.getQueryMap().containsKey("_type")){
            // canonical search;
            return new CannonicalSearchFhirCall( fhirServer, fhirRequest );
        }else {
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

    public FhirCall createPutFhirCall(HttpServletRequest request, HttpServletResponse response) throws FhirProxyException {
        FhirRequest fhirRequest = new FhirRequest( base, request, response );
        PutFhirCall fhirCall = new PutFhirCall(fhirServer, fhirRequest);
        return fhirCall;
    }

    public FhirCall createPostFhirCall(HttpServletRequest request, HttpServletResponse response) throws FhirProxyException {
        FhirRequest fhirRequest = new FhirRequest( base, request, response );

        FhirCall fhirCall;
        if ( fhirRequest.isOperationCall() ){
            fhirCall = this.fhirOperationRepository.getFhirOperation( fhirServer, fhirRequest );
            if ( fhirCall==null ){
                fhirCall = new PostFhirCall( fhirServer, fhirRequest );
            }
        } else {
            fhirCall = new PostFhirCall( fhirServer, fhirRequest );
        }
        return fhirCall;
    }

    public FhirCall createDeleteFhirCall(HttpServletRequest req, HttpServletResponse resp) throws FhirProxyException {
        FhirRequest fhirRequest = new FhirRequest( base, req, resp );
        return new DeleteFhirCall( fhirServer, fhirRequest );
    }

    public void addImplementationGuide(ImplementationGuide implementationGuide) {
        this.implementationGuides.add( implementationGuide );
        fhirOperationRepository.registerOperation(implementationGuide.getOperations());
    }

}
