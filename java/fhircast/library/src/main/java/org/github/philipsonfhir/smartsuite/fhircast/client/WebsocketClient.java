package org.github.philipsonfhir.smartsuite.fhircast.client;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebsocketClient {
    private Session session;
    WebSocketContainer container;

    public WebsocketClient( String uri ){
        System.out.println("Connect to "+uri);
        try{
            container=ContainerProvider.
                    getWebSocketContainer();
            Session session = container.connectToServer(this, new URI(uri));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session=session;
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("new Message received: "+ message);
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch ( IOException ex) {

        }
    }
}
