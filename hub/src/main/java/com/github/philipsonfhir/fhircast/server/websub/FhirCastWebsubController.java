package com.github.philipsonfhir.fhircast.server.websub;

import com.github.philipsonfhir.fhircast.server.Prefix;
import com.github.philipsonfhir.fhircast.server.websocket.WebsocketEventSender;
import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastService;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsubController {

    @Autowired
    private FhirCastService fhirCastService;

    @Autowired
    private WebsocketEventSender websocketService;

    @RequestMapping (
        method = RequestMethod.POST,
        value = Prefix.FHIRCAST+"/{sessionId}/"+Prefix.WEBSUB
    )
    public ResponseEntity updateFhirCast(
        @PathVariable String sessionId,
        @RequestBody FhirCastBody fhirCastBody,
        @RequestParam Map<String, String> queryParams
    ) {
        ResponseEntity<String> responseEntity = new ResponseEntity( HttpStatus.ACCEPTED);
        try {
            if ( fhirCastBody.isSubscribe() ){
                fhirCastService.subscribe(sessionId, fhirCastBody.getFhirCastSessionSubscribe() );
            }
            if ( fhirCastBody.isEvent() ){
                websocketService.sendEvent( fhirCastBody.getEvent() );
                fhirCastService.sendEvent( sessionId, fhirCastBody.getFhirCastWorkflowEvent() );
            }

        } catch (FhirCastException e) {
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return responseEntity;
    }


    @RequestMapping (
        method = RequestMethod.GET,
        value = Prefix.FHIRCAST+"/{sessionId}/"+Prefix.WEBSUB
    )
    public ResponseEntity getFhirCastContext(
        @PathVariable String sessionId,
        @RequestParam Map<String, String> queryParams
    ) {
        ResponseEntity<FhirCastWorkflowEvent> responseEntity = new ResponseEntity( HttpStatus.ACCEPTED);

        try {
            Map<String,String> context =  fhirCastService.getContext( sessionId );
            FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
            fhirCastWorkflowEvent.setId( UUID.randomUUID().toString() );
            fhirCastWorkflowEvent.setTimestamp( ""+new Date() );

            FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
            fhirCastWorkflowEventEvent.setHub_topic( sessionId );
            fhirCastWorkflowEventEvent.setContext( new ArrayList<>(  ) );

            context.entrySet().stream()
                .forEach( stringStringEntry -> {
                    FhirCastContext fhirCastContext = new FhirCastContext();
                    fhirCastContext.setKey( stringStringEntry.getKey() );
                    fhirCastContext.setResource( stringStringEntry.getValue() );
                    fhirCastWorkflowEventEvent.getContext().add(  fhirCastContext );
                } );
            fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );
            responseEntity = new ResponseEntity( fhirCastWorkflowEvent, HttpStatus.ACCEPTED );

        } catch (FhirCastException e) {
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return responseEntity;
    }
}
