package com.github.philipsonfhir.fhircast.server.controller;

import com.github.philipsonfhir.fhircast.server.service.FhirCastService;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastBody;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastContext;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FhirCastTopicController {

    @Autowired
    private FhirCastService fhirCastService = new FhirCastService();

    @PostMapping( Prefix.FHIRCAST )
    public ResponseEntity<String> createFhirCastSession(
        HttpServletRequest request,
        @RequestBody String requestBody) {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );

        ResponseEntity<String> responseEntity = new ResponseEntity( fhirCastService.createFhirCastSession().getTopicId(), HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping( Prefix.FHIRCAST )
    public List<String> getFhirCastServices()  {
        return  fhirCastService.getActiveFhirCastSessions().stream()
            .map( session -> session.getTopicId())
            .collect( Collectors.toList());
    }

    @PutMapping( Prefix.FHIRCAST+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastService.updateFhirCastSession(sessionId);
    }

    @DeleteMapping( Prefix.FHIRCAST+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastService.deleteFhirCastSession(sessionId);
    }

}
