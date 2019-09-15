package org.github.philipsonfhir.fhirproxy.async;

import ca.uhn.fhir.context.FhirContext;
import org.github.philipsonfhir.fhirproxy.async.model.AsyncSession;
import org.github.philipsonfhir.fhirproxy.async.service.AsyncService;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RestController
@CrossOrigin(origins = "*", allowedHeaders = {AsyncController.X_PROGRESS}, exposedHeaders = {AsyncController.X_PROGRESS})
public class AsyncController {
    public static final String X_PROGRESS = "X-Progress";
    private Logger logger = Logger.getLogger( AsyncController.class.getName());
    private FhirContext ourCtx = FhirContext.forR4();
    private final AsyncService asyncService;

    @Autowired
    AsyncController(AsyncService asyncService ){
        this.asyncService = asyncService;
    }


    @GetMapping("/async-services/{id}")
    public void getBulkData(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String id ) throws FhirProxyException {

        String resourceName = null;

        AsyncSession session = asyncService.getAsyncSession( id );

        switch ( asyncService.getSessionStatus( id ) ) {
            case PROCESSING: {
                response.setStatus(202);
                String linkHeader = "";
                JSONObject json = new JSONObject();

//                Date transactionTime = session.getTransActionTime();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                json.put( "transactionTime", sdf.format( transactionTime ) );
//
//                json.put( "request", session.getRequestUrl() );
//                json.put("secure", "false");
//                try {
//                    response.getWriter().write(json.toJSONString());
//                } catch (IOException e) {
//                    logger.severe("Oeps something went wrong");
//                }
//
                response.setContentType("text/x-json;charset=UTF-8");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader( X_PROGRESS, session.getProcessDescription() );
            }
            break;
            case UNKNOWN:
                response.setStatus(404);
                break;
            case READY: {
                if (resourceName == null) {
                    response.setStatus(200);
                    /*LINK IN HDR*/
//                    String linkHeader = "";

                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");

                    JSONObject json = new JSONObject();
                    {
                        Date transactionTime = session.getTransActionTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        json.put("transactionTime", sdf.format(transactionTime));

                        json.put("request", session.getRequestUrl());
                        json.put("requiresAccessToken ", "false");

                        JSONArray outputLinks = new JSONArray();
                        Iterator<String> resourceNameIterator = session.getResultResourceNames().iterator();
                        while (resourceNameIterator.hasNext()) {
                            String linkedResourceName = resourceNameIterator.next();
                            JSONObject linkObject = new JSONObject();
                            linkObject.put("type", linkedResourceName);
                            String resourceUrl = request.getRequestURL().toString() + "/result/" + linkedResourceName;
                            linkObject.put("url", resourceUrl);
                            linkObject.put("count", session.getResourceCount(linkedResourceName));

                            outputLinks.add(linkObject);
//                            /*LINK IN HDR*/
//                            linkHeader += "<" + resourceUrl + ">";
//                            if (resourceNameIterator.hasNext()) {
//                                linkHeader += ",";
//                            }
                        }
                        json.put("output", outputLinks);

                        JSONArray errorLinks = new JSONArray();
                        for (Map.Entry<String, OperationOutcome> entry : session.getErrorMap().entrySet()) {
                            String linkedResourceName = entry.getKey();
                            JSONObject linkObject = new JSONObject();
                            linkObject.put("type", linkedResourceName);
                            String resourceUrl = request.getRequestURL().toString() + "/error/" + linkedResourceName;
                            linkObject.put("url", resourceUrl);
                            linkObject.put("count", 1);

                            errorLinks.add(linkObject);
//                            /*LINK IN HDR*/
//                            linkHeader += "<" + resourceUrl + ">";
//                            linkHeader += ",";
                        }
                        json.put("error", errorLinks);
                    }
                    try {
                        response.getWriter().write(json.toJSONString());
                    } catch (IOException e) {
                        logger.severe("Oeps something went wrong");
                    }
                } else {
                    List<Resource> resourceList = session.getResultResources( resourceName );

                    if ( request.getHeader("Content-type")!=null && request.getHeader("Content-Type").equals("application/fhir+ndjson") ){
                        String ndjson = "";

                        Iterator<Resource> resourceIterator = resourceList.iterator();
                        while (resourceIterator.hasNext()) {
                            Resource resource = resourceIterator.next();
                            String resourceJson = ourCtx.newJsonParser().encodeResourceToString(resource);
                            ndjson += resourceJson.replace( "\n", " " ) + "\n";
                            response.setContentType("application/fhir+ndjson;charset=UTF-8");
                            response.setHeader("Cache-Control", "no-cache");
                        }
                        try {
                            response.getWriter().write( ndjson );
                        } catch (IOException e) {
                            logger.severe("Oeps something went wrong");
                        }
                    } else {
                        // download file
                        Bundle bundle = new Bundle();
                        resourceList.stream().forEach( resource -> bundle.addEntry().setResource(resource));
                        String resourceJson = ourCtx.newJsonParser().encodeResourceToString(bundle);
                        response.setContentType("text/json;charset=UTF-8");
                        response.setHeader("Cache-Control", "no-cache");
                        try {
                            response.getWriter().write(resourceJson);
                        } catch (IOException e) {
                            logger.severe("Oeps something went wrong");
                        }
                    }
                }
            }
            break;
        }
    }

    @DeleteMapping("/async-services/{id}")
    public void deleteBulkDataSession(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable String id ) {
        AsyncSession asyncSession = asyncService.getAsyncSession( id );
        if ( asyncSession==null ){
            response.setStatus( HttpStatus.NOT_FOUND.value() );
        }
        else{
            asyncService.deleteSession( id );
            response.setStatus( HttpStatus.OK.value() );
        }
    }

    @GetMapping("/async-services/{id}/result/{resourceType}")
    public void getBulkData(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable String id,
        @PathVariable String resourceType
    ) throws FHIRException, FhirProxyException {

        AsyncSession session = asyncService.getAsyncSession( id );
        if ( session == null ) {
            throw new FHIRException( "session not found" );
        }

        switch ( asyncService.getSessionStatus( id ) ) {
            case PROCESSING: {
                logger.severe("Resource "+resourceType+" called before ready");
            }
            break;

            case READY: {
                    List<Resource> resourceList = session.getResultResources( resourceType );

                    // always return ndJson
                    String ndjson = "";

                    Iterator<Resource> resourceIterator = resourceList.iterator();
                    while (resourceIterator.hasNext()) {
                        Resource resource = resourceIterator.next();
                        String resourceJson = ourCtx.newJsonParser().encodeResourceToString(resource);
                        ndjson += resourceJson.replace("\n", " ") + "\n";
                    }
                    response.setContentType("application/fhir+ndjson;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");
                    try {
                        response.getWriter().write(ndjson);
                    } catch (IOException e) {
                        logger.severe("Oeps something went wrong");
                    }
                }
                break;
            case UNKNOWN:
            default:
                response.setStatus(404);
                break;
        }
    }

    @GetMapping("/async-services/{id}/error/{resourceType}")
    public void getBulkDataErrors(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable String id,
        @PathVariable String resourceType
    ) throws FHIRException {

        AsyncSession session = asyncService.getAsyncSession( id );
        if ( session == null ) {
            throw new FHIRException( "session not found" );
        }

        switch ( asyncService.getSessionStatus( id ) ) {
            case PROCESSING: {
                logger.severe( "Resource " + resourceType + " called before ready" );
            }
            break;
            case UNKNOWN:
                response.setStatus( 404 );
                break;
            case READY: {
                OperationOutcome operationOutcome = session.getErrorMap().get( resourceType );

                String ndjson = "";

                String resourceJson = ourCtx.newJsonParser().encodeResourceToString( operationOutcome );
                ndjson += resourceJson.replace( "\n", " " ) + "\n";

                response.setContentType( "application/fhir+ndjson;charset=UTF-8" );
                response.setHeader( "Cache-Control", "no-cache" );
                try {
                    response.getWriter().write( ndjson );
                } catch ( IOException e ) {
                    logger.severe( "Oeps something went wrong" );
                }
            }
            break;
        }
    }
}
