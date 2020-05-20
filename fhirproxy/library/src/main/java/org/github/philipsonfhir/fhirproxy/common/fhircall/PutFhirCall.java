package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutFhirCall implements FhirCall {
    private final IFhirServer fhirServer;
    private final FhirRequest fhirRequest;
    private IBaseResource result = null;
    private Map<String, OperationOutcome> errorMap = new HashMap<>();

    public PutFhirCall(IFhirServer fhirServer, FhirRequest fhirRequest) {
        this.fhirServer  = fhirServer;
        this.fhirRequest = fhirRequest;
    }

    @Override
    public void execute() throws FhirProxyException {
        List<String> partList = fhirRequest.getPartList();
        switch( partList.size() ){
            case 2:
            case 1:
            case 0:
                result =fhirServer.doPut((Resource) fhirRequest.getBodyResource());
                break;
            default:
                errorMap.put(fhirRequest.getResourceType(), new OperationOutcome()
                        .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                                .setSeverity( OperationOutcome.IssueSeverity.ERROR)
                                .setDiagnostics( "PUT with "+partList.size()+" parameters is not supported")
                        )
                );
                break;
        }

    }

    @Override
    public String getStatusDescription() {
        return (this.result!=null?"Done":"Processing");
    }

    @Override
    public IBaseResource getResource() {
        return result;
    }

    @Override
    public IFhirServer getFhirServer() {
        return this.fhirServer;
    }

    @Override
    public Map<String, OperationOutcome> getErrors() {
        return Collections.unmodifiableMap(this.errorMap);
    }

    public IBaseResource updateResponse(HttpServletResponse resp) {
        if ( this.fhirRequest.isOperationCall()){
            resp.setStatus( HttpStatus.OK.value() );
            return result;
        } else {
            resp.setStatus(HttpStatus.CREATED.value());
            Resource resource = (Resource)result;
            String location = this.fhirRequest.getBase()+"/"+resource.getResourceType()+"/"+resource.getIdElement().getIdPart();
            resp.addHeader("Location", location);
            return null;
        }
    }
}
