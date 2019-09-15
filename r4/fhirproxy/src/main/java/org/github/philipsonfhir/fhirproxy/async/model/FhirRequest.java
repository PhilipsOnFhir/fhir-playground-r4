//package org.github.philipsonfhir.fhirproxy.async.model;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
//import org.hl7.fhir.exceptions.FHIRException;
//import org.hl7.fhir.instance.model.api.IBaseResource;
//import org.hl7.fhir.r4.model.Goal;
//
//import java.util.Map;
//
//@Getter
//@Setter
//public class FhirRequest {
//    private String callUrl;
//    private FhirServer fhirServer;
//    private String resourceType;
//    private String id;
//    private String params;
//    private Map<String, String> queryParams;
//
//    public FhirRequest(String callUrl, FhirServer fhirServer, String resourceType, String id, String params, Map<String, String> queryParams) {
//        this.callUrl = callUrl;
//        this.fhirServer = fhirServer;
//        this.resourceType = resourceType;
//        this.id = id;
//        this.params = params;
//        this.queryParams = queryParams;
//    }
//
//    public IBaseResource getResource() throws FHIRException {
//        if ( resourceType == null ) {
//            throw new FHIRException("resource type not set");
//        }
//        if ( id!=null){
//            if ( params!=null ) {
//                return fhirServer.doGet(resourceType, id, params, queryParams);
//            } else{
//                return fhirServer.doGet(resourceType, id, queryParams);
//            }
//        } else{
//            if ( params!=null ) {
//                throw new FHIRException("params on search not supported.");
//            } else{
//                return fhirServer.doGet( resourceType, queryParams);
//            }
//        }
//    }
//
//    public boolean returnNdJson() {
//        if ( queryParams == null ) {
//            return true;
//        }
//
//        String outputFormat = queryParams.get( "_outputFormat" );
//        return outputFormat == null ||
//            (outputFormat != null && outputFormat.equals( "application/fhir+ndjson" ));
//    }
//
//    public Goal getFhirOperationCall() {
//        return null;
//    }
//}
