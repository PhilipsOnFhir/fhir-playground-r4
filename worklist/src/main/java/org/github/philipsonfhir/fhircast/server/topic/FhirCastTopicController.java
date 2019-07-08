package org.github.philipsonfhir.fhircast.server.topic;

import org.github.philipsonfhir.fhircast.server.Prefix;
import org.github.philipsonfhir.fhircast.server.topic.service.TopicService;
import org.github.philipsonfhir.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
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
//@CrossOrigin(origins = "*")
public class FhirCastTopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping( Prefix.FHIRCAST_TOPIC )
    public ResponseEntity<String> createFhirCastSession(
        HttpServletRequest request) {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );

        FhirCastTopic fhirCastTopic = topicService.createTopic();
        ResponseEntity<String> responseEntity = new ResponseEntity( "{ \"topicID\" : \""+fhirCastTopic.getTopic()+"\"}", HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping( Prefix.FHIRCAST_TOPIC )
    public List<String> getFhirCastServices()  {
        return topicService.getTopics().stream()
            .map( fhirCastTopic -> fhirCastTopic.getTopic() )
            .collect( Collectors.toList());
    }

    @PutMapping( Prefix.FHIRCAST_TOPIC+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        topicService.updateTopic( sessionId );
    }

    @DeleteMapping( Prefix.FHIRCAST_TOPIC+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        topicService.removeTopic( sessionId );
    }


}
