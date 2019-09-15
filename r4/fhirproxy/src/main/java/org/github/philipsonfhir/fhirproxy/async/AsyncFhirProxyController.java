//package org.github.philipsonfhir.fhirproxy.async;
//
//import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
//import org.github.philipsonfhir.fhirproxy.async.model.AsyncService;
//import org.github.philipsonfhir.fhirproxy.controller.FhirProxyController;
//import org.hl7.fhir.exceptions.FHIRException;
//import org.hl7.fhir.instance.model.api.IBaseResource;
//import org.hl7.fhir.r4.model.OperationOutcome;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import static org.apache.http.HttpHeaders.CONTENT_LOCATION;
//
//@Controller
//@RestController
//@CrossOrigin(origins = "*")
//public class AsyncFhirProxyController  extends FhirProxyController {
//    private final AsyncService asyncService;
//
////    static final String base="fhirservlet";
////    private final FhirContext ourCtx;
////    private final IGenericClient ourClient;
//
//    private Logger logger =
//            Logger.getLogger(this.getClass().getName());
//
//
//    private String fhirServerUrl;
//
//    @Autowired
//    AsyncFhirProxyController(@Value("${proxy.fhirserver.url}") String fhirServerUrl, AsyncService asyncService ){
//        super( fhirServerUrl );
//        this.asyncService = asyncService;
//    }
//
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}"
//    )
//    public ResponseEntity<String> searchResources(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @RequestHeader(value = "Prefer", defaultValue = "") String prefer,
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @RequestParam Map<String, String> queryParams
//    ) {
//        if ( prefer!=null && prefer.equals("respond-async") ){
//            String callUrl = request.getRequestURL().toString();
//            logger.info( "Ansync request detected" );
//            String  sessionId = asyncService.newAyncGetSession( request.getRequestURL().toString(), fhirServer, resourceType, null, null, queryParams );
//            String sessionUrl = callUrl.substring( 0, callUrl.indexOf( "fhir/" ) ) + "async-services/" + sessionId;
//
//            response.setStatus( 202 );
//            response.setHeader( CONTENT_LOCATION, sessionUrl );
//            OperationOutcome operationOutcome = new OperationOutcome();
//            operationOutcome.addIssue()
//                    .setSeverity( OperationOutcome.IssueSeverity.INFORMATION )
//                    .setCode( OperationOutcome.IssueType.VALUE )
//                    .addLocation( sessionUrl )
//            ;
//            return new ResponseEntity<>(
//                    parser( accept ).encodeResourceToString( operationOutcome ),
//                    HttpStatus.ACCEPTED
//            );
//        } else {
//            return super.searchResources( accept,resourceType,queryParams );
//        }
//    }
//
//    @RequestMapping (
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}/{id}"
//    )
//    public ResponseEntity<String> getResource(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @RequestHeader(value = "Prefer", defaultValue = "") String prefer,
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        if ( prefer!=null && prefer.equals("respond-async") ){
//            logger.info("Async call");
//
//            String sessionId = asyncService.newAyncGetSession( request.getRequestURL().toString(), fhirServer, resourceType, id, null, queryParams );
//            String callUrl = request.getRequestURL().toString();
//
//            logger.info("Ansync request detected");
//            String sessionUrl = callUrl.substring(0, callUrl.indexOf("fhir/"))+"async-services/"+sessionId;
//
//            response.setStatus(202);
//            response.setHeader( CONTENT_LOCATION, sessionUrl );
//            OperationOutcome operationOutcome = new OperationOutcome();
//            operationOutcome.addIssue()
//                    .setSeverity( OperationOutcome.IssueSeverity.INFORMATION)
//                    .setCode(OperationOutcome.IssueType.VALUE)
//                    .addLocation(sessionUrl)
//            ;
//
//            return new ResponseEntity<>(
//                    parser( accept ).encodeResourceToString(
//                            operationOutcome ),
//                    HttpStatus.ACCEPTED
//            );
//        } else {
//            return new ResponseEntity<>(
//                    parser(accept).encodeResourceToString(fhirServer.doGet(resourceType, id, queryParams)),
//                    HttpStatus.OK
//            );
//        }
//    }
//
//
//
//    @RequestMapping (
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}/{id}/{params}"
//    )
//    public ResponseEntity<String> getResourceWithParams(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @RequestHeader(value = "Prefer", defaultValue = "") String prefer,
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @PathVariable String params,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        if ( prefer!=null && prefer.equals("respond-async") ){
//            logger.info("Async call");
//
////            String sessionId = asyncService.newAyncGetSession( request.getRequestURL().toString(), fhirServer, resourceType, id, params, queryParams );
//            String callUrl = request.getRequestURL().toString();
//
//            logger.info("Ansync request detected");
//            String sessionUrl = callUrl.substring(0, callUrl.indexOf("fhir/"))+"async-services/"+sessionId;
//
//            response.setStatus(202);
//            response.setHeader( CONTENT_LOCATION, sessionUrl );
//            OperationOutcome operationOutcome = new OperationOutcome();
//            operationOutcome.addIssue()
//                    .setSeverity( OperationOutcome.IssueSeverity.INFORMATION)
//                    .setCode(OperationOutcome.IssueType.VALUE)
//                    .addLocation(sessionUrl)
//            ;
//
//            return new ResponseEntity<>(
//                    parser( accept ).encodeResourceToString(
//                            operationOutcome ),
//                    HttpStatus.ACCEPTED
//            );
//        } else {
//            return new ResponseEntity<>(
//                    parser(accept).encodeResourceToString(fhirServer.doGet(resourceType, id, queryParams)),
//                    HttpStatus.OK
//            );
//        }
//    }
//
//
//}
