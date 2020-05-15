package org.github.philipsonfhir.smartsuite.examples.local.services;

import org.github.philipsonfhir.smartsuite.fhircast.server.websocket.FhirCastWebsocketService;
import org.github.philipsonfhir.smartsuite.fhircast.server.websocket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MyFhirCastWebsocketService extends FhirCastWebsocketService {
    @Autowired
    public MyFhirCastWebsocketService(SocketHandler socketHandler) {
        super( socketHandler);
    }
}
