package org.github.philipsonfhir.fhircast.topic;

import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.topic.service.FhirCastTopic;
import org.github.philipsonfhir.fhircast.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin( origins = "*")
@RestController
public class TopicController {

    private final TopicService topicService;
    private final String prefix = "api/fhircast/topic";

    @Autowired
    public TopicController( TopicService topicService){
        this.topicService = topicService;
    }

    @PostMapping( prefix )
    public ResponseEntity<String> createTopic(
            HttpServletRequest request ) {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );

        FhirCastTopic fhirCastTopic = topicService.createTopic();
        ResponseEntity<String> responseEntity = new ResponseEntity( "{ \"topicID\": \""+fhirCastTopic.getTopic()+"\"}" , HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping( prefix )
    public List<String> getFhirCastServices()  {
        return topicService.getTopics().stream()
                .map( fhirCastTopic -> fhirCastTopic.getTopic() )
                .collect( Collectors.toList());
    }

    @PutMapping( prefix+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        this.topicService.updateTopic( sessionId );
    }

    @DeleteMapping( prefix+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        this.topicService.removeTopic( sessionId );
    }
}
