package org.github.philipsonfhir.fhircast.server;

import org.github.philipsonfhir.fhircast.WorklistServer;
import org.github.philipsonfhir.fhircast.app.FhirCastWebsubClient;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorklistServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class FhirCastWebsubClientDataTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort private long port;


    @Test
    public void createDeleteSession() {
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), "34923849238" );
        fhirCastDrivingApplication.logout();
    }

    @Test
    public void createDeleteMultipleSession()  {
        String sessionId = "mysession";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient2 = new FhirCastWebsubClient( baseUrl(), sessionId );
        fhirCastDrivingApplication.logout();
        fhirCastWebsubClient1.logout();
        fhirCastWebsubClient2.logout();
    }

    @Test
    public void patientChange() throws InterruptedException {
        String sessionId = "mysession1";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId );
        fhirCastWebsubClient1.subscribePatientChange();

        assertNull( fhirCastDrivingApplication.getCurrentPatient() );
        assertNull( fhirCastWebsubClient1.getCurrentPatient() );

        Patient patient = new Patient();
        patient.setId( "fhircastPatient" );
        patient.addName( new HumanName().setFamily( "FhirCast1" ) );

        fhirCastDrivingApplication.setCurrentPatient( patient );

        Thread.sleep( 1000 );

        assertEquals( patient.getIdElement().getIdPart(), fhirCastDrivingApplication.getCurrentPatient().getIdElement().getIdPart());
        assertEquals( "Patient/"+patient.getId(),  fhirCastWebsubClient1.getCurrentPatient().getId() );

        fhirCastWebsubClient1.getContext();

        fhirCastDrivingApplication.logout();
        fhirCastWebsubClient1.logout();
    }



    private String baseUrl(){
//        return  "http://localhost:" + port + "/api/fhircast/websub";
        return  "http://localhost:" + port;
    }

}
