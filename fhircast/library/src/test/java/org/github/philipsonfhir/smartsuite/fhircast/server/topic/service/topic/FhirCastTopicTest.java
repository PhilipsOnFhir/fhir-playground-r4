package org.github.philipsonfhir.smartsuite.fhircast.server.topic.service.topic;

import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopicEvent;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;

import static org.junit.Assert.*;


public class FhirCastTopicTest {
    MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();

    @Test
    public void testOpen() throws InterruptedException {

        FhirCastTopic fhirCastTopic = new FhirCastTopic(myApplicationEventPublisher, null);

        Patient          anchor1 = (Patient) new Patient().setId("someId1");
        ImagingStudy     anchor2 = (ImagingStudy) new ImagingStudy().setId("someId2");
        Encounter        anchor3 = (Encounter) new Encounter().setId("someId3");
        DiagnosticReport anchor4 = (DiagnosticReport) new DiagnosticReport().setId("someId4");

        fhirCastTopic.openAnchor( anchor1 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor1 );

        fhirCastTopic.closeAnchor( anchor2 );
        fhirCastTopic.closeAnchor( anchor1 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.CLOSE, anchor1 );

        fhirCastTopic.openAnchor( anchor2 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor2 );

        fhirCastTopic.openAnchor( anchor2 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor2 );

        fhirCastTopic.closeAnchor( anchor2 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.CLOSE, anchor2 );

        fhirCastTopic.openAnchor( anchor2 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor2 );

        fhirCastTopic.openAnchor( anchor3 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor3 );

        fhirCastTopic.openAnchor( anchor4 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor4 );

        fhirCastTopic.closeTopic();
        checkAndReset( 4, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.LOGOUT );
    }

    @Test
    public void testSyncError() throws InterruptedException {

        FhirCastTopic fhirCastTopic = new FhirCastTopic(myApplicationEventPublisher, null);

        Patient          anchor1 = (Patient) new Patient().setId("someId1");
        ImagingStudy     anchor2 = (ImagingStudy) new ImagingStudy().setId("someId2");
        Encounter        anchor3 = (Encounter) new Encounter().setId("someId3");
        DiagnosticReport anchor4 = (DiagnosticReport) new DiagnosticReport().setId("someId4");

        fhirCastTopic.openAnchor( anchor1 );
        checkAndReset( 1, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.OPEN, anchor1 );

        myApplicationEventPublisher.sendSyncError( true );
        fhirCastTopic.openAnchor( anchor2 );
        checkAndReset( 2, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.SYNCERROR, anchor2 );

        myApplicationEventPublisher.sendSyncError( false );
        fhirCastTopic.closeTopic();
        checkAndReset( 3, fhirCastTopic.getTopic(), FhirCastTopicEvent.EventType.LOGOUT );
    }

    private FhirCastTopicEvent checkAndReset(int n, String topic, FhirCastTopicEvent.EventType eventType) throws InterruptedException {
        FhirCastTopicEvent mostRecentEvent = (FhirCastTopicEvent) myApplicationEventPublisher.waitForEvent();
        assertEquals( topic, mostRecentEvent.getTopic() );
        assertNotNull( mostRecentEvent );
        assertEquals( eventType , mostRecentEvent.getEventType() );
        assertEquals( n, myApplicationEventPublisher.getEvents().size() );
        return mostRecentEvent;
    }

    private void checkAndReset(int i, String topic, FhirCastTopicEvent.EventType eventType, Resource anchor) throws InterruptedException {
        check( i, topic, eventType, anchor );
        reset();
    }

    private void reset() {
        myApplicationEventPublisher.resetEventWait();
    }

    private void check(int n, String topic, FhirCastTopicEvent.EventType eventType, Resource anchor) throws InterruptedException {
        FhirCastTopicEvent mostRecentEvent = checkAndReset( n, topic, eventType );

        assertNotNull( mostRecentEvent );
        assertEquals( n, myApplicationEventPublisher.getEvents().size() );
        if ( mostRecentEvent.getEventType().equals(FhirCastTopicEvent.EventType.OPEN) || mostRecentEvent.getEventType().equals(FhirCastTopicEvent.EventType.CLOSE)){
            assertNotNull( "Anchor is null", mostRecentEvent.getAnchor() );
            assertNotNull( mostRecentEvent.getAnchor().getAnchorResource() );
            assertEquals( anchor.getId() , mostRecentEvent.getAnchor().getAnchorResource().getId() );
        }
    }


}