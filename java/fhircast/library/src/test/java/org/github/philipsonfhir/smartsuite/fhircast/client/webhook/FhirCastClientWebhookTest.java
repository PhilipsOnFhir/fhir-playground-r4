package org.github.philipsonfhir.smartsuite.fhircast.client.webhook;

import org.github.philipsonfhir.smartsuite.fhircast.FhirCastServer;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClient;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClientTopic;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastNotificationException;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastWebhookClient;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.WorkflowEventFactory;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@SpringBootTest(classes = FhirCastServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "server.port=8081")
@RunWith(SpringRunner.class)
public class FhirCastClientWebhookTest {
//    private long port = 8080;
    @Value("${local.server.port}")
    int port;

    @Test
    public void webSubTest() throws FhirCastException {
        FhirCastClient fhirCastClient = new FhirCastClient( String.format("http://localhost:%d",port ));

        FhirCastClientTopic fhirCastClientTopic = fhirCastClient.createTopic();

        FhirCastWebhookClient fhirCastWebhookClient1 = fhirCastClientTopic.createWebHookClient() ;
        FhirCastWebhookClient fhirCastWebhookClient2 = fhirCastClientTopic.createWebHookClient() ;

        {
            fhirCastWebhookClient1.subscribeWebsubAll();
            fhirCastWebhookClient2.subscribeWebsubAll();
            fhirCastWebhookClient1.waitForVerfication();
            fhirCastWebhookClient2.waitForVerfication();
        }
        {
            Patient patient = new Patient();
            patient.setId("patient1");
            ContextEvent contextEvent = WorkflowEventFactory.createOpenEvent(patient);
            fhirCastClientTopic.sendEvent(contextEvent);
            fhirCastWebhookClient1.waitForEvent();
            fhirCastWebhookClient2.waitForEvent();

            assertEquals( contextEvent.getId(), fhirCastWebhookClient1.getLastEvent().getId() );
            assertEquals( contextEvent.getId(), fhirCastWebhookClient2.getLastEvent().getId() );
        }
        {
            Patient patient = new Patient();
            patient.setId("patient2");
            ContextEvent contextEvent = WorkflowEventFactory.createOpenEvent(patient);
            fhirCastClientTopic.sendEvent(contextEvent);
            fhirCastWebhookClient1.waitForEvent();
            fhirCastWebhookClient2.waitForEvent();

            assertEquals( contextEvent.getId(), fhirCastWebhookClient1.getLastEvent().getId() );
            assertEquals( contextEvent.getId(), fhirCastWebhookClient2.getLastEvent().getId() );
        }

//        fhirCastWebhookClient1.unsubscribeWebsubAll();
        //        fhirCastWebhookClient2.unsubscribeWebsubAll();
        fhirCastWebhookClient1.resetEventWait();
        fhirCastWebhookClient2.resetEventWait();
        fhirCastClientTopic.logOut();
        fhirCastWebhookClient2.waitForEvent();

        assertEquals( "userlogout", fhirCastWebhookClient1.getLastEvent().getEvent().getHub_event());
        assertEquals( "userlogout", fhirCastWebhookClient2.getLastEvent().getEvent().getHub_event());

    }
    @Test
    public void webSubSyncErrorTest() throws FhirCastException {
        FhirCastClient fhirCastClient = new FhirCastClient( String.format("http://localhost:%d",port ));

        FhirCastClientTopic fhirCastClientTopic = fhirCastClient.createTopic();

        FhirCastWebhookClient fhirCastWebhookClient1 = fhirCastClientTopic.createWebHookClient() ;
        FhirCastWebhookClient fhirCastWebhookClient2 = fhirCastClientTopic.createWebHookClient() ;

        {
            fhirCastWebhookClient1.subscribeWebsubAll();
            fhirCastWebhookClient2.subscribeWebsubAll();
            fhirCastWebhookClient1.waitForVerfication();
            fhirCastWebhookClient2.waitForVerfication();
        }
        {
            fhirCastWebhookClient2.setReceptionErrorMode();
            Patient patient = new Patient();
            patient.setId("patient1");
            ContextEvent contextEvent = WorkflowEventFactory.createOpenEvent(patient);
            try {
                fhirCastClientTopic.sendEvent(contextEvent);
                fail("Exception expected;");
            } catch ( FhirCastNotificationException ignored) {
            }
            fhirCastWebhookClient1.waitForEvent();
            //fhirCastWebhookClient2.waitForEvent();

            assertEquals( contextEvent.getId(), fhirCastWebhookClient1.getLastEvent().getId() );
            assertEquals( "syncerror", fhirCastWebhookClient1.getLastEvent().getEvent().getHub_event() );
//            assertEquals( contextEvent.getId(), fhirCastWebhookClient2.getLastEvent().getId() );

        }

        fhirCastClientTopic.logOut();

    }

    @Test
    public void timeOutTest() throws FhirCastException {
        FhirCastClient fhirCastClient = new FhirCastClient( String.format("http://localhost:%d",port ));
        FhirCastClientTopic fhirCastClientTopic = fhirCastClient.createTopic();
        FhirCastWebhookClient fhirCastWebhookClient = fhirCastClientTopic.createWebHookClient() ;
        fhirCastWebhookClient.subscribeWebsubAll(1);

        fhirCastWebhookClient.waitForDenial();

    }
}
