//package org.github.philipsonfhir.fhirproxy.controller;
//
//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.parser.IParser;
//import ca.uhn.fhir.rest.api.MethodOutcome;
//import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
//import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
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
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Controller
//@RestController
//@CrossOrigin(origins = "*")
//public class FhirProxyController {
//    private static final String template = "Hello, %s!";
//    protected final FhirServer fhirServer;
//    private Logger logger = Logger.getLogger(this.getClass().getName());
//    private FhirContext myContext = FhirContext.forR4();
//
//    @Autowired
//    public FhirProxyController( @Value("$(proxy.fhirserver.url)") String fhirUrl) {
//        this.fhirServer = new FhirServer( fhirUrl );
//    }
//
//    @RequestMapping (
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}"
//    )
//    public ResponseEntity<String> searchResources(
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @RequestParam Map<String, String> queryParams
//    ) {
//        IBaseResource iBaseResource;
//        HttpStatus httpStatus;
//        try{
//            if ( resourceType.equals("metadata")){
//                logger.log(Level.INFO,"JSON GET CapabilityStatement" );
//                iBaseResource = fhirServer.getCapabilityStatement();
//            }
//            else {
//                logger.log(Level.INFO,"JSON GET "+resourceType );
//                iBaseResource = fhirServer.doSearch(resourceType, queryParams );
//            }
//            httpStatus= HttpStatus.OK;
//
//        } catch ( FHIRException e1 ){
//            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
//                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
//                    .setDiagnostics( e1.getMessage() )
//            );
//            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        catch ( BaseServerResponseException se ){
//            iBaseResource = se.getOperationOutcome();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        ResponseEntity<String> responseEntity = new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
//        return responseEntity;
//    }
//
//    @RequestMapping (
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}/{id}"
//    )
//    public ResponseEntity<String> getResource(
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        IBaseResource iBaseResource;
//        HttpStatus httpStatus;
//        try{
//            iBaseResource = fhirServer.doGet(resourceType, id, queryParams );
//            httpStatus= HttpStatus.OK;
//
//        } catch ( FHIRException e1 ){
//            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
//                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
//                    .setDiagnostics( e1.getMessage() )
//            );
//            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        catch ( BaseServerResponseException se ){
//            iBaseResource = se.getOperationOutcome();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        ResponseEntity<String> responseEntity = new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
//        return responseEntity;
//    }
//
//
//
//    @RequestMapping (
//            method = RequestMethod.GET,
//            value = "/fhir/{resourceType}/{id}/{params}"
//    )
//    public ResponseEntity<String> getResourceWithParams(
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @PathVariable String params,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        IBaseResource iBaseResource;
//        HttpStatus httpStatus;
//        try{
//            iBaseResource = fhirServer.doGet( resourceType, id, params, queryParams );
//            httpStatus= HttpStatus.OK;
//        } catch ( FHIRException e1 ){
//            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
//                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
//                    .setDiagnostics( e1.getMessage() )
//            );
//            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        catch ( BaseServerResponseException se ){
//            iBaseResource = se.getOperationOutcome();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        ResponseEntity<String> responseEntity = new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
//        return responseEntity;
//    }
//
//    /////////////////////////////////////////////////////////////////////////////
//    @RequestMapping (
//            method = RequestMethod.PUT,
//            value = "/fhir/{resourceType}/{id}"
//    )
//    public ResponseEntity<String> putResourceJson(
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @RequestHeader("Content-Type") String contentType,
//            @RequestBody  String requestBody,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//
//        IBaseResource iBaseResource;
//        HttpStatus httpStatus;
//        try{
//            IBaseResource body = parser( contentType ).parseResource(requestBody);
//            iBaseResource = fhirServer.updateResource(body);
//            httpStatus= HttpStatus.OK;
//        } catch ( FHIRException e1 ){
//            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
//                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
//                    .setDiagnostics( e1.getMessage() )
//            );
//            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        catch ( BaseServerResponseException se ){
//            iBaseResource = se.getOperationOutcome();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        ResponseEntity<String> responseEntity = new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
//        return responseEntity;
//    }
//
//
//    protected IParser parser(String contentType) {
//        if ( contentType.contains("application/fhir+xml")){
//            return myContext.newXmlParser();
//        } else if ( contentType.contains("application/fhir+json")){
//            return myContext.newJsonParser();
//        } else {
//            return myContext.newJsonParser();
//        }
//
//    }
//
//    /////////////////////////////////////////////////////////////////////////////
//
//    @RequestMapping (
//            method = RequestMethod.POST,
//            value = "/fhir/{resourceType}/{id}",
//            produces =  "application/fhir+xml"
//    )
//    public String postResourceXml(
//            @RequestHeader("Accept") String accept,
//            @RequestHeader("Content-Type") String contentType,
//            @RequestBody  String requestBody,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @RequestParam Map<String, String> queryParams
//    ) throws FHIRException {
//        logger.log(Level.INFO,"POST "+resourceType+" "+id );
//        IBaseResource body = parser(contentType).parseResource(requestBody);
//        MethodOutcome methodOutcome = fhirServer.doPost(body);
//        return parser( accept ).encodeResourceToString(methodOutcome.getResource());
//    }
//
//    @RequestMapping (
//            method = RequestMethod.POST,
//            value = "/fhir/{resourceType}/{id}",
//            produces =  "application/fhir+json"
//    )
//    public String postResourceJson(
//            @RequestHeader("Accept") String accept,
//            @RequestHeader("Content-Type") String contentType,
//            @RequestBody  String requestBody,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @RequestParam Map<String, String> queryParams
//    ) throws FHIRException {
//        logger.log(Level.INFO,"POST "+resourceType+" "+id );
//        IBaseResource body = parser(contentType).parseResource(requestBody);
//        MethodOutcome methodOutcome = fhirServer.doPost(body);
//        return parser( accept ).encodeResourceToString(methodOutcome.getResource());
//    }
//
//    /////////////////////////////////////////////////////////////////////////////
//
//
//    @RequestMapping (
//            method = RequestMethod.POST,
//            value = "/fhir/{resourceType}/{id}/{params}",
//            produces =  "application/fhir+json"
//    )
//    public String postJsonResourceWithParams(
//            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
//            @RequestHeader(value = "Content-Type", defaultValue = "application/fhir+json") String contentType,
//            @RequestBody  String requestBody,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @PathVariable String params,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        logger.log(Level.INFO,"POST "+resourceType+" "+id );
//        IBaseResource body = parser(contentType).parseResource(requestBody);
//        IBaseResource iBaseResource = fhirServer.doPost(resourceType, id, body, params, queryParams);
//        return parser( accept ).encodeResourceToString(iBaseResource);
//    }
//
//    @RequestMapping (
//            method = RequestMethod.POST,
//            value = "/fhir/{resourceType}/{id}/{params}",
//            produces =  "application/fhir+xml"
//    )
//    public String postXmlResourceWithParams(
//            @RequestHeader("Accept") String accept,
//            @RequestHeader("Content-Type") String contentType,
//            @RequestBody  String requestBody,
//            @PathVariable String resourceType,
//            @PathVariable String id,
//            @PathVariable String params,
//            @RequestParam Map<String, String> queryParams
//    ) throws Exception {
//        logger.log(Level.INFO,"POST "+resourceType+" "+id );
//        IBaseResource body = parser(contentType).parseResource(requestBody);
//        IBaseResource iBaseResource = fhirServer.doPost(resourceType, id, body, params, queryParams);
//        return parser( accept ).encodeResourceToString(iBaseResource);
//    }
//}
