package org.github.philipsonfhir.fhirproxy.common.fhircall;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhirproxy.FhirProxyApplication;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FhirProxyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForwardServerFhirCallTest {
    @LocalServerPort private long port;
    private static String fhirServerUrl = "http://localhost:9404/hapi-fhir-jpaserver/fhir/";

    @Test
    public void doGetTests(){
        FhirContext ourCtx = FhirContext.forR4();
        ourCtx.getRestfulClientFactory().setSocketTimeout(120 * 1000);
        IGenericClient proxyClient = ourCtx.newRestfulGenericClient("http://localhost:"+port+"/fhirservlet");
        IGenericClient srcClient = ourCtx.newRestfulGenericClient(fhirServerUrl);

        CapabilityStatement capabilityStatement = proxyClient.capabilities().ofType(CapabilityStatement.class).execute();

        Bundle srcPatients = (Bundle) srcClient.search().forResource(Patient.class).execute();
        Bundle patients = (Bundle) proxyClient.search().forResource(Patient.class).execute();
        assertEquals( srcPatients.getEntry().size(), patients.getEntry().size());
    }

}
