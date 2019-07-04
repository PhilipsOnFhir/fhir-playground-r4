package org.github.philipsonfhir.fhircast.server.websub.service;

import org.github.philipsonfhir.fhircast.server.topic.service.TopicService;
import org.github.philipsonfhir.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.fhircast.server.topic.service.FhirCastTopicEvent;
import org.github.philipsonfhir.fhircast.server.websub.service.websocket.SocketHandler;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.support.NotImplementedException;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEventEvent;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Service
public class FhirCastWebsubService implements ApplicationListener<FhirCastTopicEvent> {

    private final SocketHandler socketHandler;
    private Map<String, FhirCastWebsubSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private TopicService topicService;

    @Autowired
    public FhirCastWebsubService(TopicService fhirCastContextService, SocketHandler socketHandler){
        this.topicService = fhirCastContextService;
        this.socketHandler = socketHandler;
    }

    public Collection<FhirCastWebsubSession> getActiveFhirCastSessions() {
        return sessions.values();
    }


    private FhirCastWebsubSession getFhirCastSession(String topicId) throws FhirCastException {
//        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( topicId );

        FhirCastWebsubSession fhirCastWebsubSession = sessions.get( topicId );
        if ( fhirCastWebsubSession ==null ){
            logger.info( "Create WebSub session for topic "+topicId );
            fhirCastWebsubSession = new FhirCastWebsubSession( topicId, socketHandler );
            sessions.put( topicId, fhirCastWebsubSession );
        }
        return fhirCastWebsubSession;
    }

    public String subscribe(String sessionId, FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException, NotImplementedException {
        logger.info("subscibe session"+sessionId);
        FhirCastWebsubSession fhirCastWebsubSession = getFhirCastSession( sessionId );
        String response = fhirCastWebsubSession.updateSubscriptions( fhirCastSessionSubscribe );

        return response;
    }

    public void eventReceived(FhirCastWorkflowEvent fhirCastWorkflowEvent ) throws FhirCastException, NotImplementedException {
        eventReceived( fhirCastWorkflowEvent.getEvent() );
    }

    private void eventReceived(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException, NotImplementedException {
        FhirCastTopic fhirCastTopic = this.topicService.getTopic( fhirCastWorkflowEventEvent.getHub_topic() );
        switch ( fhirCastWorkflowEventEvent.getHub_event() ) {
            case OPEN_PATIENT_CHART: {
                Patient patient = fhirCastWorkflowEventEvent.retrievePatientFromContext();
                fhirCastTopic.openPatientChart( patient );
                break;
            }
            case CLOSE_PATIENT_CHART: {
                Patient patient = fhirCastWorkflowEventEvent.retrievePatientFromContext();
                fhirCastTopic.close( patient );
                break;
            }
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
