package com.github.philipsonfhir.fhircast.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.philipsonfhir.fhircast.server.websub.model.FhirCastWorkflowEvent;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@ClientEndpoint
public class WebsocketCommunicationListener  {
    private String websocketAdress;
    private final FhirCastWebsubClient client;
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private WebSocketContainer container;
    private Session session;

    public WebsocketCommunicationListener( FhirCastWebsubClient fhirCastWebSocketClient)
            throws URISyntaxException, IOException, DeploymentException {
        this.client = fhirCastWebSocketClient;
    }

    public void connect( String websocketAdress ) throws URISyntaxException, IOException, DeploymentException {
        this.websocketAdress = websocketAdress;

        logger.info("Connect to "+websocketAdress );
        container= ContainerProvider.
                getWebSocketContainer();
        session = container.connectToServer(this, new URI(websocketAdress));
    }

//    @OnOpen
//    public void onOpen(Session session){
//        this.session=session;
//    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("new Message : "+ message);
        ObjectMapper objectMapper = new ObjectMapper();
        FhirCastWorkflowEvent fhirCastWorkflowEvent = objectMapper.readValue( message, FhirCastWorkflowEvent.class );
        if ( fhirCastWorkflowEvent.getSubscription()!=null ) {
            sendMessage("ChallengeResponse");
        } else {
            this.client.newEvent( fhirCastWorkflowEvent );
        }
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch ( IOException ex) {

        }
    }
}
