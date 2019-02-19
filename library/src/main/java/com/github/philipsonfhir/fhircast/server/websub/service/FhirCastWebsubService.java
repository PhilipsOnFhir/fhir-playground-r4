package com.github.philipsonfhir.fhircast.server.websub.service;

import com.github.philipsonfhir.fhircast.server.service.FhirCastContextService;
import com.github.philipsonfhir.fhircast.server.service.FhirCastTopic;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.NotImplementedException;
import com.github.philipsonfhir.fhircast.support.websub.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.logging.Logger;

@Controller
public class FhirCastWebsubService {
    @Autowired
    private FhirCastContextService fhirCastContextService;

    private Map<String, FhirCastWebsubSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public FhirCastWebsubService(){
//        this.updateFhirCastSession( "demo" );
    }

//    public FhirCastWebsubSession createFhirCastSession() {
//        String topicId = "FC"+System.currentTimeMillis();
//        FhirCastWebsubSession fhirCastWebsubSession = new FhirCastWebsubSession( topicId );
//
//        sessions.put( topicId, fhirCastWebsubSession );
//        logger.info("create session"+topicId);
//        return fhirCastWebsubSession;
//    }

    public Collection<FhirCastWebsubSession> getActiveFhirCastSessions() {
        return sessions.values();
    }

//    public void deleteFhirCastSession(String topicId)  {
//        logger.info("remove session"+topicId);
//        try {
//            FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession(topicId);
//            sessions.remove( topicId );
//            //TODO send remove events.
//        } catch ( FhirCastException e ) {
//        }
//    }

//    public boolean hasFhirCastSession(String topicId) {
//        FhirCastWebsubSession fhirCastWebsubSession = sessions.get( topicId );
//        return( fhirCastWebsubSession !=null );
//    }

    private FhirCastWebsubSession getFhirCastSession(String topicId) throws FhirCastException {
        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( topicId );
        FhirCastWebsubSession fhirCastWebsubSession = sessions.get( topicId );
        if ( fhirCastWebsubSession ==null ){
            logger.info( "Create WebSub session for topic "+topicId );
            fhirCastWebsubSession = new FhirCastWebsubSession( topicId );
            fhirCastTopic.registerFhirCastTopicEventListener( fhirCastWebsubSession );
            sessions.put( topicId, fhirCastWebsubSession );
        }
        return fhirCastWebsubSession;
    }

    public void subscribe(String sessionId, FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        logger.info("subscibe session"+sessionId);
        FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession( sessionId );
        fhirCastWebsubSession.updateSubscriptions( fhirCastSessionSubscribe );
    }

    public void eventReceived(FhirCastWorkflowEvent fhirCastWorkflowEvent ) throws FhirCastException, NotImplementedException {
        eventReceived( fhirCastWorkflowEvent.getEvent() );
    }

    public void eventReceived(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException, NotImplementedException {
        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( fhirCastWorkflowEventEvent.getHub_topic() );
        switch( fhirCastWorkflowEventEvent.getHub_event() ){
            case OPEN_PATIENT_CHART: {
                Patient patient = getPatientFromContext( fhirCastWorkflowEventEvent );
                fhirCastTopic.openPatientChart( patient );
                break;
            }
            case CLOSE_PATIENT_CHART:
                fhirCastTopic.closeCurrent();
                break;
            case SWITCH_PATIENT_CHART:{
                Patient patient = getPatientFromContext( fhirCastWorkflowEventEvent );
                fhirCastTopic.switchPatient( patient );
                break;
            }
            default:
                throw new NotImplementedException(  );
        }
//        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
//        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );
//        fhirCastWorkflowEvent.setId( "WS"+System.currentTimeMillis() );
//        fhirCastWorkflowEvent.setTimestamp( ""+new Date() );
//        eventReceived( fhirCastWorkflowEventEvent.getHub_topic(), fhirCastWorkflowEvent );
    }

    private Patient getPatientFromContext(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException {
        Optional<FhirCastContext> opt = fhirCastWorkflowEventEvent.getContext()
            .stream()
            .filter( fhirCastContext -> fhirCastContext.getKey().equals( "patient" ) )
            .findFirst();
        if ( opt.isPresent() ){
            return (Patient) opt.get().retrieveFhirResource();
        }
        throw new FhirCastException( "Context should contain patient" );
    }

//    public void eventReceived(String topicId, FhirCastWorkflowEvent fhirCastWorkflowEvent) throws FhirCastException {
//        //TODO
//        logger.info( "send event "+fhirCastWorkflowEvent.getEvent().getHub_event().getName()+" for "+topicId );
//        FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession( topicId );
//        fhirCastWebsubSession.eventReceived( fhirCastWorkflowEvent );
//    }

    public Map<String, String> getContext(String topicId) throws FhirCastException {
        //TODO
        logger.info( "retrieve context for "+topicId );
        FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession( topicId );
        return fhirCastWebsubSession.getContext();
    }

//    public void updateFhirCastSession(String topicId) {
//        logger.info( "update topic"+topicId );
//        FhirCastWebsubSession fhirCastWebsubSession = null;
//        try {
//            fhirCastWebsubSession = getFhirCastSession( topicId );
//        } catch ( FhirCastException e ) {
//            fhirCastWebsubSession = new FhirCastWebsubSession( topicId );
//            sessions.put( topicId, fhirCastWebsubSession );
//        }
//    }
}
