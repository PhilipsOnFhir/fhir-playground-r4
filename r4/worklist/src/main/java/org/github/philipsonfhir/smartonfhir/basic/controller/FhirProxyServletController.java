package org.github.philipsonfhir.smartonfhir.basic.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

@WebServlet(urlPatterns = "/"+ FhirProxyServletController.base+"/*")
public class FhirProxyServletController extends HttpServlet {

    static final String base="fhirservlet";
    private final FhirContext ourCtx;
    private final IGenericClient ourClient;
    private org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass());


    private String fhirServerUrl;

    FhirProxyServletController(@Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        this.fhirServerUrl = fhirServerUrl;
        ourCtx = FhirContext.forR4();
        ourClient = ourCtx.newRestfulGenericClient(fhirServerUrl);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if ( getFhirRequestUri(req).startsWith("metadata")){
            // handle Capability Statement
            CapabilityStatement capabilityStatement = ourClient.capabilities().ofType(CapabilityStatement.class).execute();

        }
        forwardRequest("GET", req, resp);

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        forwardRequest("POST", req, resp);
    }

//    public void forwardRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        this.forwardRequest("GET",request,response);
//    }

    //https://stackoverflow.com/questions/12130992/forward-httpservletrequest-to-a-different-server/22572736
    private void forwardRequest(String method, HttpServletRequest req, HttpServletResponse resp) {
        final boolean hasoutbody = (method.equals("POST"));

        try {

//            final URL url = new URL(
//                    GlobalConstants.CLIENT_BACKEND_HTTPS  // no trailing slash +
//                     req.getRequestURI()
//                    + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
            final URL url = new URL(
                    this.fhirServerUrl+
                            getFhirRequestUri(req) +
                    (req.getQueryString() != null ? "?" + req.getQueryString() : "" )
            );
            logger.info(method+" "+ url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            final Enumeration<String> headers = req.getHeaderNames();
            while (headers.hasMoreElements()) {
                final String header = headers.nextElement();
                final Enumeration<String> values = req.getHeaders(header);
                while (values.hasMoreElements()) {
                    final String value = values.nextElement();
                    conn.addRequestProperty(header, value);
                }
            }
            logger.info(method+" "+ url.toString()+ "headers DONE");

            //conn.setFollowRedirects(false);  // throws AccessDenied exception
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(hasoutbody);
            conn.connect();

            final byte[] buffer = new byte[16384];
            while (hasoutbody) {
                final int read = req.getInputStream().read(buffer);
                if (read <= 0) break;
                conn.getOutputStream().write(buffer, 0, read);
            }
            logger.info(method+" "+ url.toString()+ " connect DONE");

            resp.setStatus(conn.getResponseCode());
            for (int i = 1; ; ++i) {
                final String header = conn.getHeaderFieldKey(i);
                if (header == null) break;
                final String value = conn.getHeaderField(i);
                resp.setHeader(header, value);
            }
            logger.info(method+" "+ url.toString()+ " response headers DONE "+conn.getResponseCode());

            try {
                while (true) {
                    final int read = conn.getInputStream().read(buffer);
                    if (read <= 0) break;
                    resp.getOutputStream().write(buffer, 0, read);
                }
                logger.info(method + " " + url.toString() + " response content DONE");
            }
            catch( FileNotFoundException e){
                logger.info(method + " " + url.toString() + " response has no content");
                resp.sendError(404);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString() );
            e.printStackTrace();
//            resp.sendError();
        }
    }

    private String getFhirRequestUri(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getRequestURI().indexOf("/fhir/")+2+base.length());
    }

}
