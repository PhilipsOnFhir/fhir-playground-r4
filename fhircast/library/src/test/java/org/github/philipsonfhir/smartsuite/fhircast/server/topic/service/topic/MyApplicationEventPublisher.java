package org.github.philipsonfhir.smartsuite.fhircast.server.topic.service.topic;

import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopicEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class MyApplicationEventPublisher implements ApplicationEventPublisher {

    private List<ApplicationEvent> events = new ArrayList<ApplicationEvent>();
    private Object eventSem = new Object();
    private boolean eventReceived = false;
    private ApplicationEvent mostRecentEvent;
    private boolean sendSync = false;

    @Override
    public void publishEvent(ApplicationEvent event) {
        System.out.println("event received "+event);
        this.events.add( event );
        mostRecentEvent = event;
        synchronized ( eventSem ){
            eventReceived = true;
            eventSem.notifyAll();
        }
        if ( sendSync == true && !((FhirCastTopicEvent)event).getEventType().equals(FhirCastTopicEvent.EventType.SYNCERROR )){
            FhirCastTopic fhirCastTopic = (FhirCastTopic) event.getSource();
            fhirCastTopic.syncError( event );
        }
    }

    @Override
    public void publishEvent(Object o) {
        publishEvent( (ApplicationEvent) o);
    }

    public void resetEventWait() {
        this.eventReceived = false;
        this.events = new ArrayList<ApplicationEvent>();
    }


    ApplicationEvent waitForEvent() throws InterruptedException {
        synchronized (eventSem) {
            if( eventReceived==false ){
                eventSem.wait( 10*1000 );
            }
            if( eventReceived==false ) {
                fail("event not received");
            }
        }
        return mostRecentEvent;
    }

    public List<ApplicationEvent> getEvents() {
        return this.events;
    }

    public void sendSyncError( boolean sendSync ) {
        this.sendSync = sendSync;
    }
}
