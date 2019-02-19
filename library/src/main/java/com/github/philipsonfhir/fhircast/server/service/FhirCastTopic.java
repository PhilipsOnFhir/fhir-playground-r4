package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.FhircastEventType;
import lombok.ToString;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@ToString
public class FhirCastTopic {
    private final FhirCastContextService _source;
    private String _topic;
    private List<FhirCastTopicStudyOrPatient> _fhirCastTopicStudyOrPatients = new ArrayList<>(  );
    private List<FhirCastTopicEventListener> _listenersSet = new ArrayList<>(  );
    private FhirCastTopicStudyOrPatient _current;
    private boolean _loggedOut;

    FhirCastTopic( FhirCastContextService source ){
        this( source, "FC"+System.currentTimeMillis() );
    }

    FhirCastTopic( FhirCastContextService source, String topic){
        _topic = topic;
        _source = source;
    }



    public String getTopic() {
        return _topic;
    }

    public void userLogout() throws FhirCastException {
        this._fhirCastTopicStudyOrPatients.add(_current );
        _current=null;
        _fhirCastTopicStudyOrPatients.stream()
            .filter( f -> f!=null )
            .forEach( fhirCastTopicStudyOrPatient -> {
            if ( fhirCastTopicStudyOrPatient.hasPatient() ) {
                sendEvent( FhircastEventType.CLOSE_PATIENT_CHART, fhirCastTopicStudyOrPatient.getPatient() );
            } else {
                sendEvent( FhircastEventType.CLOSE_IMAGING_STUDY, fhirCastTopicStudyOrPatient.getImagingStudy() );
            }
        } );
        _fhirCastTopicStudyOrPatients.clear();

        if ( !_loggedOut){
            _loggedOut = true;
            sendEvent( FhircastEventType.USER_LOGOUT, null );
            _source.removeTopic( _topic );
        }
    }

    public void registerFhirCastTopicEventListener( FhirCastTopicEventListener eventListener) {
        _listenersSet.add( eventListener );
    }

    public FhirCastTopicStudyOrPatient openPatientChart(Patient patient) {
        if ( _current!=null) { _fhirCastTopicStudyOrPatients.add( _current ); }
        _current = new FhirCastTopicStudyOrPatient( patient );
        sendEvent( FhircastEventType.OPEN_PATIENT_CHART, patient );
        return _current;
    }

    public void switchPatient(Patient patient) throws FhirCastException {
        Optional<FhirCastTopicStudyOrPatient> optPatient = this._fhirCastTopicStudyOrPatients.stream()
            .filter( fhirCastTopicStudyOrPatient -> fhirCastTopicStudyOrPatient.hasPatient() )
            .filter( fhirCastTopicStudyOrPatient -> fhirCastTopicStudyOrPatient.getPatient().getId().equals( patient.getId() ) )
            .findFirst();
        if ( optPatient.isPresent() ) {
            if ( _current!=null) { _fhirCastTopicStudyOrPatients.add( _current ); }
            _current = optPatient.get();
            _fhirCastTopicStudyOrPatients.remove( _current );
            sendEvent( FhircastEventType.SWITCH_PATIENT_CHART, patient );
        } else {
            throw new FhirCastException( "Patient "+patient.getId()+" is unknown in topic "+this._topic );
        }
    }

    public FhirCastTopicStudyOrPatient openImagingStudy(ImagingStudy study ) {
        if ( _current!=null) { _fhirCastTopicStudyOrPatients.add( _current ); }
        _current = new FhirCastTopicStudyOrPatient( this, study );
        sendEvent( FhircastEventType.OPEN_IMAGING_STUDY, study );
        return _current;
    }

    public void closeCurrent(){
        if ( _current.hasPatient() ) {
            sendEvent( FhircastEventType.CLOSE_PATIENT_CHART, _current.getPatient() );
        } else {
            sendEvent( FhircastEventType.CLOSE_IMAGING_STUDY, _current.getImagingStudy() );
        }
        if (  !_fhirCastTopicStudyOrPatients.isEmpty()) {
            _current = _fhirCastTopicStudyOrPatients.get( 0 );
            _fhirCastTopicStudyOrPatients.remove( _current );
            if ( _current.hasPatient() ) {
                sendEvent( FhircastEventType.SWITCH_PATIENT_CHART, _current.getPatient() );
            } else {
                sendEvent( FhircastEventType.SWITCH_IMAGING_STUDY, _current.getImagingStudy() );
            }
        } else {
            _current = null;
        }
    }

    public void sendEvent(FhircastEventType fhircastEventType, Resource resource ) {
        TreeMap<String, Object > map = new TreeMap<>();
        if ( resource!=null && resource instanceof Patient){ map.put( "patient", resource ); }
        if ( resource!=null && resource instanceof ImagingStudy){ map.put( "study", resource ); }
        FhirCastTopicEvent fhirCastWorkflowEvent = new FhirCastTopicEvent(  this, fhircastEventType, map );
        _listenersSet.stream().forEach( listener ->{
            listener.newFhirCastTopicEvent( fhirCastWorkflowEvent );
        } );
    }


}
