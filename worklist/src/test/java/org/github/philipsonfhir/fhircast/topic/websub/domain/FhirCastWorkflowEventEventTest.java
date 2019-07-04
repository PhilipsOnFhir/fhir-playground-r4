package org.github.philipsonfhir.fhircast.topic.websub.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastContext;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEventEvent;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhircastEventType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;

import java.io.IOException;

public class FhirCastWorkflowEventEventTest {
    String json =
            "{ \"event\": \n" +
            "\t{ \"hub.topic\": \"FC1561990516010\", \n" +
            "\t  \"hub.event\":\"open-patient-chart\",\n" +
            "\t  \"context\": [\n" +
            "\t\t{ \t\"key\": \"patient\",\n" +
            "\t\t\t\"resource\": \n" +
            "\t\t\t\t{\"resourceType\":\"Patient\",\n" +
            "\t\t\t\t\"id\":\"WORKLIST-4\",\n" +
            "\t\t\t\t\"name\":[{\"family\":\"Pijlkruid\",\"given\":[\"Y\"]}]\n" +
            "\t\t\t\t}\n" +
            "\t\t}]\n" +
            "\t}\n" +
            "}";

    String json2 = "{\"timestamp\":null,\"id\":null,\"event\":{\"context\":[{\"key\":\"patient\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"dummy\"}}],\"hub.topic\":\"FC1561990516010\",\"hub.event\":\"open-patient-chart\"}}\n";

    @Test
    public void testParsing1() throws IOException {
        Patient patient = (Patient) new Patient().setId("dummy");
        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( FhircastEventType.OPEN_PATIENT_CHART );
        fhirCastWorkflowEventEvent.setHub_topic( "FC1561990516010" );

        FhirCastContext fhirCastContext = new FhirCastContext();
        fhirCastContext.setKey("patient");
        fhirCastContext.setResource(patient);
        fhirCastWorkflowEvent.setEvent(fhirCastWorkflowEventEvent);
        fhirCastWorkflowEventEvent.getContext().add(fhirCastContext);

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(fhirCastWorkflowEvent);
        System.out.println(value);

    }

    @Test
    public void testParsing2() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue( json, FhirCastWorkflowEvent.class );

    }

}
