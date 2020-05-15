package org.github.philipsonfhir.smartsuite.examples.local.services;

import org.github.philipsonfhir.smartsuite.fhircast.server.websocket.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Configuration
@EnableWebSocket
public class MyWebSocketConfig extends WebSocketConfig {

    @Autowired
    public MyWebSocketConfig(WebSocketHandler socketHandler) {
        super(socketHandler);
    }
}
