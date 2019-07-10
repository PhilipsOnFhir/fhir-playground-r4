package org.github.philipsonfhir.fhircast.server.topic.service;

import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhircastEventType;
import lombok.ToString;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.jboss.logging.Logger;

import java.util.*;

@ToString
public class FhirCastTopic {
    private final TopicService _source;
    private String _topic;
    private List<FhirCastTopicStudyOrPatient> fhirCastTopicStudyOrPatients = new ArrayList<>(  );
    private FhirCastTopicStudyOrPatient _current;
    private boolean _loggedOut;
    private Logger logger;

    FhirCastTopic(TopicService source ){
        this( source, "FC"+System.currentTimeMillis() );
    }

    FhirCastTopic(TopicService source, String topic){
        _topic = topic;
        _source = source;
        logger = Logger.getLogger("FhirCastTopic-"+_topic);
    }


    public String getTopic() {
        return _topic;
    }

    public void userLogout() throws FhirCastException {
        logger.info( "logout" );
//        this.fhirCastTopicStudyOrPatients.add(_current );
//        _current=null;
        fhirCastTopicStudyOrPatients.stream()
            .filter( Objects::nonNull )
            .forEach( fhirCastTopicStudyOrPatient -> {
            if ( fhirCastTopicStudyOrPatient.hasPatient() ) {
                sendEvent( FhircastEventType.CLOSE_PATIENT_CHART, fhirCastTopicStudyOrPatient.getPatient() );
            } else {
                sendEvent( FhircastEventType.CLOSE_IMAGING_STUDY, fhirCastTopicStudyOrPatient.getImagingStudy() );
            }
        } );
        fhirCastTopicStudyOrPatients.clear();

        if ( !_loggedOut){
            _loggedOut = true;
            sendEvent( FhircastEventType.USER_LOGOUT, null );
            _source.removeTopic( _topic );
        }
    }

    public FhirCastTopicStudyOrPatient openPatientChart(Patient patient) {
        logger.info( "open patient "+patient.getId() );
        FhirCastTopicStudyOrPatient studyOrPatient = new FhirCastTopicStudyOrPatient( patient );
        if ( _current !=null && !_current.equals(studyOrPatient)) {
            sendEvent(FhircastEventType.OPEN_PATIENT_CHART, patient);
        }
        if ( !this.fhirCastTopicStudyOrPatients.contains(  studyOrPatient )){
            this.fhirCastTopicStudyOrPatients.add(studyOrPatient);
        }
        _current = studyOrPatient;
        return studyOrPatient;
    }

//    public void switchPatient(Patient patient) throws FhirCastException {
//        logger.info( "switch patient "+patient.getId() );
//        FhirCastTopicStudyOrPatient studyOrPatient = new FhirCastTopicStudyOrPatient( patient );
//        if ( _current!=null && _current.equals( studyOrPatient )){
//            return;
//        }
//        Optional<FhirCastTopicStudyOrPatient> optPatient = this.fhirCastTopicStudyOrPatients.stream()
//            .filter( FhirCastTopicStudyOrPatient::hasPatient )
//            .filter( fhirCastTopicStudyOrPatient -> fhirCastTopicStudyOrPatient.getPatient().getId().equals( patient.getId() ) )
//            .findFirst();
//        if ( optPatient.isPresent() ) {
//            if ( _current!=null) { fhirCastTopicStudyOrPatients.add( _current ); }
//            _current = optPatient.get();
//            fhirCastTopicStudyOrPatients.remove( _current );
//            sendEvent( FhircastEventType.SWITCH_PATIENT_CHART, patient );
//        } else {
//            throw new FhirCastException( "Patient "+patient.getId()+" is unknown in topic "+this._topic );
//        }
//    }

    public FhirCastTopicStudyOrPatient openImagingStudy(ImagingStudy study) {
        logger.info( "open study "+study.getId() );
        FhirCastTopicStudyOrPatient studyOrPatient = new FhirCastTopicStudyOrPatient( study );

        if ( _current !=null && !_current.equals(studyOrPatient)) {
            sendEvent(FhircastEventType.OPEN_IMAGING_STUDY, study);
        }
        if ( !this.fhirCastTopicStudyOrPatients.contains(  studyOrPatient )){
            this.fhirCastTopicStudyOrPatients.add(studyOrPatient);
        }
        _current = studyOrPatient;
        return studyOrPatient;
    }

    public void close( Patient patient ){
        logger.info( "close patient "+patient.getId() );
        boolean found = false;
        Iterator<FhirCastTopicStudyOrPatient> it = this.fhirCastTopicStudyOrPatients.iterator();
        while ( it.hasNext() ){
            FhirCastTopicStudyOrPatient studyOrPatient = it.next();

            if ( studyOrPatient.contains( patient )){
                found = true;
                it.remove();
                sendEvent( FhircastEventType.CLOSE_PATIENT_CHART, patient );
            }
        }
        if ( !found){
            logger.info( "close patient "+patient.getId()+" did not match open patient" );
        }
    }

    public void close( ImagingStudy study ){
        logger.info( "close study "+study.getId() );
        boolean found = false;
        Iterator<FhirCastTopicStudyOrPatient> it = this.fhirCastTopicStudyOrPatients.iterator();
        while ( it.hasNext() ){
            FhirCastTopicStudyOrPatient studyOrPatient = it.next();
            if ( studyOrPatient.contains( study )){
                found = true;
                it.remove();
                sendEvent( FhircastEventType.CLOSE_IMAGING_STUDY, study );
            }
        }
        if ( !found){
            logger.info( "close study "+study.getId()+" did not match open study" );
        }
    }

    private void sendEvent(FhircastEventType fhircastEventType, Resource resource ) {
        TreeMap<String, Object > map = new TreeMap<>();
        if ( resource instanceof Patient ){ map.put( "patient", resource ); }
        if ( resource instanceof ImagingStudy ){ map.put( "study", resource ); }
        FhirCastTopicEvent fhirCastWorkflowEvent = new FhirCastTopicEvent(  this, _topic, fhircastEventType, map );
        _source.publishEvent( fhirCastWorkflowEvent );
    }


}
