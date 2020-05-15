package org.github.philipsonfhir.fhirproxy.common.expression;


import com.google.common.io.Resources;
import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.testutil.TestDataBuilder;
import org.github.philipsonfhir.fhirproxy.testutil.TestServer;
import org.hl7.fhir.r4.hapi.ctx.MyHapiWorkerContext;
import org.hl7.fhir.r4.hapi.validation.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.MyFHIRPathEngine;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ExpressionProcessorTest {

    @Test
    public void testUnitConversions() throws IOException, UcumException {
        TestServer testServer = new TestServer();
        IFhirServer fhirServer = testServer.getFhirServer();
        MyHapiWorkerContext hapiWorkerContext = new MyHapiWorkerContext( fhirServer.getCtx(), new PrePopulatedValidationSupport() );
        MyFHIRPathEngine fhirPathEngine = new MyFHIRPathEngine(hapiWorkerContext);
        ExpressionProcessor.HostServices hostServices = new ExpressionProcessor.HostServices();
        fhirPathEngine.setHostServices( hostServices );
        org.fhir.ucum.UcumEssenceService ucumEssenceService = new org.fhir.ucum.UcumEssenceService( Resources.getResource("ucum-essence.xml").openStream() );
        hapiWorkerContext.setUcumService( ucumEssenceService );

        Observation observation = new Observation();
        Quantity quantity = new Quantity();
        quantity.setValue(195).setUnit("cm");
        observation.setValue( quantity );
        List<Base> result = fhirPathEngine.evaluate(observation, "value");
        fhirPathEngine.evaluate(observation, "1 'cm' * 1 'm'");
        fhirPathEngine.evaluate(observation, "1 'cm' - 1 'm'");
        fhirPathEngine.evaluate(observation, "1 'cm' + 1 'm'");
        {
            List<Base> a = fhirPathEngine.evaluate(observation, " 75 'kg' / ( 1.8 'm' * 180 'cm')");
            Quantity q = (Quantity) a.get(0);
            Decimal dec = ucumEssenceService.convert(new Decimal(q.getValue().toString()), q.getCode(), "kg/m2");
            assertEquals(23.148, Double.parseDouble(dec.asScientific()), 0.01);
        }

        {   // Body surface area (the Mosteller formula), m2 = [ Height, cm x Weight, kg  / 3600 ]1/2
            List<Base> a = fhirPathEngine.evaluate(observation, "((90 'kg'.value  * 160 'cm'.value)/3600).sqrt() ");
            assertEquals( 1, a.size() );
            assertTrue( a.get(0) instanceof DecimalType );
            assertEquals( 2, ((DecimalType)a.get(0)).getValue().doubleValue(), 0.05 );

        }
        {   // Body surface area (the Mosteller formula), m2 = [ Height, cm x Weight, kg  / 3600 ]1/2
            List<Base> a = fhirPathEngine.evaluate(observation, "((90 'kg'.value  * 1.60 'm'.toQuantity('cm').value)/3600).sqrt() ");
            assertEquals( 1, a.size() );
            assertTrue( a.get(0) instanceof DecimalType );
            assertEquals( 2, ((DecimalType)a.get(0)).getValue().doubleValue(), 0.05 );

        }
    }

    @Test
    public void testUcum() throws IOException, UcumException {
        UcumEssenceService ucumEssenceService = new UcumEssenceService( Resources.getResource("ucum-essence.xml").openStream() );
        Pair result1 = ucumEssenceService.multiply(new Pair(new Decimal("1"), "cm"), new Pair(new Decimal("1"), "m"));
        Pair result2 = ucumEssenceService.multiply(new Pair(new Decimal("195"), "cm"), new Pair(new Decimal("0.95"), "kg"));
        ucumEssenceService.convert( result2.getValue(), result2.getCode(), "kg.m");


    }

    @Test
    public void testVariables() throws FhirProxyException {
        Patient testPatient = TestDataBuilder.createPatient( "Common-expressionprocessor-Pa-1", "1964-06-12");
        TestServer testServer = new TestServer();
//        testServer.putResource(testPatient);

        ExpressionProcessor expressionProcessor = new ExpressionProcessor(testServer.getFhirServer());
        expressionProcessor.setBase(new ActivityDefinition());
        expressionProcessor.updateContext("p", testPatient );
        Object result = expressionProcessor.handleListResult( expressionProcessor.evaluateFhirPath( "%p"));
        assertNotNull( result );
        assertTrue( result instanceof Patient );
        Patient resultPatient = (Patient)result;
        assertEquals(testPatient,resultPatient);

    }
}
