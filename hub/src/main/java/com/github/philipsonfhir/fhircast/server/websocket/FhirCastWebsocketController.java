package com.github.philipsonfhir.fhircast.server.websocket;

import com.github.philipsonfhir.fhircast.server.websub.FhirCastService;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsocketController {


    @Autowired
    private FhirCastService fhirCastService;

    private boolean registered = false;

//    private void checkRegistered(){
//        if( ! registered ){
//            fhirCastService.register(this);
//            registered = true;
//        }
//    }

    @MessageMapping("/fhircast/{topic}/{event}")
    @SendTo("/hub/fhircast/{topic}/{event}")
    public FhirCastWorkflowEventEvent fhircastEvent(@DestinationVariable String topic,@DestinationVariable String event, FhirCastWorkflowEventEvent fhirCastEvent) {
        System.out.println("SERVER: fhircast "+topic+fhirCastEvent.getHub_event() );
        return fhirCastEvent;
    }

}
