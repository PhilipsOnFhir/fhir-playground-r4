package com.github.philipsonfhir.fhircast.server.websocket;

import com.github.philipsonfhir.fhircast.server.websub.service.EventChannelListener;
import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastService;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import static java.util.concurrent.TimeUnit.SECONDS;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsocketController  {


    @Autowired
    private FhirCastService fhirCastService;

    private boolean registered = false;

    private void checkRegistered(){
        if( ! registered ){
            fhirCastService.register(new WebsocketEventSender( 9080 ));
            registered = true;
        }
    }

    @MessageMapping("/fhircast/{topic}/{event}")
    @SendTo("/hub/fhircast/{topic}/{event}")
    public FhirCastWorkflowEventEvent fhircastEvent(@DestinationVariable String topic,@DestinationVariable String event, FhirCastWorkflowEventEvent fhirCastEvent) {
        System.out.println("SERVER: fhircast "+topic+fhirCastEvent.getHub_event() );
        checkRegistered();
        return fhirCastEvent;
    }

}
