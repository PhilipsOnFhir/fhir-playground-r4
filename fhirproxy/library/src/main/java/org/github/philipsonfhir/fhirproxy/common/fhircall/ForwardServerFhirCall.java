package org.github.philipsonfhir.fhirproxy.common.fhircall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

public class ForwardServerFhirCall {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private boolean done = false;
    private final String fhirServerUrl;
    private final String base;

//    public ForwardServerFhirCall(String fhirServerUrl, FhirRequest fhirRequest ) {
//        super();
//        this.request = fhirRequest.getRequest();
//        this.response = fhirRequest.getResponse();
//        this.base = fhirRequest.getBase();
//        this.fhirServerUrl = fhirServerUrl;
//    }

    public ForwardServerFhirCall(String fhirServerUrl, String base, HttpServletRequest request, HttpServletResponse response) {
        super();
        this.request =request;
        this.response = response;
        this.fhirServerUrl = fhirServerUrl;
        this.base = base;
    }

    public void execute() {
        final boolean hasoutbody = (request.getMethod().equals("POST") || request.getMethod().equals("PUT"));

        try {

//            final URL url = new URL(
//                    GlobalConstants.CLIENT_BACKEND_HTTPS  // no trailing slash +
//                     req.getRequestURI()
//                    + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
            final URL url = new URL(
                    this.fhirServerUrl+
                            getFhirRequestUri(request) +
                    (request.getQueryString() != null ? "?" + request.getQueryString() : "" )
            );
//            logger.info(method+" "+ url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(request.getMethod());

            final Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                final String header = headers.nextElement();
                final Enumeration<String> values = request.getHeaders(header);
                while (values.hasMoreElements()) {
                    final String value = values.nextElement();
                    conn.addRequestProperty(header, value);
                }
            }
//            logger.info(method+" "+ url.toString()+ "headers DONE");

            //conn.setFollowRedirects(false);  // throws AccessDenied exception
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(hasoutbody);
            conn.connect();

            final byte[] buffer = new byte[16384];
            while (hasoutbody) {
                final int read = request.getInputStream().read(buffer);
                if (read <= 0) break;
                conn.getOutputStream().write(buffer, 0, read);
            }
//            logger.info(method+" "+ url.toString()+ " connect DONE");

            response.setStatus(conn.getResponseCode());
            for (int i = 1; ; ++i) {
                final String header = conn.getHeaderFieldKey(i);
                if (header == null) break;
                final String value = conn.getHeaderField(i);
                response.setHeader(header, value);
            }
//            logger.info(method+" "+ url.toString()+ " response headers DONE "+conn.getResponseCode());

            try {
                while (true) {
                    final int read = conn.getInputStream().read(buffer);
                    if (read <= 0) break;
                    response.getOutputStream().write(buffer, 0, read);
                }
//                logger.info(method + " " + url.toString() + " response content DONE");
            }
            catch( FileNotFoundException e){
//                logger.info(method + " " + url.toString() + " response has no content");
                response.sendError(404);
            }
        } catch (Exception e) {
//            logger.error(e.getStackTrace().toString() );
            e.printStackTrace();
//            resp.sendError();

        }
        done = true;
    }

    private String getFhirRequestUri(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getRequestURI().indexOf("/fhir/")+2+base.length());
    }


}
