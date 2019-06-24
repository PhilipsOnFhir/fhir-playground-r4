package com.github.philipsonfhir.fhircast.server.websub;

import com.github.philipsonfhir.fhircast.server.Prefix;
import com.github.philipsonfhir.fhircast.server.websub.service.websocket.SocketHandler;
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
            .addHandler( socketHandler, Prefix.WEBSOCKET+"/{websocketId}" )
//            .addHandler(new SocketHandler(), Prefix.WEBSOCKET+"/{websocketId}" )
            .setAllowedOrigins( "*" );
    }
}