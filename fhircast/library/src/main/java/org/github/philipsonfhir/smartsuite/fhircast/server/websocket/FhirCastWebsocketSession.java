package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.server.websub.MostRecentEventHandler;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

import java.util.HashMap;
import java.util.logging.Logger;

public class FhirCastWebsocketSession {
    private final String topicId;
    private final SocketHandler websocketHandler;
    private final FhirCastService fhirCastService;
    private HashMap<String, FhirCastWebsocketClient> clients = new HashMap<>();
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private final MostRecentEventHandler mostRecentEventHandler = new MostRecentEventHandler();

    public FhirCastWebsocketSession( FhirCastService fhirCastService, String topicId, SocketHandler websocketHandler) {
        this.topicId = topicId;
        this.websocketHandler = websocketHandler;
        this.fhirCastService = fhirCastService;
    }

    public String updateSubscriptions(SubscriptionUpdate subscriptionUpdate) throws FhirCastException {
        FhirCastWebsocketClient fhircastWebsocketClient = null;
        if ( subscriptionUpdate.getHub_channel_endpoint()==null ){
            fhircastWebsocketClient = new FhirCastWebsocketClient( fhirCastService, websocketHandler, mostRecentEventHandler, topicId );
            this.clients.put( fhircastWebsocketClient.getEndpointId(), fhircastWebsocketClient );
        } else {
            fhircastWebsocketClient = this.clients.get( fhircastWebsocketClient.getEndpointId() );
        }
        fhircastWebsocketClient.updateSubscription( subscriptionUpdate );
        if ( fhircastWebsocketClient == null ){
            throw new FhirCastException("Unknown EndpointID "+ fhircastWebsocketClient.getEndpointId());
        }
        return fhircastWebsocketClient.getEndpointId();
    }

    public synchronized SendEventResult newFhirCastContextEvent(ContextEvent fhirCastEvent) {
        this.mostRecentEventHandler.newEvent(fhirCastEvent);
        SendEventResult sendEventResult = new SendEventResult();
        clients.forEach((key, client) -> {
            try {
                logger.info("sending event " + fhirCastEvent );
                client.sendEvent(fhirCastEvent);
            } catch (FhirCastException e) {
                logger.warning("Sending event failed " + e);
                sendEventResult.addError(key + "-" + e.getMessage() + ";");
            }
        });
        return sendEventResult;
    }
}
