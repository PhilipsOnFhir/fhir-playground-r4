package org.github.philipsonfhir.fhirproxy.common.util;

import org.hl7.fhir.r4.model.Reference;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceUtilTest {

    @Test
    public void test(){
        {
            Reference reference = new Reference().setReference("http://something.com");
            ReferenceUtil.ParsedReference parsedReference = ReferenceUtil.parseReference(reference);
            assertTrue( parsedReference.isUrl() );
            assertEquals(null, parsedReference.getResourceType());
            assertEquals(null, parsedReference.getResourceId());
            assertEquals(null, parsedReference.getVersion());
        }
        {
            Reference reference = new Reference().setReference("Patient");
            ReferenceUtil.ParsedReference parsedReference = ReferenceUtil.parseReference(reference);
            assertFalse( parsedReference.isUrl() );
            assertEquals("Patient", parsedReference.getResourceType());
            assertEquals(null, parsedReference.getResourceId());
            assertEquals(null, parsedReference.getVersion());
        }
        {
            Reference reference = new Reference().setReference("Patient/1");
            ReferenceUtil.ParsedReference parsedReference = ReferenceUtil.parseReference(reference);
            assertFalse( parsedReference.isUrl() );
            assertEquals("Patient", parsedReference.getResourceType());
            assertEquals("1", parsedReference.getResourceId());
            assertEquals(null, parsedReference.getVersion());
        }
        {
            Reference reference = new Reference().setReference("Patient/1/83247");
            ReferenceUtil.ParsedReference parsedReference = ReferenceUtil.parseReference(reference);
            assertFalse( parsedReference.isUrl() );
            assertEquals("Patient", parsedReference.getResourceType());
            assertEquals("1", parsedReference.getResourceId());
            assertEquals("83247", parsedReference.getVersion());
        }
    }

}
