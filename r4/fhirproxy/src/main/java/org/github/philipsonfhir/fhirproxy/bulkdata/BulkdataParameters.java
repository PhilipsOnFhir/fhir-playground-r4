package org.github.philipsonfhir.fhirproxy.bulkdata;

import lombok.Getter;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import java.util.Map;

@Getter
class BulkdataParameters {

    private String outputFormat=null;
    private InstantType since=null;
    private String type=null;

    BulkdataParameters(FhirRequest fhirRequest ) throws FhirProxyException {
        if ( fhirRequest.getMethod().equals("GET")){
            Map<String, String> queryMap = fhirRequest.getQueryMap();

            for(Map.Entry<String, String> entry: queryMap.entrySet()){
                switch( entry.getKey() ){
                    case "outputFormat":
                    case "_outputFormat":
                        outputFormat =entry.getValue();
                        break;
                    case "since":
                    case "_since":
                        since = new InstantType(entry.getValue());
                        break;
                    case "type":
                    case "_type":
                        type = entry.getValue();
                        break;
                }
            }
        }
        else {
            IBaseResource resource = fhirRequest.getBodyResource();
            if ( !(resource instanceof Parameters) ) {
                throw new FhirProxyException("body of a POST operation call must be a Parameters");
            }
            Parameters parameters = (Parameters) resource;
            for ( Parameters.ParametersParameterComponent param: parameters.getParameter()){
                switch (param.getName()){
                    case "outputFormat":
                    case "_outputFormat":
                        outputFormat =((StringType)param.getValue()).getValue();
                        break;
                    case "since":
                    case "_since":
                        since = (InstantType)param.getValue();
                        break;
                    case "type":
                    case "_type":
                        type = ((StringType)param.getValue()).getValue();
                        break;
                }
            }
        }
    }
}
