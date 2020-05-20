//package org.github.philipsonfhir.fhirproxy.common;
//
//import org.github.philipsonfhir.fhirproxy.common.operation.FhirResourceInstanceOperation;
//import org.hl7.fhir.r4.model.OperationDefinition;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class FhirResourceInstanceOperationTest {
//
//    @Test
//    public void testCreateFromOperationDefinition() throws FhirProxyNotImplementedException {
//        FhirResourceInstanceOperation fhirResourceInstanceOperation1 = new FhirResourceInstanceOperation( "resourceType", "operationName");
//        OperationDefinition operationDefinition = fhirResourceInstanceOperation1.getOperationDefinition();
//        assertEquals( "resourceType", operationDefinition.getResource().get(0).getCode() );
//        assertEquals( "operationName", operationDefinition.getName() );
//
//        FhirResourceInstanceOperation fhirResourceInstanceOperation2 = new FhirResourceInstanceOperation( operationDefinition );
//        assertEquals( "resourceType", fhirResourceInstanceOperation2.getOperationDefinition().getResource().get(0).getCode() );
//        assertEquals( "operationName", fhirResourceInstanceOperation2.getOperationDefinition().getName() );
//
//    }
//
//}
