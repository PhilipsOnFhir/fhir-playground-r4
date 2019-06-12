package org.github.philipsonfhir.smartonfhir.basic.controller.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class FhirServerTest {

    @Test
    public void getCapabilityStatementFromTestServer() {
        FhirServer fhirServer = new FhirServer("http://localhost:9504/hapi-fhir-jpaserver/fhir/");
        assertNotNull(fhirServer.getCapabilityStatement());
    }
}