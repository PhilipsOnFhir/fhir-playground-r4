package org.github.philipsonfhir.smartsuite.fhircast.client;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class FhirCastClientTopic {
    private final String topicId;
    private final String hubUrl;
    private final String topicUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public FhirCastClientTopic(String topicUrl, String hubUrl, String topic) {
        this.topicUrl = topicUrl;
        this.hubUrl = hubUrl;
        this.topicId = topic;
    }

    public FhirCastWebhookClient createWebHookClient(){
        return new FhirCastWebhookClient( this.hubUrl, this.topicId );
    }

    public String getTopicId() {
        return topicId;
    }

    public void logOut() {
        restTemplate.delete( topicUrl +"/"+topicId );
    }

    public FhirCastWebsocketClient createWebSocketClient() {
        return new FhirCastWebsocketClient( this.hubUrl, this.topicId );
    }

    public void sendEvent( ContextEvent fhirCastWorkflowEvent ) throws FhirCastException {
        fhirCastWorkflowEvent.getEvent().setHub_topic(this.topicId);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(hubUrl, fhirCastWorkflowEvent, String.class);
            if ( !responseEntity.getStatusCode().is2xxSuccessful()  ) {
                throw new FhirCastException("Event sending failed");
            }
        } catch ( HttpServerErrorException e ){
            throw new FhirCastNotificationException("One event was not delivered");
        }
    }

}
