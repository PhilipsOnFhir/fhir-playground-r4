package org.github.philipsonfhir.worklist.fhircast.server.websub.service.websocket;

import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Autowired
    public WebSocketConfig( SocketHandler socketHandler ){
        this.socketHandler = socketHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler( socketHandler, Prefix.FHIRCAST_WEBSOCKET+"/{websocketId}" )
//            .addHandler(new SocketHandler(), Prefix.WEBSOCKET+"/{websocketId}" )
            .setAllowedOrigins( "*" );
    }
}
