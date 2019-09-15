package org.github.philipsonfhir.fhirproxy.bulkdata.experimental;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.r4.model.Parameters;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.List;

public class ImportFhirOperation implements org.github.philipsonfhir.fhirproxy.common.operation.FhirOperation {
    static final String ndjsonMimetype = "application/fhir+ndjson";
    @Override
    public FhirCall createFhirCall(FhirServer fhirServer, FhirRequest fhirRequest) throws FhirProxyException {
        if ( fhirRequest.getMethod()!="POST"){
            throw new FhirProxyException("$import only accepts POST methods - "+fhirRequest.getMethod());
        }
//        Parameters parameters = (Parameters) fhirRequest.getBodyResource();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ImportData importData = objectMapper.readValue( fhirRequest.getBodyStr(), ImportData.class );
            if ( !importData.importFormat.equals(ndjsonMimetype)){
                throw new FhirProxyException("import format must be "+ndjsonMimetype);
            }

            return new ImportFhirCall( fhirServer, importData );
        } catch (IOException e) {
            throw new FhirProxyException(e);
        }
    }

    @Override
    public String getOperationName() {
        return "$import";
    }


}
