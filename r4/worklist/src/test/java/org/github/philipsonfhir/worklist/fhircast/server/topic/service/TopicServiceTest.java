package org.github.philipsonfhir.worklist.fhircast.server.topic.service;

import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopicServiceTest {

    @Test
    public void createRemoveTopic() throws FhirCastException {
//        MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();
        TopicService fhirCastContextService = new TopicService(null );

        FhirCastTopic fhirCastTopic1 = fhirCastContextService.updateTopic( "test", new Practitioner() );
        FhirCastTopic fhirCastTopic2 = fhirCastContextService.createTopic( new Practitioner());
        String patientId = "someId";

        FhirCastTopic getExists1 = fhirCastContextService.getTopic( fhirCastTopic1.getTopic() );
        assertNotNull( getExists1 );
        assertEquals( fhirCastTopic1.getTopic(), getExists1.getTopic() );

        FhirCastTopic getExists2 = fhirCastContextService.getTopic( fhirCastTopic2.getTopic() );
        assertNotNull( getExists2 );
        assertEquals( fhirCastTopic2.getTopic(), getExists2.getTopic() );
    }

}
