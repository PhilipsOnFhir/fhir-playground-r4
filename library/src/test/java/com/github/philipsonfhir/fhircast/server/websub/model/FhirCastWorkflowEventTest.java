package com.github.philipsonfhir.fhircast.server.websub.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philipsonfhir.fhircast.server.topic.FhirCastTopicEvent;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FhirCastWorkflowEventTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serialize() throws JsonProcessingException {
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        fhirCastWorkflowEvent.setId("id");
        fhirCastWorkflowEvent.setTimestamp("opDitMonent");
        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setContext( null );
        fhirCastWorkflowEventEvent.setHub_topic("myTopic");
        fhirCastWorkflowEvent.setEvent(fhirCastWorkflowEventEvent);

        assertTrue( objectMapper.canSerialize(FhirCastTopicEvent.class) );
        String json = objectMapper.writeValueAsString(fhirCastWorkflowEvent);
        System.out.println(json);
        //     public FhirCastTopicEvent(Object source, String topic, FhircastEventType fhircastEventType, Map<String, Object> contexy) {

    }

    @Test
    public void deserialize() throws IOException {
        String msg = "{\"timestamp\":\"opDitMonent\",\"id\":\"id\",\"event\":{\"context\":null,\"hub.topic\":\"myTopic\",\"hub.event\":null,\"hub.channel.type\":\"websocket\"}}";
        FhirCastWorkflowEvent fhirCastWorkflowEvent = objectMapper.readValue( msg, FhirCastWorkflowEvent.class );
    }


}