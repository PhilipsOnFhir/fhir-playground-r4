package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.FhircastEventType;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.Assert.*;

public class FhirCastContextServiceTest {


    @Test
    public void createRemoveTopic() throws FhirCastException {
        MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();
        FhirCastContextService fhirCastContextService = new FhirCastContextService( myApplicationEventPublisher );

        FhirCastTopic fhirCastTopic1 = fhirCastContextService.updateTopic( "test" );
        FhirCastTopic fhirCastTopic2 = fhirCastContextService.createTopic();
        String patientId = "someId";

        FhirCastTopic getExists1 = fhirCastContextService.getTopic( fhirCastTopic1.getTopic() );
        assertNotNull( getExists1 );
        assertEquals( fhirCastTopic1.getTopic(), getExists1.getTopic() );

        FhirCastTopic getExists2 = fhirCastContextService.getTopic( fhirCastTopic2.getTopic() );
        assertNotNull( getExists2 );
        assertEquals( fhirCastTopic2.getTopic(), getExists2.getTopic() );

        getExists1.userLogout();
        try{
            FhirCastTopic getAbsent1 = fhirCastContextService.getTopic( fhirCastTopic1.getTopic() );
            fail(  );
        } catch ( FhirCastException e ){}

        getExists2.userLogout();
        try{
            FhirCastTopic getAbsent2 = fhirCastContextService.getTopic( fhirCastTopic2.getTopic() );
            fail(  );
        } catch ( FhirCastException e ){}
    }

    @Test
    public void patientManagement() throws InterruptedException {
        MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();
        FhirCastContextService fhirCastContextService = new FhirCastContextService( myApplicationEventPublisher );

        FhirCastTopic fhirCastTopic = fhirCastContextService.createTopic();

        Patient patient1 = (Patient) new Patient().setId( "patient1" );
        Patient patient2 = (Patient) new Patient().setId( "patient2" );

        FhirCastTopicStudyOrPatient fhirCastTopicStudyOrPatient1 = fhirCastTopic.openPatientChart( patient1 );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.OPEN_PATIENT_CHART, patient1 );

        FhirCastTopicStudyOrPatient fhirCastTopicStudyOrPatient2 = fhirCastTopic.openPatientChart( patient2 );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.OPEN_PATIENT_CHART, patient2 );

        fhirCastTopic.closeCurrent( );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.SWITCH_PATIENT_CHART);


        fhirCastTopic.closeCurrent( );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.CLOSE_PATIENT_CHART );

    }

    @Test
    public void studyManagement() throws InterruptedException {
        MyApplicationEventPublisher myApplicationEventPublisher = new MyApplicationEventPublisher();
        FhirCastContextService fhirCastContextService = new FhirCastContextService( myApplicationEventPublisher );

        FhirCastTopic fhirCastTopic = fhirCastContextService.createTopic();

        ImagingStudy study1 = (ImagingStudy) new ImagingStudy().setId( "study1" );
        ImagingStudy study2 = (ImagingStudy) new ImagingStudy().setId( "study2" );

        FhirCastTopicStudyOrPatient fhirCastTopicStudyOrPatient1 = fhirCastTopic.openImagingStudy( study1 );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.OPEN_IMAGING_STUDY, study1 );

        FhirCastTopicStudyOrPatient fhirCastTopicStudyOrPatient2 = fhirCastTopic.openImagingStudy( study2 );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.OPEN_IMAGING_STUDY, study2 );

        fhirCastTopic.closeCurrent( );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.SWITCH_IMAGING_STUDY );


        fhirCastTopic.closeCurrent( );
        checkEvent( fhirCastTopic, myApplicationEventPublisher, FhircastEventType.CLOSE_IMAGING_STUDY );

    }
    private void checkEvent(FhirCastTopic fhirCastTopic, MyApplicationEventPublisher myApplication, FhircastEventType event, ImagingStudy study ) throws InterruptedException {
        FhirCastTopicEvent fhirCastTopicEvent = checkEvent( fhirCastTopic, myApplication, event );

        assertTrue( fhirCastTopicEvent.getContext().get("study") instanceof ImagingStudy  );
        ImagingStudy eventPatientOrStudy = (ImagingStudy) fhirCastTopicEvent.getContext().get("study");
        assertEquals( study.getId(), eventPatientOrStudy.getId() );
    }
    private void checkEvent(FhirCastTopic fhirCastTopic, MyApplicationEventPublisher myApplication, FhircastEventType event, Patient patient ) throws InterruptedException {
        FhirCastTopicEvent fhirCastTopicEvent = checkEvent( fhirCastTopic, myApplication, event );

        assertTrue( fhirCastTopicEvent.getContext().get("patient") instanceof Patient  );
        Patient eventPatientOrStudy = (Patient) fhirCastTopicEvent.getContext().get("patient");
        assertEquals( patient.getId(), eventPatientOrStudy.getId() );
    }

    private FhirCastTopicEvent checkEvent(FhirCastTopic fhirCastTopic, MyApplicationEventPublisher myApplication, FhircastEventType event) throws InterruptedException {
        FhirCastTopicEvent fhirCastTopicEvent = myApplication.getLastEvent();
        assertEquals( fhirCastTopic, fhirCastTopicEvent.getSource() );
        assertEquals( event, fhirCastTopicEvent.getEventType() );
        myApplication.clear();
        return fhirCastTopicEvent;
    }
}

class MyApplicationEventPublisher implements ApplicationEventPublisher {

    private FhirCastTopicEvent _event;
    private final Object o = new Object();

    FhirCastTopicEvent getLastEvent() throws InterruptedException {
        if ( _event==null ) {
            synchronized ( o ) {
                o.wait( 5000 );
                return _event;
            }
        }
        return _event;
    }


    private void newFhirCastTopicEvent(FhirCastTopicEvent fhirCastTopicEvent) {
        synchronized ( o ){
            _event = fhirCastTopicEvent;
            o.notifyAll();
        }
    }

    void clear(){
        _event = null;
    }

    @Override
    public void publishEvent(Object event) {
        newFhirCastTopicEvent( (FhirCastTopicEvent) event );
    }
}