package org.github.philipsonfhir.fhirproxy.memoryserver;


import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryFhirServerTest {
    @Test
    public void testPutPostSearchResource() throws FhirProxyException {
        MemoryFhirServer memoryFhirServer = new MemoryFhirServer();

        {
            IBaseResource iBaseResource = memoryFhirServer.doGet(ResourceType.Patient.name());
            assertTrue(iBaseResource instanceof Bundle);
            Bundle bundle = (Bundle) iBaseResource;
            assertEquals(0, bundle.getEntry().size());
        }

        Patient patient1 = (Patient) new Patient().addName( new HumanName().setFamily("family")).setId("someId");
        memoryFhirServer.doPut( patient1 );
        {
            IBaseResource iBaseResource = memoryFhirServer.doGet(ResourceType.Patient.name());
            assertTrue(iBaseResource instanceof Bundle);
            Bundle bundle = (Bundle) iBaseResource;
            assertEquals(1, bundle.getEntry().size());
        }
        Patient patient2 = (Patient) new Patient().addName( new HumanName().setFamily("family")).setId("someOtherId");
        memoryFhirServer.doPost( patient2 );
        {
            IBaseResource iBaseResource = memoryFhirServer.doGet(ResourceType.Patient.name());
            assertTrue(iBaseResource instanceof Bundle);
            Bundle bundle = (Bundle) iBaseResource;
            assertEquals(2, bundle.getEntry().size());
        }
    }

    @Test
    public void testFamilyNameSearch() throws FhirProxyException {
        MemoryFhirServer memoryFhirServer = new MemoryFhirServer();

        Patient patient1 = (Patient) new Patient().addName( new HumanName().setFamily("familyName")).setId("someId");
        memoryFhirServer.doPost( patient1 );
        Patient patient2 = (Patient) new Patient().addName( new HumanName().setFamily("OtherFamily")).setId("someId");
        memoryFhirServer.doPost( patient2 );

        HashMap<String,String> map = new HashMap<>();
        map.put("family","familyName");
        IBaseResource iBaseResource = memoryFhirServer.doGet(ResourceType.Patient.name(),map);
        assertTrue(iBaseResource instanceof Bundle);
        Bundle bundle = (Bundle) iBaseResource;
        assertEquals(1, bundle.getEntry().size());
        assertEquals( ((Patient)bundle.getEntry().get(0).getResource()).getName().get(0).getFamily(), "familyName");

    }
}
