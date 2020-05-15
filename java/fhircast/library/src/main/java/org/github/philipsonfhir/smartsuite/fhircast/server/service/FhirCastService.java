package org.github.philipsonfhir.smartsuite.fhircast.server.service;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.WorkflowEventFactory;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopicEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.websocket.FhirCastWebsocketService;
import org.github.philipsonfhir.smartsuite.fhircast.server.websub.FhirCastWebhookService;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FhirCastService implements ApplicationListener<FhirCastTopicEvent> {
    private final FhirCastWebhookService websubService;
    private final FhirCastWebsocketService websocketService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public FhirCastService(FhirCastWebhookService websubService, FhirCastWebsocketService websocketService, ApplicationEventPublisher applicationEventPublisher){
        this.websubService = websubService;
        this.websocketService = websocketService;
        websocketService.setFhircastService( this );
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String subscribeOrUnsubscribe(SubscriptionUpdate subscriptionRequest ) throws FhirCastException {
        String str="";
        switch (subscriptionRequest.getHub_channel_type()) {
            // TODO replace strings with enum or constant
            case "webhook":
                websubService.updateSubscriptions(subscriptionRequest);
                break;
            case "websocket":
                str= websocketService.updateSubscriptions(subscriptionRequest);
                break;
            default:
                throw new FhirCastException("Topic does not exist");
        }
        return str;
    }

    public SendEventResult sendEvent(ContextEvent contextEvent)  {
        SendEventResult sendEventResult = new SendEventResult( );

        sendEventResult.update( websubService.sendEvent(contextEvent) );
        sendEventResult.update( websocketService.sendEvent(contextEvent) );
        InternalContextEvent internalContextEvent = new InternalContextEvent( this, contextEvent );
        applicationEventPublisher.publishEvent(internalContextEvent);

        if ( sendEventResult.hasErrorOccurred() && !contextEvent.getEvent().getHub_event().equals( "syncerror" )){
            sendEvent( WorkflowEventFactory.createSyncErrorEvent( contextEvent, sendEventResult ));
        }
        return sendEventResult;
    }

    @Override
    public void onApplicationEvent(  @NonNull FhirCastTopicEvent fhirCastTopicEvent) {
        System.out.println(fhirCastTopicEvent);
        if ( fhirCastTopicEvent.getEventType().equals(FhirCastTopicEvent.EventType.LOGOUT)) {
            sendEvent(WorkflowEventFactory.createLogoutEvent( fhirCastTopicEvent.getTopic() ));
        }
    }
}
