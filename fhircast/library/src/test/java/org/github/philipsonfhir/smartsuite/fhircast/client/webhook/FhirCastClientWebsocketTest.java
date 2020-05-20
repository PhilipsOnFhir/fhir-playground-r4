package org.github.philipsonfhir.smartsuite.fhircast.client.webhook;

import org.github.philipsonfhir.smartsuite.fhircast.FhirCastServer;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClient;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastClientTopic;
import org.github.philipsonfhir.smartsuite.fhircast.client.FhirCastWebsocketClient;
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

@SpringBootTest(classes = FhirCastServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = "server.port=8082")
@RunWith(SpringRunner.class)
public class FhirCastClientWebsocketTest {
    @Value("${local.server.port}")
    int port;

    @Test
    public void webSocketTest() throws FhirCastException {
        FhirCastClient fhirCastClient = new FhirCastClient( String.format("http://localhost:%d",port ));

        FhirCastClientTopic fhirCastClientTopic = fhirCastClient.createTopic();

        FhirCastWebsocketClient fhirCastClient1 = fhirCastClientTopic.createWebSocketClient() ;
        FhirCastWebsocketClient fhirCastClient2 = fhirCastClientTopic.createWebSocketClient() ;

        {
            fhirCastClient1.subscribeWebsubAll();
            fhirCastClient2.subscribeWebsubAll();
//            fhirCastClient1.waitForVerfication();
//            fhirCastClient2.waitForVerfication();
        }
        {
            Patient patient = new Patient();
            patient.setId("patient1");
            fhirCastClient1.resetEventWait();
            fhirCastClient2.resetEventWait();
            ContextEvent contextEvent = WorkflowEventFactory.createOpenEvent(patient);
            fhirCastClientTopic.sendEvent(contextEvent);
            fhirCastClient1.waitForEvent();
            fhirCastClient2.waitForEvent();

            assertEquals( contextEvent.getId(), fhirCastClient1.getLastEvent().getId() );
            assertEquals( contextEvent.getId(), fhirCastClient2.getLastEvent().getId() );
        }
        {
            Patient patient = new Patient();
            patient.setId("patient2");
            ContextEvent contextEvent = WorkflowEventFactory.createOpenEvent(patient);
            fhirCastClient1.resetEventWait();
            fhirCastClient2.resetEventWait();
            fhirCastClientTopic.sendEvent(contextEvent);
            fhirCastClient1.waitForEvent();
            fhirCastClient2.waitForEvent();

            assertEquals( contextEvent.getId(), fhirCastClient1.getLastEvent().getId() );
            assertEquals( contextEvent.getId(), fhirCastClient2.getLastEvent().getId() );
        }

//        fhirCastWebhookClient1.unsubscribeWebsubAll();
        //        fhirCastWebhookClient2.unsubscribeWebsubAll();
        fhirCastClient1.resetEventWait();
        fhirCastClient2.resetEventWait();
        fhirCastClientTopic.logOut();
        fhirCastClient1.waitForEvent();
        fhirCastClient2.waitForEvent();

        assertEquals( "userlogout", fhirCastClient1.getLastEvent().getEvent().getHub_event());
        assertEquals( "userlogout", fhirCastClient2.getLastEvent().getEvent().getHub_event());

    }

}
