package org.github.philipsonfhir.fhirproxy.common.operation;

import org.github.philipsonfhir.fhirproxy.bulkdata.ExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.bulkdata.PatientExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FhirOperationRepository {
    private TreeMap<String, Map<String, FhirResourceInstanceOperation>> fhirResourceInstanceOperationMap = new TreeMap<>(  );
    private TreeMap<String, Map<String, FhirResourceOperation>> fhirResourceOperationMap = new TreeMap<>(  );
    private TreeMap<String, FhirBaseOperation> fhirBaseOperationMap = new TreeMap<>(  );

    public void registerOperation( List<FhirOperation> fhirOperationList) {
        fhirOperationList.forEach( fhirOperation -> this.registerOperation(fhirOperation));
    }

    private void registerOperation(FhirOperation fhirOperation) {
        if( fhirOperation instanceof FhirBaseOperation ){
            this.registerOperation((FhirBaseOperation)fhirOperation);
        } else if( fhirOperation instanceof FhirResourceOperation ){
            this.registerOperation((FhirResourceOperation)fhirOperation);
        }  if( fhirOperation instanceof FhirResourceInstanceOperation ){
            this.registerOperation((FhirResourceInstanceOperation)fhirOperation);
        }
    }

    public void registerOperation(FhirBaseOperation fhirOperation) {
        fhirBaseOperationMap.put( fhirOperation.getOperationName(), fhirOperation );
    }

    public void registerOperation(FhirResourceOperation fhirOperation) {
        Map<String, FhirResourceOperation> map =
                this.fhirResourceOperationMap.get( fhirOperation.getResourceType());
        if ( map==null ){
            map = new TreeMap<>(  );
            this.fhirResourceOperationMap.put( fhirOperation.getResourceType(), map );
        }
        map.put(fhirOperation.getOperationName(),fhirOperation);
    }

    public void registerOperation(FhirResourceInstanceOperation fhirOperation ) {
        Map<String, FhirResourceInstanceOperation> map =
            this.fhirResourceInstanceOperationMap.get( fhirOperation.getResourceType());
        if ( map==null ){
            map = new TreeMap<>(  );
            this.fhirResourceInstanceOperationMap.put( fhirOperation.getResourceType(), map );
        }
        map.put( fhirOperation.getOperationName(), fhirOperation );
    }



//    public FhirOperationCall doGetOperation(FhirServer fhirServer, String resourceType, String resourceId, String operationName, Map<String, String> queryparams) throws FhirProxyException {
//        FhirResourceInstanceOperation operation = getFhirResourceInstanceOperation( resourceType, operationName );
//        if ( operation != null ) {
//            return operation.createGetOperationCall( fhirServer, resourceId, queryparams );
//        }
//        return null;
//    }
//
//    public FhirOperationCall doPostOperation(FhirServer fhirServer, String resourceType, String resourceId, String operationName, IBaseResource parseResource, Map<String, String> queryParams) throws FhirProxyException {
//        FhirResourceInstanceOperation operation = getFhirResourceInstanceOperation( resourceType, operationName );
//        if ( operation != null ) {
//            return operation.createPostOperationCall( fhirServer, resourceId, parseResource, queryParams );
//        }
//        return null;
//    }

    public boolean holdsOperation(FhirRequest fhirRequest) {
        return this.fhirResourceInstanceOperationMap.containsKey(fhirRequest.getOperationName());
    }

    public FhirCall getFhirOperation(FhirServer fhirServer, FhirRequest fhirRequest) throws FhirProxyException {
        FhirOperation fhirOperation = null;
        switch ( fhirRequest.getPartList().size() ){
            case 1:
                fhirOperation = this.fhirBaseOperationMap.get( fhirRequest.getOperationName() );
                break;
            case 2: {
                    Map<String, FhirResourceOperation> map = this.fhirResourceOperationMap.get(fhirRequest.getResourceType());
                    fhirOperation = map.get(fhirRequest.getOperationName());
                }
                break;
            case 3: {
                Map<String, FhirResourceInstanceOperation> map = this.fhirResourceInstanceOperationMap.get(fhirRequest.getResourceType());
                fhirOperation = map.get(fhirRequest.getOperationName());
                }
                break;
        }
        if ( fhirOperation!=null ) {
            return fhirOperation.createFhirCall(fhirServer, fhirRequest);
        } else {
            return null;
        }
    }


}
