package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirResourceInstanceOperation;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirResourceOperation;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;

public class GroupInstanceExportFhirOperation implements FhirResourceInstanceOperation{


    public FhirCall createFhirCall(FhirServer fhirServer, FhirRequest fhirRequest) throws FhirProxyException {
        BulkdataParameters bulkdataParameters = new BulkdataParameters( fhirRequest );
        return new GroupInstanceExportFhirCall( fhirServer, fhirRequest.getResourceId(), bulkdataParameters.getOutputFormat(), bulkdataParameters.getSince(), bulkdataParameters.getType());
    }

    @Override
    public String getOperationName() {
        return "$export";
    }

    @Override
    public String getResourceType() {
        return "Group";
    }
}
