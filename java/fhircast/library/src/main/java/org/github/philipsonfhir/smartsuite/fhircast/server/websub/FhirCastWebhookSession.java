package org.github.philipsonfhir.smartsuite.fhircast.server.websub;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FhirCastWebhookSession {
    private final String topicId;
    private final Map<String, FhirCastWebhookClient> clients = new HashMap<>();
    private final Logger logger;
    private final MostRecentEventHandler mostRecentEventHandler = new MostRecentEventHandler();

    public FhirCastWebhookSession(String topicId) {
        this.topicId = topicId;
        logger = Logger.getLogger(this.getClass().getName()+"-"+topicId);
    }

    SendEventResult newFhirCastContextEvent(ContextEvent contextEvent) {
        mostRecentEventHandler.newEvent( contextEvent );

        SendEventResult sendEventResult = new SendEventResult();
        clients.forEach((key, client) -> {
            try {
                logger.info("sending contextEvent " + contextEvent);
                client.sendEvent(contextEvent);
            } catch (FhirCastException e) {
                logger.warning("Sending contextEvent failed " + e);
                sendEventResult.addError(key + "-" + e.getMessage() + ";");
            }
        });
        return sendEventResult;
    }

    void updateSubscriptions(SubscriptionUpdate body) throws FhirCastException {
        FhirCastWebhookClient fhirCastWebhookClient = clients.get( body.getHub_callback() );
        if ( fhirCastWebhookClient==null ){
            fhirCastWebhookClient = new FhirCastWebhookClient( body.getHub_callback(), mostRecentEventHandler );
            clients.put( body.getHub_callback(), fhirCastWebhookClient );
        }
        fhirCastWebhookClient.updateSubscriptions( body );
    }

}
