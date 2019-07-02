package org.github.philipsonfhir.fhircast.topic.websub;

import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.support.NotImplementedException;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastBody;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastContext;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.topic.websub.domain.FhirCastWorkflowEventEvent;
import org.github.philipsonfhir.fhircast.topic.websub.service.FhirCastWebsubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsubController {

    private final String prefix="/api/fhircast/topic";
    @Autowired
    private FhirCastWebsubService fhirCastWebsubService;

    @RequestMapping (
        method = RequestMethod.POST,
        value = prefix+"/{sessionId}/websub"
    )
    public ResponseEntity updateFhirCast(
        @PathVariable String sessionId,
        @RequestBody FhirCastBody fhirCastBody,
        @RequestParam Map<String, String> queryParams
    ) {
        ResponseEntity<String> responseEntity = new ResponseEntity( HttpStatus.ACCEPTED);
        try {
            if ( fhirCastBody.isSubscribe() ){
                fhirCastWebsubService.subscribe(sessionId, fhirCastBody.getFhirCastSessionSubscribe() );
            }
            if ( fhirCastBody.isEvent() ){
                fhirCastWebsubService.eventReceived( fhirCastBody.getFhirCastWorkflowEvent() );
            }

        } catch ( FhirCastException | NotImplementedException e) {
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return responseEntity;
    }


    @RequestMapping (
        method = RequestMethod.GET,
        value = prefix+"/{sessionId}/websub"
    )
    public ResponseEntity getFhirCastContext(
        @PathVariable String sessionId,
        @RequestParam Map<String, String> queryParams
    ) {
        ResponseEntity<FhirCastWorkflowEvent> responseEntity = new ResponseEntity( HttpStatus.ACCEPTED);

        try {
            Map<String, String> context =  fhirCastWebsubService.getContext( sessionId );
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
