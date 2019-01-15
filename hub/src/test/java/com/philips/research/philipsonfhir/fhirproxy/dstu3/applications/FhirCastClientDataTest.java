package com.philips.research.philipsonfhir.fhirproxy.dstu3.applications;

import com.philips.research.philipsonfhir.fhircast.server.FhirCastServerApplication;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.applications.fhircast.app.service.FhirCastClient;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.service.FhirCastException;
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
public class FhirCastClientDataTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort private long port;


    @Test
    public void createDeleteSession() throws FhirCastException {
        FhirCastClient fhirCastDrivingApplication = new FhirCastClient( baseUrl() );
        fhirCastDrivingApplication.close();
    }

    @Test
    public void createDeleteMultipleSession() throws FhirCastException {
        String sessionId = "mysession";
        FhirCastClient fhirCastDrivingApplication = new FhirCastClient( baseUrl(), sessionId );
        FhirCastClient fhirCastClient1 = new FhirCastClient( baseUrl(), sessionId );
        FhirCastClient fhirCastClient2 = new FhirCastClient( baseUrl(), sessionId );
        fhirCastDrivingApplication.close();
        fhirCastClient1.close();
        fhirCastClient2.close();
    }

    @Test
    public void patientChange() throws FhirCastException, InterruptedException {
        String sessionId = "mysession1";
        FhirCastClient fhirCastDrivingApplication = new FhirCastClient( baseUrl(), sessionId );
        FhirCastClient fhirCastClient1 = new FhirCastClient( baseUrl(), sessionId );
        fhirCastClient1.subscribePatientChange();

        assertNull( fhirCastDrivingApplication.getCurrentPatient() );
        assertNull( fhirCastClient1.getCurrentPatient() );

        Patient patient = new Patient();
        patient.setId( "fhircastPatient" );
        patient.addName( new HumanName().setFamily( "FhirCast1" ) );

        fhirCastDrivingApplication.setCurrentPatient( patient );

        Thread.sleep( 1000 );

        assertEquals( patient.getId(), fhirCastDrivingApplication.getCurrentPatient().getId() );
        assertEquals( "Patient/"+patient.getId(),  fhirCastClient1.getCurrentPatient().getId() );

        fhirCastClient1.getContext();

        fhirCastDrivingApplication.close();
        fhirCastClient1.close();
    }



    private String baseUrl(){
        return  "http://localhost:" + port + "/fhircast";
    }

}