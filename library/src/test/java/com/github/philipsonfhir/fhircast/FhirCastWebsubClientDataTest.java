package com.github.philipsonfhir.fhircast;

import com.github.philipsonfhir.fhircast.app.FhirCastWebsubClient;
import com.github.philipsonfhir.fhircast.server.FhirCastServerApplication;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FhirCastServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class FhirCastWebsubClientDataTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort private long port;


    @Test
    public void createDeleteSession() throws FhirCastException {
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), "34923849238" );
        fhirCastDrivingApplication.close();
    }

    @Test
    public void createDeleteMultipleSession() throws FhirCastException {
        String sessionId = "mysession";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient2 = new FhirCastWebsubClient( baseUrl(), sessionId );
        fhirCastDrivingApplication.close();
        fhirCastWebsubClient1.close();
        fhirCastWebsubClient2.close();
    }

    @Test
    public void patientChange() throws FhirCastException, InterruptedException {
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

        assertEquals( patient.getId(), fhirCastDrivingApplication.getCurrentPatient().getId() );
        assertEquals( "Patient/"+patient.getId(),  fhirCastWebsubClient1.getCurrentPatient().getId() );

        fhirCastWebsubClient1.getContext();

        fhirCastDrivingApplication.close();
        fhirCastWebsubClient1.close();
    }



    private String baseUrl(){
        return  "http://localhost:" + port + "/fhircast";
    }

}