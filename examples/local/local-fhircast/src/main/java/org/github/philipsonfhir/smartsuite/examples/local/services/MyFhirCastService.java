package org.github.philipsonfhir.smartsuite.examples.local.services;

import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.websocket.FhirCastWebsocketService;
import org.github.philipsonfhir.smartsuite.fhircast.server.websub.FhirCastWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class MyFhirCastService extends FhirCastService {
    @Autowired
    public MyFhirCastService(FhirCastWebhookService websubService, FhirCastWebsocketService websocketService, ApplicationEventPublisher applicationEventPublisher) {
        super(websubService, websocketService, applicationEventPublisher);
    }
}
