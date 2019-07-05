package org.github.philipsonfhir.smartonfhir.basic.controller.service;

import org.github.philipsonfhir.fhircast.WorklistServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorklistServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FhirServerTest {
    @LocalServerPort
    private long port;

    @Test
    public void getCapabilityStatementFromTestServer() {
        FhirServer fhirServer = new FhirServer("http://localhost:9404/hapi-fhir-jpaserver/fhir/");
        assertNotNull(fhirServer.getCapabilityStatement());
    }
}
