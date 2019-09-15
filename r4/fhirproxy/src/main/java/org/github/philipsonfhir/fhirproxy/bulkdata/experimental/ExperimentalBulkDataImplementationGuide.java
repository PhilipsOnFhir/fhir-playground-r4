package org.github.philipsonfhir.fhirproxy.bulkdata.experimental;

import org.github.philipsonfhir.fhirproxy.bulkdata.ExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.bulkdata.GroupInstanceExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.bulkdata.PatientExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.bulkdata.PatientInstanceExportFhirOperation;
import org.github.philipsonfhir.fhirproxy.bulkdata.experimental.ImportFhirOperation;
import org.github.philipsonfhir.fhirproxy.common.ImplementationGuide;
import org.github.philipsonfhir.fhirproxy.common.operation.FhirOperation;
import org.hl7.fhir.r4.model.CapabilityStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * https://build.fhir.org/ig/HL7/bulk-data/
 */
public class ExperimentalBulkDataImplementationGuide implements ImplementationGuide {

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
        operationList.add( new ImportFhirOperation() );
        return operationList;
    }
}
