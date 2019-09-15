package org.github.philipsonfhir.fhirproxy.controller;//package org.github.philipsonfhir.smartonfhir.basic.org.github.philipsonfhir.fhirproxy.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhirproxy.async.service.AsyncService;
import org.github.philipsonfhir.fhirproxy.bulkdata.BulkDataImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperationRepository;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCallFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;

@WebServlet(urlPatterns = FhirProxyServletController.base+"/*")
public class FhirProxyServletController extends HttpServlet {

    static final String base="/fhirservlet";
    private final FhirContext ourCtx;
    private final IGenericClient ourClient;
    private final AsyncService asyncService;
    private FhirCallFactory fhirCallFactory = null;
    private org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass());


    private String fhirServerUrl;

    @Autowired
    FhirProxyServletController(@Value("${proxy.fhirserver.url}") String fhirServerUrl, AsyncService asyncService ){
        this.fhirServerUrl = fhirServerUrl;
        this.asyncService = asyncService;
        ourCtx = FhirContext.forR4();
        ourClient = ourCtx.newRestfulGenericClient(fhirServerUrl);
        fhirCallFactory = new FhirCallFactory(  base, fhirServerUrl );
        fhirCallFactory.addImplementationGuide( new BulkDataImplementationGuide() );
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
//        if ( getFhirRequestUri(req).startsWith("metadata")){
//            // handle Capability Statement
//            CapabilityStatement capabilityStatement = ourClient.capabilities().ofType(CapabilityStatement.class).execute();
//        }
//        String prefer = req.getHeader("Prefer");
//        if ( prefer!=null && prefer.equals("respond-async") ){
//            String callUrl = request.getRequestURL().toString();
//            logger.info( "Ansync request detected" );
//            String  sessionId = asyncService.newAyncGetSession( request.getRequestURL().toString(), fhirServer, resourceType, null, null, queryParams );
//            String sessionUrl = callUrl.substring( 0, callUrl.indexOf( base ) ) + "/async-services/" + sessionId;
//
//            resp.setStatus( 202 );
//            resp.setHeader( CONTENT_LOCATION, sessionUrl );
//            OperationOutcome operationOutcome = new OperationOutcome();
//            operationOutcome.addIssue()
//                    .setSeverity( OperationOutcome.IssueSeverity.INFORMATION )
//                    .setCode( OperationOutcome.IssueType.VALUE )
//                    .addLocation( sessionUrl )
//            ;
//
//            return new ResponseEntity<>(
//                    parser( accept ).encodeResourceToString( operationOutcome ),
//                    HttpStatus.ACCEPTED
//            );

        IBaseResource result;
        try {
            String prefer = req.getHeader("Prefer");
            if ( prefer!=null && prefer.equals("respond-async") ){
                logger.info( "Ansync request detected" );
                FhirCall fhirCall = fhirCallFactory.createGetFhirCall( req, resp );
                String sessionId = asyncService.newAyncGetSession( req.getRequestURL().toString(), fhirCall );

                String callUrl = req.getRequestURL().toString();
                String sessionUrl = callUrl.substring( 0, callUrl.indexOf( "fhir/" ) ) + "async-services/" + sessionId;
                resp.setStatus( 202 );
                resp.setHeader( CONTENT_LOCATION, sessionUrl );
                OperationOutcome operationOutcome = new OperationOutcome();
                operationOutcome.addIssue()
                        .setSeverity( OperationOutcome.IssueSeverity.INFORMATION )
                        .setCode( OperationOutcome.IssueType.VALUE )
                        .addLocation( sessionUrl )
                ;

                result = operationOutcome;
            } else {
                FhirCall fhirCall = fhirCallFactory.createGetFhirCall(req,resp);
                fhirCall.execute();
                result = fhirCall.getResource();
                resp.setStatus(HttpStatus.OK.value());
            }
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



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        FhirCall fhirCall = fhirCallFactory.createPutFhirCall(req,resp);
        fhirCall.execute();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        FhirCall fhirCall = fhirCallFactory.createPostFhirCall(req,resp);
        fhirCall.execute();
    }

////    public void forwardRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        this.forwardRequest("GET",request,response);
////    }
//
//    //https://stackoverflow.com/questions/12130992/forward-httpservletrequest-to-a-different-server/22572736
//    private void forwardRequest(String method, HttpServletRequest req, HttpServletResponse resp) {
//        final boolean hasoutbody = (method.equals("POST"));
//
//        try {
//
////            final URL url = new URL(
////                    GlobalConstants.CLIENT_BACKEND_HTTPS  // no trailing slash +
////                     req.getRequestURI()
////                    + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
//            final URL url = new URL(
//                    this.fhirServerUrl+
//                            getFhirRequestUri(req) +
//                    (req.getQueryString() != null ? "?" + req.getQueryString() : "" )
//            );
//            logger.info(method+" "+ url.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod(method);
//
//            final Enumeration<String> headers = req.getHeaderNames();
//            while (headers.hasMoreElements()) {
//                final String header = headers.nextElement();
//                final Enumeration<String> values = req.getHeaders(header);
//                while (values.hasMoreElements()) {
//                    final String value = values.nextElement();
//                    conn.addRequestProperty(header, value);
//                }
//            }
//            logger.info(method+" "+ url.toString()+ "headers DONE");
//
//            //conn.setFollowRedirects(false);  // throws AccessDenied exception
//            conn.setUseCaches(false);
//            conn.setDoInput(true);
//            conn.setDoOutput(hasoutbody);
//            conn.connect();
//
//            final byte[] buffer = new byte[16384];
//            while (hasoutbody) {
//                final int read = req.getInputStream().read(buffer);
//                if (read <= 0) break;
//                conn.getOutputStream().write(buffer, 0, read);
//            }
//            logger.info(method+" "+ url.toString()+ " connect DONE");
//
//            resp.setStatus(conn.getResponseCode());
//            for (int i = 1; ; ++i) {
//                final String header = conn.getHeaderFieldKey(i);
//                if (header == null) break;
//                final String value = conn.getHeaderField(i);
//                resp.setHeader(header, value);
//            }
//            logger.info(method+" "+ url.toString()+ " response headers DONE "+conn.getResponseCode());
//
//            try {
//                while (true) {
//                    final int read = conn.getInputStream().read(buffer);
//                    if (read <= 0) break;
//                    resp.getOutputStream().write(buffer, 0, read);
//                }
//                logger.info(method + " " + url.toString() + " response content DONE");
//            }
//            catch( FileNotFoundException e){
//                logger.info(method + " " + url.toString() + " response has no content");
//                resp.sendError(404);
//            }
//        } catch (Exception e) {
//            logger.error(e.getStackTrace().toString() );
//            e.printStackTrace();
////            resp.sendError();
//        }
//    }
//
//    private String getFhirRequestUri(HttpServletRequest req) {
//        return req.getRequestURI().substring(req.getRequestURI().indexOf("/fhir/")+2+base.length());
//    }

}
