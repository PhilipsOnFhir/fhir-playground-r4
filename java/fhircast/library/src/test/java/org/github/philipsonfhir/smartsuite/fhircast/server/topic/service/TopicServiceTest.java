package org.github.philipsonfhir.smartsuite.fhircast.server.topic.service;

import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.TopicService;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.service.topic.MyApplicationEventPublisher;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopicServiceTest {
    @Test
    public void createRemoveTopic() throws FhirCastException {
        MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();
        TopicService fhirCastContextService = new TopicService(myApplicationEventPublisher );

        FhirCastTopic fhirCastTopic1 = fhirCastContextService.updateTopic( "test", null );
        FhirCastTopic fhirCastTopic2 = fhirCastContextService.createTopic( null );
        String patientId = "someId";

        FhirCastTopic getExists1 = fhirCastContextService.getTopic( fhirCastTopic1.getTopic() );
        assertNotNull( getExists1 );
        assertEquals( fhirCastTopic1.getTopic(), getExists1.getTopic() );

        FhirCastTopic getExists2 = fhirCastContextService.getTopic( fhirCastTopic2.getTopic() );
        assertNotNull( getExists2 );
        assertEquals( fhirCastTopic2.getTopic(), getExists2.getTopic() );

        fhirCastContextService.removeTopic("test");
        try {
            getExists1 = fhirCastContextService.getTopic(fhirCastTopic1.getTopic());
            fail("topic should not exist");
        } catch( FhirCastException e ){
        }

    }

}