package com.github.philipsonfhir.fhircast.server.websub.service;

import com.github.philipsonfhir.fhircast.server.service.FhirCastContextService;
import com.github.philipsonfhir.fhircast.server.service.FhirCastTopic;
import com.github.philipsonfhir.fhircast.server.service.FhirCastTopicEvent;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.NotImplementedException;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastSessionSubscribe;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Controller
public class FhirCastWebsubService implements ApplicationListener<FhirCastTopicEvent> {
    @Autowired
    private FhirCastContextService fhirCastContextService;

    private Map<String, FhirCastWebsubSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Collection<FhirCastWebsubSession> getActiveFhirCastSessions() {
        return sessions.values();
    }


    private FhirCastWebsubSession getFhirCastSession(String topicId) throws FhirCastException {
        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( topicId );
        FhirCastWebsubSession fhirCastWebsubSession = sessions.get( topicId );
        if ( fhirCastWebsubSession ==null ){
            logger.info( "Create WebSub session for topic "+topicId );
            fhirCastWebsubSession = new FhirCastWebsubSession( topicId );
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

    private void eventReceived(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException, NotImplementedException {
        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( fhirCastWorkflowEventEvent.getHub_topic() );
        switch ( fhirCastWorkflowEventEvent.getHub_event() ) {
            case OPEN_PATIENT_CHART: {
                Patient patient = fhirCastWorkflowEventEvent.retrievePatientFromContext();
                fhirCastTopic.openPatientChart( patient );
                break;
            }
            case CLOSE_PATIENT_CHART:
                fhirCastTopic.closeCurrent();
                break;
            case SWITCH_PATIENT_CHART: {
                Patient patient = fhirCastWorkflowEventEvent.retrievePatientFromContext();
                fhirCastTopic.switchPatient( patient );
                break;
            }
            default:
                throw new NotImplementedException();
        }
    }

    public Map<String, String> getContext(String topicId) throws FhirCastException {
        //TODO
        logger.info( "retrieve context for "+topicId );
        FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession( topicId );
        return fhirCastWebsubSession.getContext();
    }

    @Override
    public void onApplicationEvent(FhirCastTopicEvent event) {
        try {
            getFhirCastSession( event.getTopic() ).newFhirCastTopicEvent(event);
        } catch ( FhirCastException e ) {
            e.printStackTrace();
        }
    }

}