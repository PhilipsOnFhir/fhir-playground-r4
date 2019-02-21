package com.github.philipsonfhir.fhircast.server.controller;

import com.github.philipsonfhir.fhircast.server.service.FhirCastContextService;
import com.github.philipsonfhir.fhircast.server.service.FhirCastTopic;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastTopicController {

    @Autowired
    private FhirCastContextService fhirCastContextService;

    @PostMapping( Prefix.FHIRCAST )
    public ResponseEntity<String> createFhirCastSession(
        HttpServletRequest request,
        @RequestBody String requestBody) {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );

        FhirCastTopic fhirCastTopic = fhirCastContextService.createTopic();
//        fhirCastWebsubService.updateFhirCastSession( fhirCastTopic.getTopic() );
        ResponseEntity<String> responseEntity = new ResponseEntity( fhirCastTopic.getTopic(), HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping( Prefix.FHIRCAST )
    public List<String> getFhirCastServices()  {
        return fhirCastContextService.getTopics().stream()
            .map( fhirCastTopic -> fhirCastTopic.getTopic() )
            .collect( Collectors.toList());
//        return  fhirCastWebsubService.getActiveFhirCastSessions().stream()
//            .map( session -> session.getTopicId())
//            .collect( Collectors.toList());
    }

    @PutMapping( Prefix.FHIRCAST+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastContextService.updateTopic( sessionId );
//        fhirCastWebsubService.updateFhirCastSession(sessionId);
    }

    @DeleteMapping( Prefix.FHIRCAST+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        fhirCastContextService.removeTopic( sessionId );
//        fhirCastWebsubService.deleteFhirCastSession(sessionId);
    }

}
