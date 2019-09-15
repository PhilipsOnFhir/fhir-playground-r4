package org.github.philipsonfhir.smartonfhir.basic.controller;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.github.philipsonfhir.smartonfhir.PathValues;
import org.github.philipsonfhir.smartonfhir.basic.controller.service.FhirServer;
import org.github.philipsonfhir.smartonfhir.secureproxy.support.SmartOnFhirServer;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin( origins = "*")
@RestController( )
public class FhirProxyController {

    protected final String prefix="/fhir";
    private FhirServer fhirServer = null ;
    private org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass());

    FhirProxyController( @Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        this.fhirServer = new FhirServer(fhirServerUrl);
    }

    public FhirProxyController(SmartOnFhirServer fhirServer) {
        this.fhirServer = fhirServer;
    }

    public FhirProxyController(){}

    @RequestMapping (
            method = RequestMethod.GET,
            value = prefix+"/{resourceType}"
    )
    public ResponseEntity<String> getResourceType(
            HttpServletRequest request,
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String resourceType,
            @RequestParam Map<String, String> queryParams
    ) {
        IBaseResource iBaseResource;
        HttpStatus httpStatus = HttpStatus.OK;
        try {

            if (resourceType.equals("metadata")) {
                logger.info("JSON GET CapabilityStatement");
                iBaseResource = fhirServer.getCapabilityStatement();
            } else {
                logger.info("JSON GET " + resourceType);
                iBaseResource = fhirServer.doSearch(resourceType,queryParams);
            }
        }
        catch ( BaseServerResponseException e ){
            iBaseResource = e.getOperationOutcome();
            httpStatus = HttpStatus.resolve( e.getStatusCode() );
        } catch ( FHIRException e1 ){
            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
                    .setDiagnostics( e1.getMessage() )
            );
            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
        }
        httpStatus = ( httpStatus==null? HttpStatus.INTERNAL_SERVER_ERROR: httpStatus );
        return new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
    }

    @RequestMapping (
            method = RequestMethod.GET,
            value = prefix+"/{resourceType}/{resourceId}"
    )
    public ResponseEntity<String> getResource(
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String resourceType,
            @PathVariable String resourceId,
            @RequestParam Map<String, String> queryParams
    ) {
        logger.info("JSON GET " + resourceType+ " "+ resourceId);
        IBaseResource iBaseResource;
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            iBaseResource = fhirServer.doGet(resourceType,resourceId,queryParams);
        }
        catch ( BaseServerResponseException e ){
            iBaseResource = e.getOperationOutcome();
            httpStatus = HttpStatus.resolve( e.getStatusCode() );
        } catch ( FHIRException e1 ){
            iBaseResource = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
                    .setDiagnostics( e1.getMessage() )
            );
            httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
        }
        httpStatus = ( httpStatus==null? HttpStatus.INTERNAL_SERVER_ERROR: httpStatus );
        return new ResponseEntity<>( parser( accept ).encodeResourceToString(iBaseResource), httpStatus );
    }

    private IParser parser(String contentType) {
        if ( contentType.contains("application/fhir+xml")){
            return fhirServer.getCtx().newXmlParser();
        } else if ( contentType.contains("application/fhir+json")){
            return fhirServer.getCtx().newJsonParser();
        } else {
            return fhirServer.getCtx().newJsonParser();
        }

    }

}
