package org.github.philipsonfhir.smartsuite.fhircast;

import org.github.philipsonfhir.smartsuite.Prefix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;


@SpringBootTest(classes = FhirCastServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8080")
@RunWith(SpringRunner.class)
public class FhirCastServerTest {

    private String baseUrl;
    private long port = 8080;

    private String topicId;
    private String topicUrl;
    RestTemplate restTemplate = new RestTemplate(  );

    @Test
    public void testTopicController(){
        this.baseUrl = "http://localhost:"+port;
        this.topicUrl = baseUrl+ Prefix.TOPIC;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.topicUrl, "{}", String.class  );
        assertEquals( HttpStatus.CREATED, responseEntity.getStatusCode());
        topicId = responseEntity.getBody();
    }


}
