package com.github.philipsonfhir.fhircast.server.websocket;

import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubService;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.NotImplementedException;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsocketController  {

    private Logger logger = Logger.getLogger( this.getClass().getName());
    @Autowired
    private FhirCastWebsubService fhirCastWebsubService;


    @MessageMapping("/fhircast/{topic}/{event}")
    @SendTo("/hub/fhircast/{topic}/{event}")
    public FhirCastWorkflowEventEvent fhircastEvent(@DestinationVariable String topic,@DestinationVariable String event, FhirCastWorkflowEventEvent fhirCastEvent) throws NotImplementedException {
        logger.info("WebSocket event "+topic+"/"+event+"/"+fhirCastEvent );
        try {
            fhirCastWebsubService.eventReceived( fhirCastEvent );
        } catch ( FhirCastException e ) {
//            return null;
        }
        return fhirCastEvent;
    }

}
