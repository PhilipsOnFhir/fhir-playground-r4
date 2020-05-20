package org.github.philipsonfhir.smartsuite.fhircast.client;

import org.github.philipsonfhir.smartsuite.Prefix;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain.Topic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain.TopicList;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class FhirCastClient {
    private final String serverUrl;
    private final String topicUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public FhirCastClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.topicUrl = serverUrl+ Prefix.TOPIC;
    }

    public FhirCastClientTopic createTopic() throws FhirCastException {
        ResponseEntity<Topic> responseEntity = restTemplate.postForEntity( this.topicUrl, "{}", Topic.class  );
        if ( responseEntity.getStatusCode() != HttpStatus.CREATED ){
            throw new FhirCastException("topic could not be created");
        }
        if ( responseEntity.getBody()==null ){ throw new FhirCastException("Body is null "); }
        if ( responseEntity.getBody().getTopic()==null ){ throw new FhirCastException("topic is null "); }
        if ( responseEntity.getBody().getHubUrl()==null ){ throw new FhirCastException("hub_url is null "); }
        return new FhirCastClientTopic( this.topicUrl
                , responseEntity.getBody().getHubUrl()
                , responseEntity.getBody().getTopic()
        );
    }

    public FhirCastClientTopic connectToTopic( String topic ) throws FhirCastException {
        ResponseEntity<TopicList> responseEntity = restTemplate.getForEntity( this.topicUrl, TopicList.class  );
        if ( responseEntity.getStatusCode() != HttpStatus.OK ){
            throw new FhirCastException("topic list could not be retrieved");
        }
        if ( responseEntity.getBody()==null ){ throw new FhirCastException("Body is null "); }
        if ( responseEntity.getBody().getTopic()==null ){ throw new FhirCastException("topic is null "); }

        String hubUrl = responseEntity.getBody().getHubUrl();

        return new FhirCastClientTopic( this.topicUrl
                , responseEntity.getBody().getHubUrl()
                , topic
        );
    }

    public List<FhirCastWebhookClient> getTopics() throws FhirCastException {
        ResponseEntity<TopicList> responseEntity = restTemplate.getForEntity( this.topicUrl, TopicList.class  );
        if ( responseEntity.getStatusCode() != HttpStatus.OK ){
            throw new FhirCastException("topic list could not be retrieved");
        }
        if ( responseEntity.getBody()==null ){ throw new FhirCastException("Body is null "); }
        if ( responseEntity.getBody().getTopic()==null ){ throw new FhirCastException("topic is null "); }

        return responseEntity.getBody().getTopic().stream()
                .map( topicStr -> new FhirCastWebhookClient( this.topicUrl, topicStr ))
                .collect(Collectors.toList())
        ;
    }
}
