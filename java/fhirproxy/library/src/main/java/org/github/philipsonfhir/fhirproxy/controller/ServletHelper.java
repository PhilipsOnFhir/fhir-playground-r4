package org.github.philipsonfhir.fhirproxy.controller;//package org.github.philipsonfhir.smartonfhir.basic.org.github.philipsonfhir.fhirproxy.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCallFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

public class ServletHelper {

    public static final String base="/fhirservlet";
    private final FhirContext ourCtx;
    private final IGenericClient ourClient;
    private FhirCallFactory fhirCallFactory = null;
    private org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass());


    private String fhirServerUrl;

    public ServletHelper( String fhirServerUrl ){
            logger.info("FHIR-server : "+ fhirServerUrl);
        this.fhirServerUrl = fhirServerUrl;
        ourCtx = FhirContext.forR4();
        ourClient = ourCtx.newRestfulGenericClient(fhirServerUrl);
        fhirCallFactory = new FhirCallFactory(  base, fhirServerUrl );
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

        IBaseResource result;
        try {
            FhirCall fhirCall = fhirCallFactory.createGetFhirCall(req,resp);
            fhirCall.execute();
            result = fhirCall.getResource();
            resp.setStatus(HttpStatus.OK.value());
        } catch (FhirProxyException | MalformedURLException e) {
            OperationOutcome operationOutcome = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
                    .setDiagnostics( e.getMessage() )
            );
            result = operationOutcome;
            resp.setStatus( HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        PrintWriter out = null;
        String resultStr = "";
        String contentType = req.getContentType();
        contentType = ( contentType==null? contentType="application/fhir+json":contentType);
        switch( contentType ){
            case "application/xml":
            case "application/fhir+xml":
                resp.setContentType("application/fhir+xml");
                resultStr = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
                break;
            case "application/json":
            case "application/fhir+json":
            default:
                resp.setContentType("application/fhir+json");
                resultStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result);
                break;
        }

        try {
            out = resp.getWriter();
            out.print( resultStr );
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resp.setCharacterEncoding("UTF-8");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        IBaseResource result;
        try {
            FhirCall fhirCall = fhirCallFactory.createPostFhirCall(req,resp);
            fhirCall.execute();
            result = fhirCall.getResource();
            resp.setStatus(HttpStatus.OK.value());
        } catch (FhirProxyException| FHIRException e) {
            OperationOutcome operationOutcome = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
                    .setDiagnostics( e.getMessage() )
            );
            result = operationOutcome;
            resp.setStatus( HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        PrintWriter out = null;
        String resultStr = "";
        String contentType = req.getContentType();
        contentType = ( contentType==null? contentType="application/fhir+json":contentType);
        switch( contentType ){
            case "application/xml":
            case "application/fhir+xml":
                resp.setContentType("application/fhir+xml");
                resultStr = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
                break;
            case "application/json":
            case "application/fhir+json":
            default:
                resp.setContentType("application/fhir+json");
                resultStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result);
                break;
        }

        try {
            out = resp.getWriter();
            out.print( resultStr );
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resp.setCharacterEncoding("UTF-8");
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        IBaseResource result;
        try {
            FhirCall fhirCall = fhirCallFactory.createPutFhirCall(req,resp);
            fhirCall.execute();
            result = fhirCall.getResource();
            resp.setStatus(HttpStatus.OK.value());
        } catch (FhirProxyException e) {
            OperationOutcome operationOutcome = new OperationOutcome().addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity( OperationOutcome.IssueSeverity.FATAL )
                    .setDiagnostics( e.getMessage() )
            );
            result = operationOutcome;
            resp.setStatus( HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
