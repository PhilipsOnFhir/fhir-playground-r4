package com.github.philipsonfhir.fhircast;

import com.github.philipsonfhir.fhircast.app.FhirCastWebsubClient;
import com.github.philipsonfhir.fhircast.server.FhirCastServerApplication;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.server.websub.model.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.server.websub.model.FhircastEventType;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FhirCastServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
public class FhirCastWebsubClientTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort private long port;


    @Test
    public void createDeleteSession() throws DeploymentException, IOException, URISyntaxException {
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), "34923849238" );
        fhirCastDrivingApplication.logout();
    }

    @Test
    public void createDeleteMultipleSession() throws DeploymentException, IOException, URISyntaxException {
        String sessionId = "mysession";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient2 = new FhirCastWebsubClient( baseUrl(), sessionId );
        fhirCastDrivingApplication.logout();
        fhirCastWebsubClient1.logout();
        fhirCastWebsubClient2.logout();
    }

    @Test
    public void patientChangeRest() throws InterruptedException, FhirCastException, DeploymentException, IOException, URISyntaxException {
        String sessionId = "mysession1";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId );
        fhirCastWebsubClient1.subscribePatientChange();

        assertNull( fhirCastDrivingApplication.getCurrentPatient() );
        assertNull( fhirCastWebsubClient1.getCurrentPatient() );

        Patient patient1 = (Patient) new Patient()
                .addName(new HumanName().setFamily("FhirCast1"))
                .setId("fhircastPatient1");
        newPatient( fhirCastDrivingApplication, fhirCastWebsubClient1, patient1 );

        fhirCastWebsubClient1.clearEvents();
        Patient patient2 = (Patient) new Patient()
                .addName(new HumanName().setFamily("FhirCast1"))
                .setId("fhircastPatient2");
        newPatient( fhirCastDrivingApplication, fhirCastWebsubClient1, patient2 );

        fhirCastWebsubClient1.clearEvents();

        fhirCastDrivingApplication.logout();
        assertEquals( 3 , fhirCastWebsubClient1.getEvents().size() );
        assertEquals( 2, fhirCastWebsubClient1.getEvents().stream().filter( event -> event.getEvent().getHub_event() == FhircastEventType.CLOSE_PATIENT_CHART).collect(Collectors.toList()).size());
        assertEquals( 1, fhirCastWebsubClient1.getEvents().stream().filter( event -> event.getEvent().getHub_event() == FhircastEventType.USER_LOGOUT).collect(Collectors.toList()).size());

//        fhirCastWebsubClient1.logout();
    }

    @Test
    public void patientChangeWebsocket() throws Exception, FhirCastException {
        String sessionId = "mysession2";
        FhirCastWebsubClient fhirCastDrivingApplication = new FhirCastWebsubClient( baseUrl(), sessionId, false  );
        FhirCastWebsubClient fhirCastWebsubClient1 = new FhirCastWebsubClient( baseUrl(), sessionId, false );
        fhirCastWebsubClient1.subscribePatientChange();

        assertNull( fhirCastDrivingApplication.getCurrentPatient() );
        assertNull( fhirCastWebsubClient1.getCurrentPatient() );

        System.out.println(" WAITING --------------");
        for ( int i=0; i<10; i++ ) {
            Thread.sleep(1000);
            System.out.println(" WAITING " + i);
        }
        System.out.println(" DONE --------------");

        Patient patient1 = (Patient) new Patient()
                .addName(new HumanName().setFamily("FhirCast1"))
                .setId("fhircastPatient1ws");
        newPatient( fhirCastDrivingApplication, fhirCastWebsubClient1, patient1 );

        fhirCastWebsubClient1.clearEvents();
        Patient patient2 = (Patient) new Patient()
                .addName(new HumanName().setFamily("FhirCast1"))
                .setId("fhircastPatient2ws");
        newPatient( fhirCastDrivingApplication, fhirCastWebsubClient1, patient2 );

        fhirCastWebsubClient1.clearEvents();

        fhirCastDrivingApplication.logout();
        assertEquals( 3 , fhirCastWebsubClient1.getEvents().size() );
        assertEquals( 2, fhirCastWebsubClient1.getEvents().stream().filter( event -> event.getEvent().getHub_event() == FhircastEventType.CLOSE_PATIENT_CHART).collect(Collectors.toList()).size());
        assertEquals( 1, fhirCastWebsubClient1.getEvents().stream().filter( event -> event.getEvent().getHub_event() == FhircastEventType.USER_LOGOUT).collect(Collectors.toList()).size());

//        fhirCastWebsubClient1.logout();
    }

    private void newPatient( FhirCastWebsubClient fhirCastDrivingApplication, FhirCastWebsubClient fhirCastWebsubClient, Patient patient) throws InterruptedException, FhirCastException {
        fhirCastDrivingApplication.setCurrentPatient(patient);

        System.out.println(" WAITING --------------");
        for ( int i=0; i<10; i++ ) {
            Thread.sleep(1000);
            System.out.println(" WAITING " + i);
        }
        System.out.println(" DONE --------------");
        assertEquals(patient.getIdElement().getIdPart(), fhirCastDrivingApplication.getCurrentPatient().getIdElement().getIdPart());
        assertEquals("Patient/" + patient.getId(), fhirCastWebsubClient.getCurrentPatient().getId());

        assertEquals( 1, fhirCastWebsubClient.getEvents().size());
        FhirCastWorkflowEvent fhirCastWorkflowEvent = fhirCastWebsubClient.getEvents().get(0);
        assertNotNull( fhirCastWorkflowEvent );
        assertNotNull( fhirCastWorkflowEvent.getEvent() );
        assertNotNull( fhirCastWorkflowEvent.getEvent().getContext() );
        Patient newPatient = fhirCastWorkflowEvent.getEvent().retrievePatientFromContext();
        assertNotNull( newPatient );
        assertEquals( "Patient/" + patient.getId(), newPatient.getId() );
    }


    private String baseUrl(){
        return  "http://localhost:" + port + "/fhircast/websub";
    }

}