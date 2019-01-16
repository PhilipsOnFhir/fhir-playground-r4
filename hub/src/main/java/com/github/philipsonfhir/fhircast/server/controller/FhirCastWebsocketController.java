package com.github.philipsonfhir.fhircast.server.controller;

import com.github.philipsonfhir.fhircast.server.service.EventChannelListener;
import com.github.philipsonfhir.fhircast.server.service.FhirCastService;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsocketController implements EventChannelListener {


    @Autowired
    private FhirCastService fhirCastService;

    @Autowired
    private SimpMessagingTemplate template;

    private boolean registered = false;

    private void checkRegistered(){
        if( ! registered ){
            fhirCastService.register(this);
            registered = true;
        }
    }

    @SubscribeMapping("/{topic}/{event}")
    public void subscribeReceived( @DestinationVariable String topic, @DestinationVariable String event) throws Exception {
        System.out.println("subscribe received");
        checkRegistered();
    }


    @MessageMapping("/{topic}/{event}")
    @SendTo("/client/{topic}/{event}")
    public void messageReceived( @DestinationVariable String topic, @DestinationVariable String event, String message) throws Exception {
        System.out.println("message received "+topic+" "+event+" "+message);
        checkRegistered();
    }


    @Override
    public void sendEvent(FhirCastWorkflowEvent event) {
        System.out.println("Fire");
        this.template.convertAndSend("/client/demo/patient-open", "EventFromMe");
    }
}
