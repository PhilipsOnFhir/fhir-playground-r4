package org.github.philipsonfhir.smartsuite.fhircast.client;

import org.github.philipsonfhir.smartsuite.fhircast.FhirCastServer;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FhirCastServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8080")
@RunWith(SpringRunner.class)
public class FhirCastClientTest {
    private long port = 8080;


    @Test
    public void createRemoveTopic() throws FhirCastException {
        FhirCastClient fhirCastClient = new FhirCastClient( String.format("http://localhost:%d",port ));
        FhirCastClientTopic fhirCastClientTopic = fhirCastClient.createTopic();

        FhirCastWebhookClient fhirCastWebhookClient = fhirCastClientTopic.createWebHookClient();
        {
            List<FhirCastWebhookClient> fhirCastClientTopicList = fhirCastClient.getTopics();
            assertEquals(1, fhirCastClientTopicList.size());
            assertEquals(fhirCastClientTopic.getTopicId(), fhirCastClientTopicList.get(0).getTopicId());
        }

        fhirCastClientTopic.logOut();
        {
            List<FhirCastWebhookClient> fhirCastClientTopicList = fhirCastClient.getTopics();
            assertEquals(0, fhirCastClientTopicList.size());
        }
    }


}
