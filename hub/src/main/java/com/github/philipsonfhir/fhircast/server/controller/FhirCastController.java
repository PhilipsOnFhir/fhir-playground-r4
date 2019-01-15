package com.github.philipsonfhir.fhircast.server.controller;

import com.github.philipsonfhir.fhircast.server.service.FhirCastService;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastController {
    static final String PREFIX = "fhircast";

//    @Autowired
    private FhirCastService fhirCastService = new FhirCastService();

    @PostMapping(PREFIX )
    public ResponseEntity<String> createFhirCastSession(
        HttpServletRequest request,
        @RequestBody String requestBody) {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );

        ResponseEntity<String> responseEntity = new ResponseEntity( fhirCastService.createFhirCastSession().getTopicId(), HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping(PREFIX )
    public List<String> getFhirCastServices()  {
        return  fhirCastService.getActiveFhirCastSessions().stream()
            .map( session -> session.getTopicId())
            .collect( Collectors.toList());
    }

    @PutMapping(PREFIX+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastService.updateFhirCastSession(sessionId);
    }

    @DeleteMapping(PREFIX+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastService.deleteFhirCastSession(sessionId);
    }

    ///////////////////FHIR CAST ////////////////////////////////////////
    @RequestMapping (
        method = RequestMethod.POST,
        value = PREFIX+"/{sessionId}"
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
                fhirCastService.sendEvent( sessionId, fhirCastBody.getFhirCastWorkflowEvent() );
            }

        } catch (FhirCastException e) {
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return responseEntity;
    }


    @RequestMapping (
        method = RequestMethod.GET,
        value = PREFIX+"/{sessionId}"
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
