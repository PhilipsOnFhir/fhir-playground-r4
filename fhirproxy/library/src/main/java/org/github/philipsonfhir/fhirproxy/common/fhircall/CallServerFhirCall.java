//package org.github.philipsonfhir.fhirproxy.common.fhircall;
//
//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.rest.client.api.IGenericClient;
//import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
//import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
//import org.hl7.fhir.instance.model.api.IBaseBundle;
//import org.hl7.fhir.instance.model.api.IBaseResource;
//import org.hl7.fhir.r4.model.CapabilityStatement;
//import org.hl7.fhir.r4.model.Parameters;
//import org.hl7.fhir.r4.model.Resource;
//import org.hl7.fhir.r4.model.StringType;
//import org.springframework.http.HttpStatus;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Logger;
//
//public class CallServerFhirCall implements FhirCall {
//    private final String fhirServerUrl;
//    private final FhirRequest fhirRequest;
//    private final FhirServer fhirServer;
//    private IBaseResource result;
//    private Logger logger = Logger.getLogger( this.getClass().getName());
//    private boolean done=false;
//
//
//    public CallServerFhirCall(String fhirServerUrl, FhirRequest fhirRequest) {
//        this.fhirServerUrl = fhirServerUrl;
//        this.fhirRequest = fhirRequest;
////        client = fhirContext.newRestfulGenericClient(fhirServerUrl);
//        this.fhirServer = new FhirServer(fhirServerUrl);
//    }
//
//    @Override
//    public void execute() throws FhirProxyException {
//        List<String> partList = fhirRequest.getPartList();
//        Map<String, String> queryMap = fhirRequest.getQueryMap();
//        switch ( fhirRequest.getMethod() ){
//            case "GET":
//                if ( partList.size()==1 ) {
//                    result = fhirServer.doSearch( fhirRequest.getResourceType(), fhirRequest.getQueryMap() );
//                } else if ( partList.size()==2) {
//                    result = fhirServer.doGet(partList.get(0), partList.get(1), fhirRequest.getQueryMap());
//                } else if ( partList.size()==3 ) {
//                    result = fhirServer.doGet(partList.get(0), partList.get(1), partList.get(3), fhirRequest.getQueryMap());
//                } else {
//                    throw new FhirProxyException("Unexpected value: " + fhirRequest.getPartList());
//                }
//                break;
//            case "PUT":
//                fhirServer.doPut( fhirRequest.getBodyResource() )
//                break;
//            case "POST":
//                result = fhirServer.doPost( this.fhirRequest.getBodyResource() );
//                break;
//            case "DELETE":
//                result = fhirServer.doPost( this.fhirRequest.getBodyResource() );
//                break;
//            default:
//                throw new FhirProxyException("Unknown http method "+ fhirRequest.getMethod() );
//        }
//
//        done = true;
//    }
//
//    @Override
//    public String getStatusDescription() {
//        return (done?"processing":"done");
//    }
//
//    @Override
//    public HttpServletResponse getResponse() {
//        new FhirProxyException("Response not available for this call." );
//    }
//
//    @Override
//    public IBaseResource getResource() {
//        return (done?result:null);
//    }
//
//    @Override
//    public FhirServer getFhirServer() {
//        return fhirServer;
//    }
//
//}
