package org.github.philipsonfhir.smartsuite.fhircast.server.websub;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Service
public class FhirCastWebhookService {

    private Map<String, FhirCastWebhookSession> sessions = new TreeMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public FhirCastWebhookService(){
    }

//    private FhirCastWebhookSession getFhirCastSession(String topicId) {
//        FhirCastWebhookSession fhirCastWebhookSession = sessions.get( topicId );
//        if ( fhirCastWebhookSession ==null ){
//            logger.info( "Create WebSub session for topic "+topicId );
//            fhirCastWebhookSession = new FhirCastWebhookSession( topicId );
//            sessions.put( topicId, fhirCastWebhookSession);
//        }
//        return fhirCastWebhookSession;
//    }

    public void updateSubscriptions(SubscriptionUpdate body) throws FhirCastException {
        String topicId = body.getHub_topic();
        FhirCastWebhookSession fhirCastWebhookSession = getWebSubSession( topicId );
        fhirCastWebhookSession.updateSubscriptions( body );
    }

    private FhirCastWebhookSession getWebSubSession(String topicId ) {
        FhirCastWebhookSession fhirCastWebhookSession = sessions.get( topicId );
        if ( fhirCastWebhookSession ==null ){
            logger.info("Create websub session for topic "+topicId );
            fhirCastWebhookSession = new FhirCastWebhookSession( topicId );
            sessions.put( topicId, fhirCastWebhookSession);
        }
        return fhirCastWebhookSession;
    }

    public SendEventResult sendEvent(ContextEvent fhirCastEvent) {
        FhirCastWebhookSession fhirCastWebhookSession = getWebSubSession( fhirCastEvent.getEvent().getHub_topic() );
        logger.info("sending event "+fhirCastEvent );
        return fhirCastWebhookSession.newFhirCastContextEvent( fhirCastEvent );
    }
}
