package org.github.philipsonfhir.fhircast.topic.websub.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class FhirCastSessionSubscribeTest {

    @Test
    public void json2string() throws JsonProcessingException {
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_channel_type("websocket");
        fhirCastSessionSubscribe.setHub_events(FhircastEventType.OPEN_PATIENT_CHART.getName()+","+FhircastEventType.CLOSE_PATIENT_CHART);
        fhirCastSessionSubscribe.setHub_mode("subscribe");
        fhirCastSessionSubscribe.setHub_secret("randomSecret");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(fhirCastSessionSubscribe);

        System.out.println(json);
    }

}
