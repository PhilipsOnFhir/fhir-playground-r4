package org.github.philipsonfhir.fhirproxy.common.fhircall;

import ca.uhn.fhir.context.FhirContext;
import lombok.Getter;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class FhirRequest {

    private String subPath;
    private String query;
    private final static FhirContext ourCtx = FhirContext.forR4();
    private String bodyStr = null;
    private IBaseResource bodyResource = null;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String resourceType = null;
    private String resourceId= null;
    private String param= null;
    private Map<String, String> queryMap= null;
    private List<String> partList= null;
    private String operationName;
    private String base;
    private String method;

    public FhirRequest(String base, String call, String query )throws FhirProxyException {

        this.base = base;
        this.query = query;

        processUrls(base, call, query);

    }

    public FhirRequest(String base, HttpServletRequest request, HttpServletResponse response) throws FhirProxyException {
        this.base = base;
        this.request = request;
        this.response = response;

        String operation = request.getMethod();
        String requestUriStr = request.getRequestURI();
        String queryStr = request.getQueryString();

        processUrls(base, requestUriStr, queryStr);
        this.method = this.request.getMethod();

        if ( request.getMethod().equals("PUT") || request.getMethod().equals("POST")){
            StringBuffer sb = new StringBuffer();
            BufferedReader bufferedReader = null;
            String content = "";

            try {
                //InputStream inputStream = request.getInputStream();
                //inputStream.available();
                //if (inputStream != null) {
                bufferedReader =  request.getReader() ; //new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) {
                    sb.append(charBuffer, 0, bytesRead);
                }
                //} else {
                //        sb.append("");
                //}

            } catch (IOException ex) {
                throw new FhirProxyException(ex);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw new FhirProxyException(ex);
                    }
                }
            }

            bodyStr  = sb.toString();
            String contentType = request.getContentType();
            if ( contentType.startsWith("application/fhir+json") || contentType.startsWith( "application/json" ) ) {
                bodyResource = this.ourCtx.newJsonParser().parseResource(bodyStr);
            } else if ( contentType.startsWith("application/fhir+xml") || contentType.startsWith( "application/xml" ) ) {
                bodyResource = this.ourCtx.newXmlParser().parseResource(bodyStr);
            }

        }
    }

    public FhirRequest(String method, String s, String s1, String s2) throws FhirProxyException {
        this( s,s1,s2);
        this.method = method;
    }

    private void processUrls(String base, String requestUrlStr, String queryStr) throws FhirProxyException {
        if ( !requestUrlStr.startsWith(base )){
            throw new FhirProxyException( String.format( "url %s does not start with %s",requestUrlStr, base));
        }
//        URL baseUrl = new URL("http:localhost"+base);
//        URL url = new URL(requestUrlStr);
//        String path = url.getPath();
//        String query = url.getQuery();

        this.subPath = requestUrlStr.substring(base.length());

        this.partList = Arrays.asList(subPath.split("/")).stream()
                .filter( p -> !p.isEmpty())
                .collect(Collectors.toList());

        switch ( partList.size()){
            case 4:
            case 3: this.param = checkOperationName( partList.get(2) );
            case 2: this.resourceId = checkOperationName( partList.get(1) );
            case 1: this.resourceType = checkOperationName( partList.get(0) );
                break;
        }

        this.query = queryStr;
        this.queryMap = splitQuery(queryStr);
    }


    private String checkOperationName(String s) {
        if( operationName==null && s.startsWith("$")){
            operationName =s;
            return null;
        }
        return s;
    }


    private Map<String, String> splitQuery(String query) {
        Map<String,String> map = new HashMap<>();
        if ( query==null || query.length()==0){return map;}
        for ( String it: query.split("&")){
            int idx = it.indexOf("=");
            String key = idx > 0 ? it.substring(0, idx) : it;
            String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
            map.put(key, value);
        }
        return map;
    }


    public boolean isOperationCall() {
        return operationName!=null;
    }

    public boolean isMetadataCall() {
        return this.resourceType!=null && this.resourceType.equals("metadata") && this.method.equals( "GET" );
    }

    public String getMethod() {
        return method;
    }

    public String getSearchUrl() {
        return this.subPath+this.query;
    }

    public String toString(){
        String operation = request.getMethod();
        String requestUriStr = request.getRequestURI();
        String queryStr = request.getQueryString();
        return  operation + " " + requestUriStr + "?" + queryStr;
    }
}
