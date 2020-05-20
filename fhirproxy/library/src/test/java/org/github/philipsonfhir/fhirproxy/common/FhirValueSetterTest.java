package org.github.philipsonfhir.fhirproxy.common;


import org.hl7.fhir.r4.model.*;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class FhirValueSetterTest {

    @Test
    public void testBaseValue(){
        RequestGroup requestGroup = (RequestGroup) new RequestGroup()
                .setStatus( RequestGroup.RequestStatus.ACTIVE)
                .setId("someId");
        assertTrue( FhirValueSetter.getBaseValue( requestGroup ) instanceof  RequestGroup );
        assertTrue( FhirValueSetter.getBaseValue( "hello" ) instanceof StringType);
        assertTrue( FhirValueSetter.getBaseValue( true ) instanceof BooleanType);
        assertTrue( FhirValueSetter.getBaseValue( 242 ) instanceof IntegerType);
        assertTrue( FhirValueSetter.getBaseValue( 242.0 ) instanceof DecimalType );
        assertTrue( FhirValueSetter.getBaseValue( ((float)242.0) ) instanceof DecimalType );
    }

    @Test
    public void testSetValue() throws FhirProxyException {
        RequestGroup requestGroup = (RequestGroup) new RequestGroup()
                .setStatus( RequestGroup.RequestStatus.ACTIVE)
                .setId("someId");

        FhirValueSetter.setProperty( requestGroup, "id", new IdType("hello"));
        assertEquals( "hello", requestGroup.getId() );

        FhirValueSetter.setProperty( requestGroup, "status", new StringType("draft"));
        assertEquals(  RequestGroup.RequestStatus.DRAFT, requestGroup.getStatus() );

        FhirValueSetter.setProperty( requestGroup, "action.description", new StringType("description"));
        assertEquals(  "description", requestGroup.getActionFirstRep().getDescription() );

        FhirValueSetter.setProperty( requestGroup, "action.description", new StringType("description1"));
        assertEquals(  "description1", requestGroup.getActionFirstRep().getDescription() );
        assertEquals( 1, requestGroup.getAction().size() );

        FhirValueSetter.setProperty( requestGroup, "action[0].description", new StringType("description2"));
        assertEquals(  "description2", requestGroup.getActionFirstRep().getDescription() );
        assertEquals( 1, requestGroup.getAction().size() );


    }

    @Test
    public void testArraysLargerThan2AndValueX() throws FhirProxyException {

        Observation obs = new Observation();
        FhirValueSetter.setProperty( obs, "component[0].code.coding", new Coding().setSystem("http://loinc.org").setCode("8480-6"));
        FhirValueSetter.setProperty( obs, "component[0].valueQuantity.value", "100");
        FhirValueSetter.setProperty( obs, "component[1].code.coding", new Coding().setSystem("http://loinc.org").setCode("8462-4"));
        FhirValueSetter.setProperty( obs, "component[1].valueQuantity.value", "110");
        FhirValueSetter.setProperty( obs, "component[1].valueQuantity.unit", "mmHg");

        assertEquals(2, obs.getComponent().size());
        assertEquals( "8480-6", obs.getComponent().get(0).getCode().getCoding().get(0).getCode());
        assertEquals( "8462-4", obs.getComponent().get(1).getCode().getCoding().get(0).getCode());
    }

    @Test
    public void testAsFunction() throws FhirProxyException {

        Observation obs = new Observation();
        FhirValueSetter.setProperty( obs, "value as Quantity.value", "100");
        FhirValueSetter.setProperty( obs, "value as Quantity.unit", "mmHg");
        FhirValueSetter.setProperty( obs, "(value as Quantity).value", "100");


    }

    @Test
    public void testSetArrayValue() throws FhirProxyException {
        RequestGroup requestGroup = (RequestGroup) new RequestGroup()
                .setStatus( RequestGroup.RequestStatus.ACTIVE)
                .setId("someId");

        FhirValueSetter.setProperty( requestGroup, "action.description", new StringType("description"));
        assertEquals( 1, requestGroup.getAction().size() );
        assertEquals(  "description", requestGroup.getActionFirstRep().getDescription() );

        FhirValueSetter.setProperty( requestGroup, "action[0].description", new StringType("description2"));
        assertEquals(  "description2", requestGroup.getActionFirstRep().getDescription() );
        assertEquals( 1, requestGroup.getAction().size() );

        requestGroup.addAction(new RequestGroup.RequestGroupActionComponent()
                .setDescription("newAction")
        );

        FhirValueSetter.setProperty( requestGroup, "action[1].description", new StringType("description2"));
        assertEquals( 2, requestGroup.getAction().size() );
        assertEquals(  "description2", requestGroup.getAction().get(1).getDescription() );

    }

    @Test
    public void testFunctions() throws FhirProxyException {
        RequestGroup requestGroup = (RequestGroup) new RequestGroup()
                .setStatus( RequestGroup.RequestStatus.ACTIVE)
                .setId("someId");

        // TODO
        try {
            FhirValueSetter.setProperty( requestGroup, "action.resource.resolve().id", new StringType("description"));
            fail();
        } catch (FhirProxyException e ){}

        try {
            FhirValueSetter.setProperty( requestGroup, "extension('http:testurl').value", new StringType("description"));
            fail();
        } catch (FhirProxyException e ){}

        try {
            Observation observation = new Observation()
                    .setValue( new StringType("1"));
            FhirValueSetter.setProperty( observation, "value.ofType(Quantity)", new StringType("description"));
            fail();
        } catch (FhirProxyException e ){}

    }
////    // TODO use FHIR path to find object. As FhirPath can be used... currently failing on property only working on element one level up.
//    @Test
//    public void fhirPathBasedSettter() throws IOException, UcumException {
////        TestServer testServer = new TestServer();
//        MyHapiWorkerContext hapiWorkerContext = new MyHapiWorkerContext( FhirContext.forR4(), new PrePopulatedValidationSupport() );
//
//        UcumEssenceService ucumEssenceService = new UcumEssenceService( Resources.getResource("ucum-essence.xml").openStream() );
//        hapiWorkerContext.setUcumService( ucumEssenceService );
//
//        MyFHIRPathEngine fhirPathEngine = new MyFHIRPathEngine(hapiWorkerContext);
//
//        RequestGroup requestGroup = (RequestGroup) new RequestGroup()
//                .setStatus( RequestGroup.RequestStatus.ACTIVE)
//
//                .setId("someId");
//
//        fhirPathEngine.evaluate( requestGroup, "status");
//
//
//        ((Enumeration)fhirPathEngine.evaluate( requestGroup, "status").get(0)).getCode();
//        fhirPathEngine.evaluate( requestGroup, "status").get(0).getClass();
//        fhirPathEngine.evaluate( requestGroup, "status").get(0);
//        Enumeration a = ((Enumeration)fhirPathEngine.evaluate( requestGroup, "status").get(0));
//        a.setValue( FhirValueSetter.getBaseValue("draft"));
//
//    }
}
