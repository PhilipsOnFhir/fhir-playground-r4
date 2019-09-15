package org.github.philipsonfhir.worklist.fhircast.server.topic.service;

import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.FhircastEventType;
import lombok.ToString;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.jboss.logging.Logger;

import java.util.*;

@ToString
public class FhirCastTopic {
    private final TopicService _source;
    private String _topic;
    private List<LaunchContext> launchContexts = new ArrayList<>(  );
    private LaunchContext _current;
    private boolean _loggedOut;
    private Logger logger;
    private Practitioner practitioner;

    FhirCastTopic(TopicService source, Practitioner practitioner){
        this( source, "FC"+System.currentTimeMillis(), practitioner );
    }

    FhirCastTopic( TopicService source, String topic, Practitioner practitioner ){
        _topic = topic;
        _source = source;
        logger = Logger.getLogger("FhirCastTopic-"+_topic);
        this.practitioner = practitioner;
    }


    public String getTopic() {
        return _topic;
    }

    public void userLogout() throws FhirCastException {
        logger.info( "logout" );
//        this.launchContexts.add(_current );
//        _current=null;
        launchContexts.stream()
            .filter( Objects::nonNull )
            .forEach( fhirCastTopicStudyOrPatient -> {
            if ( fhirCastTopicStudyOrPatient.hasPatient() ) {
                sendEvent( FhircastEventType.CLOSE_PATIENT_CHART, fhirCastTopicStudyOrPatient.getPatient() );
            } else {
                sendEvent( FhircastEventType.CLOSE_IMAGING_STUDY, fhirCastTopicStudyOrPatient.getImagingStudy() );
            }
        } );
        launchContexts.clear();

        if ( !_loggedOut){
            _loggedOut = true;
            sendEvent( FhircastEventType.USER_LOGOUT, null );
            _source.removeTopic( _topic );
        }
    }

    public LaunchContext openPatientChart(Patient patient) {
        logger.info( "open patient "+patient.getId() );

        boolean present = false;
        LaunchContext studyOrPatient = new LaunchContext( practitioner, patient );
        Iterator<LaunchContext> it = this.launchContexts.iterator();
        while( it.hasNext() ){
            LaunchContext nxt = it.next();
            if ( nxt.equals( studyOrPatient )){
                studyOrPatient = nxt;
                present=true;
            }
        }
        if ( !present ){
            this.launchContexts.add(studyOrPatient);
        }

        if ( _current !=null && !_current.equals(studyOrPatient)) {
            sendEvent(FhircastEventType.OPEN_PATIENT_CHART, patient);
        }
        _current = studyOrPatient;
        return studyOrPatient;
    }

//    public void switchPatient(Patient patient) throws FhirCastException {
//        logger.info( "switch patient "+patient.getId() );
//        LaunchContext studyOrPatient = new LaunchContext( patient );
//        if ( _current!=null && _current.equals( studyOrPatient )){
//            return;
//        }
//        Optional<LaunchContext> optPatient = this.launchContexts.stream()
//            .filter( LaunchContext::hasPatient )
//            .filter( fhirCastTopicStudyOrPatient -> fhirCastTopicStudyOrPatient.getPatient().getId().equals( patient.getId() ) )
//            .findFirst();
//        if ( optPatient.isPresent() ) {
//            if ( _current!=null) { launchContexts.add( _current ); }
//            _current = optPatient.get();
//            launchContexts.remove( _current );
//            sendEvent( FhircastEventType.SWITCH_PATIENT_CHART, patient );
//        } else {
//            throw new FhirCastException( "Patient "+patient.getId()+" is unknown in topic "+this._topic );
//        }
//    }

    public LaunchContext openImagingStudy(ImagingStudy study) {
        logger.info( "open study "+study.getId() );
        LaunchContext studyOrPatient = new LaunchContext( study );

        if ( _current !=null && !_current.equals(studyOrPatient)) {
            sendEvent(FhircastEventType.OPEN_IMAGING_STUDY, study);
        }
        if ( !this.launchContexts.contains(  studyOrPatient )){
            this.launchContexts.add(studyOrPatient);
        }
        _current = studyOrPatient;
        return studyOrPatient;
    }

    public void close( Patient patient ){
        logger.info( "close patient "+patient.getId() );
        boolean found = false;
        Iterator<LaunchContext> it = this.launchContexts.iterator();
        while ( it.hasNext() ){
            LaunchContext studyOrPatient = it.next();

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
        Iterator<LaunchContext> it = this.launchContexts.iterator();
        while ( it.hasNext() ){
            LaunchContext studyOrPatient = it.next();
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


    public LaunchContext getFhirCastTopicStudyOrPatient(String launch) {
        Iterator<LaunchContext> it = this.launchContexts.iterator();
        while ( it.hasNext() ){
            LaunchContext launchContext = it.next();
            if ( launchContext.getLaunch().equals(launch)){
                return launchContext;
            }
        }
        return null;
    }

    public String openLaunch(IBaseResource retrieveFhirResource) {
        LaunchContext launchContext = null;
        if ( retrieveFhirResource instanceof Patient ){
            launchContext = this.openPatientChart((Patient) retrieveFhirResource);
        } else if ( retrieveFhirResource instanceof ImagingStudy ){
            launchContext = this.openImagingStudy((ImagingStudy) retrieveFhirResource);
        }
        return ( launchContext !=null? launchContext.getLaunch():null );
    }

    public boolean ofUser(String name) {
        boolean res = this.practitioner.getIdentifier().stream()
                .anyMatch(identifier -> identifier.getSystem().equals("userid") && identifier.getValue().equals(name));
        return res;

    }

    public Practitioner getPractioner() {
        return this.practitioner;
    }

}
