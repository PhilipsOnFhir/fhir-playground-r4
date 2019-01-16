package com.github.philipsonfhir.fhircast.server.websub.service;

import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.*;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.logging.Logger;

@Controller
public class FhirCastService {
    private Map<String, FhirCastSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private EventChannelListener eventChannelListener = null;

    public FhirCastService(){
        this.updateFhirCastSession( "demo" );
    }

    public FhirCastSession createFhirCastSession() {
        String sessionId = "FC"+System.currentTimeMillis();
        FhirCastSession fhirCastSession = new FhirCastSession( sessionId );

        sessions.put( sessionId, fhirCastSession );
        logger.info("create session"+sessionId);
        return fhirCastSession;
    }

    public Collection<FhirCastSession> getActiveFhirCastSessions() {
        return sessions.values();
    }

    public void deleteFhirCastSession(String sessionId)  {
        logger.info("remove session"+sessionId);
        try {
            FhirCastSession fhirCastSession = getFhirCastSession(sessionId);
            sessions.remove( sessionId );
            //TODO send remove events.
        } catch ( FhirCastException e ) {
        }
    }

    private FhirCastSession getFhirCastSession(String sessionId) throws FhirCastException {
        FhirCastSession fhirCastSession = sessions.get( sessionId );
        if ( fhirCastSession !=null ){
            return fhirCastSession;
        }
        throw new FhirCastException( "UnknownSessionId" );
    }

    public void subscribe(String sessionId, FhirCastSessionSubscribe fhirCastSessionSubscribe) throws FhirCastException {
        logger.info("subscibe session"+sessionId);
        FhirCastSession fhirCastSession = getFhirCastSession( sessionId );
        fhirCastSession.updateSubscriptions( fhirCastSessionSubscribe );
    }

    public void sendEvent(String sessionId, FhirCastWorkflowEvent fhirCastWorkflowEvent) throws FhirCastException {
        logger.info( "send event "+fhirCastWorkflowEvent.getEvent().getHub_event().getName()+" for "+sessionId );
        FhirCastSession fhirCastSession = getFhirCastSession( sessionId );
        fhirCastSession.sendEvent( fhirCastWorkflowEvent );
        if ( this.eventChannelListener!=null){
            this.eventChannelListener.sendEvent( fhirCastWorkflowEvent.getEvent() );
        }

    }

    public Map<String, String> getContext(String sessionId) throws FhirCastException {
        logger.info( "retrieve context for "+sessionId );
        FhirCastSession fhirCastSession = getFhirCastSession( sessionId );
        return fhirCastSession.getContext();
    }

    public void updateFhirCastSession(String sessionId) {
        logger.info( "update topic"+sessionId );
        FhirCastSession fhirCastSession = null;
        try {
            fhirCastSession = getFhirCastSession( sessionId );
        } catch ( FhirCastException e ) {
            fhirCastSession = new FhirCastSession( sessionId );
            sessions.put( sessionId, fhirCastSession );
        }
    }

    public void register( EventChannelListener eventChannelListener ) {
        this.eventChannelListener = eventChannelListener;
    }
}
