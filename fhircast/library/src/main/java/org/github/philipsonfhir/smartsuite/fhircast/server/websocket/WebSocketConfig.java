package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;//package org.github.philipsonfhir.smartsuite.fhircast.server.websocket;

import org.github.philipsonfhir.smartsuite.Prefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler socketHandler;

    @Autowired
    public WebSocketConfig( WebSocketHandler socketHandler ){
        this.socketHandler = socketHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler( socketHandler, Prefix.FHIRCAST_WEBSOCKET+"/{websocketId}" )
//            .addHandler(new SocketHandler(), Prefix.WEBSOCKET+"/{websocketId}" )
            .setAllowedOrigins( "*" );
    }
}
