package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.github.philipsonfhir.fhirproxy.common.ImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirBaseOperation;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperation;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperationRepository;
import org.hl7.fhir.r4.model.CapabilityStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * https://build.fhir.org/ig/HL7/bulk-data/
 */
public class BulkDataImplementationGuide implements ImplementationGuide {

    public BulkDataImplementationGuide() {

    }

    @Override
    public CapabilityStatement updateCapabilityStatement(CapabilityStatement capabilityStatement) {
        // (enough?)
        capabilityStatement.addInstantiates("http://www.hl7.org/fhir/bulk-data/CapabilityStatement-bulk-data.html");

        capabilityStatement.getRest().stream().forEach( rest ->{
            rest.addOperation( new CapabilityStatement.CapabilityStatementRestResourceOperationComponent()
                    .setName("export")
                    .setDefinition("http://hl7.org/fhir/uv/bulkdata/STU1/OperationDefinition-export.html")
            );
        });
        return capabilityStatement;
    }

    public List<FhirOperation> getOperations() {
        List<FhirOperation> operationList = new ArrayList<>();
        operationList.add( new ExportFhirOperation());
        operationList.add( new PatientExportFhirOperation() );
        operationList.add( new PatientInstanceExportFhirOperation() );
        operationList.add( new GroupInstanceExportFhirOperation() );
        return operationList;
    }
}
