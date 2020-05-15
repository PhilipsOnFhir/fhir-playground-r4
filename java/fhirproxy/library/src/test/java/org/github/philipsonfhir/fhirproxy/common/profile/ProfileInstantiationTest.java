package org.github.philipsonfhir.fhirproxy.common.profile;

import ca.uhn.fhir.context.FhirContext;
import junit.framework.TestCase;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirValueSetter;
import org.github.philipsonfhir.fhirproxy.testutil.TestServer;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ProfileInstantiationTest {

    @Test
    public void testBloodpressureUpdate() throws NoSuchMethodException, InstantiationException, FhirProxyException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        InputStream nbpip = ProfileInstantiationTest.class.getResourceAsStream("/examples/fhir-shorthand/bp-profile.json");
        FhirContext ourCtx = FhirContext.forR4();
        StructureDefinition bpProfile = (StructureDefinition) ourCtx.newJsonParser().parseResource(nbpip);

        Base base  = ProfileInstantiator.instantiateProfile(bpProfile);
        assertTrue( base instanceof Observation );
        Observation obs = (Observation)base;

        FhirValueSetter.setProperty( obs, "status", new CodeType("final"));
        FhirValueSetter.setProperty( obs, "component[0].code.coding", new Coding().setSystem("http://loinc.org").setCode("8480-6"));
        FhirValueSetter.setProperty( obs, "component[0].valueQuantity.value", "100");
        FhirValueSetter.setProperty( obs, "component[1].code.coding", new Coding().setSystem("http://loinc.org").setCode("8462-4"));
        FhirValueSetter.setProperty( obs, "component[1].value as Quantity.value", "110");

        TestCase.assertEquals(2,  obs.getComponent().size() );

//        Base newBase = ProfileInstantiator.updateBasedOnProfile( (new TestServer()).getFhirServer(), obs, bpProfile );
        Base newBase = ProfileInstantiator.updateBasedOnProfile2( (new TestServer()).getFhirServer(), obs, bpProfile );

        assertTrue( newBase instanceof  Observation );
        Observation obs2 = (Observation) newBase;
        assertEquals( "final", obs2.getStatus().toCode() );
        assertEquals( "mm[Hg]", ((Quantity)obs2.getComponent().get(1).getValue()).getCode());
    }

    @Test
    public void testBloodpressureInstantiation() throws NoSuchMethodException, InstantiationException, FhirProxyException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        InputStream nbpip = ProfileInstantiationTest.class.getResourceAsStream("/examples/fhir-shorthand/bp-profile.json");
        FhirContext ourCtx = FhirContext.forR4();
        StructureDefinition bpProfile = (StructureDefinition) ourCtx.newJsonParser().parseResource(nbpip);
        Base base  = ProfileInstantiator.instantiateProfile(bpProfile);
        assertTrue( base instanceof Observation );
        Observation obs = (Observation)base;

        FhirValueSetter.setProperty( obs, "component[0].code.coding", new Coding().setSystem("http://loinc.org").setCode("8480-6"));
        FhirValueSetter.setProperty( obs, "component[0].valueQuantity.value", "100");
        FhirValueSetter.setProperty( obs, "component[1].code.coding", new Coding().setSystem("http://loinc.org").setCode("8462-4"));
        FhirValueSetter.setProperty( obs, "component[1].(value as Quantity).value", "110");

        TestCase.assertEquals(2,  obs.getComponent().size() );
    }

}
