package org.github.philipsonfhir.fhircast.server.context;

import org.github.philipsonfhir.fhircast.server.Prefix;
import org.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubService;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastContext;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEventEvent;
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
public class FhirCastRestController {

    @Autowired
    private FhirCastWebsubService fhirCastWebsubService;

    @RequestMapping (
        method = RequestMethod.GET,
        value = Prefix.FHIRCAST+"/{topicId}/"+Prefix.CONTEXT
    )
    public ResponseEntity getFhirCastContext(
        @PathVariable String topicId,
        @RequestParam Map<String, String> queryParams
    ) {
        ResponseEntity<FhirCastWorkflowEvent> responseEntity = new ResponseEntity( HttpStatus.ACCEPTED);

        try {
            Map<String, String> context =  fhirCastWebsubService.getContext(topicId);
            FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
            fhirCastWorkflowEvent.setId( UUID.randomUUID().toString() );
            fhirCastWorkflowEvent.setTimestamp( ""+new Date() );

            FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = new FhirCastWorkflowEventEvent();
            fhirCastWorkflowEventEvent.setHub_topic(topicId);
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
