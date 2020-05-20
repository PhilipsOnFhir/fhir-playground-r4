package org.github.philipsonfhir.smartsuite.fhircast.client.websocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebsocketClientTest {
    //    private final String uri="ws://localhost:9080/chat/fu";
    private final String uri = "ws://echo.websocket.org";
//    private final String uri="ws://localhost:9080/fhircast/demo/websocket";
    private Session session;
    //    private ClientWindow clientWindow;
    WebSocketContainer container;

    public static void main(String args[]) {
        new WebsocketClientTest();
    }

    WebsocketClientTest(){
        System.out.println("Connect to "+uri);
        try{
            container=ContainerProvider.
                    getWebSocketContainer();
            Session session = container.connectToServer(this, new URI(uri));

            int i = 0;
            while( true ){
                Thread.sleep( 2000 );
                System.out.println(" "+i);
                sendMessage( "Message " +i );
                i++;
            }

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
