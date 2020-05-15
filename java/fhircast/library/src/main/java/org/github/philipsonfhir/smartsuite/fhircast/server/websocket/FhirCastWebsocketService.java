package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Service
public class FhirCastWebsocketService {

    private final SocketHandler websocketHandler;
    private Map<String, FhirCastWebsocketSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private FhirCastService fhirCastService;

    @Autowired
    public FhirCastWebsocketService( SocketHandler socketHandler ){
        this.websocketHandler = socketHandler;
    }

    public void setFhircastService( FhirCastService fhircastService ){
        this.fhirCastService = fhircastService;
    }

    private FhirCastWebsocketSession getFhirCastSession(String topicId) {
        FhirCastWebsocketSession fhirCastWebsocketSession = sessions.get( topicId );
        if ( fhirCastWebsocketSession ==null ){
            logger.info( "Create WebSub session for topic "+topicId );
            fhirCastWebsocketSession = new FhirCastWebsocketSession( fhirCastService, topicId, websocketHandler );
            sessions.put( topicId, fhirCastWebsocketSession);
        }
        return fhirCastWebsocketSession;
    }


    public String updateSubscriptions(SubscriptionUpdate body) throws FhirCastException {
        String topicId = body.getHub_topic();
        FhirCastWebsocketSession fhirCastWebsocketSession = getWebSubSession(topicId);
        String websocketId = fhirCastWebsocketSession.updateSubscriptions(body);
        return websocketId;
    }

    private FhirCastWebsocketSession getWebSubSession(String topicId ) {
        FhirCastWebsocketSession fhirCastWebsocketSession = sessions.get( topicId );
        if ( fhirCastWebsocketSession ==null ){
            logger.info("Create websub session for topic "+topicId );
            fhirCastWebsocketSession = new FhirCastWebsocketSession( fhirCastService, topicId, websocketHandler);
            sessions.put( topicId, fhirCastWebsocketSession);
        }
        return fhirCastWebsocketSession;
    }

    public SendEventResult sendEvent(ContextEvent fhirCastEvent) {
        FhirCastWebsocketSession fhirCastWebsocketSession = getWebSubSession( fhirCastEvent.getEvent().getHub_topic() );
        logger.info("sending event "+fhirCastEvent );
        return fhirCastWebsocketSession.newFhirCastContextEvent( fhirCastEvent );
    }
}
