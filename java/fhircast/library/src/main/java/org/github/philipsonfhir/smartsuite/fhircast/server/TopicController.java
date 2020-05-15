package org.github.philipsonfhir.smartsuite.fhircast.server;

import org.github.philipsonfhir.smartsuite.Prefix;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.FhirCastTopic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.TopicService;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain.Topic;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain.TopicList;
import org.github.philipsonfhir.smartsuite.fhircast.server.websub.FhirCastWebhookService;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RestController
//@CrossOrigin(origins = "*")
public class TopicController {

    private final TopicService topicService;
    private final FhirCastWebhookService websubService;
//    private final String fhirServerUrl;
//    private final FhirServer fhirServer;
    //     private TopicService topicService;
//    @Value("${proxy.fhirserver.url}") String fhirServerUrl;
//    FhirContext ourCtx = FhirContext.forR4();

    @Autowired
    public TopicController(
            TopicService topicService,
            FhirCastWebhookService websubService
//            @Value("${fhircast.fhirserver.url:#{null}}") String fhirServerUrl
    ){
        this.topicService = topicService;
        this.websubService = websubService;
//        this.fhirServerUrl = fhirServerUrl;
//        this.fhirServer = new FhirServer(fhirServerUrl);
    }

    @GetMapping( Prefix.TOPIC)
    public TopicList getFhirCastSessions( HttpServletRequest request )  {
        TopicList topicList = new TopicList();
        topicList.setHubUrl( getHubUrl(request) );

        topicService.getTopics(null).stream()
                .forEach( fhirCastTopic -> topicList.getTopic().add(fhirCastTopic.getTopic() ));
        return topicList;
    }

    @PostMapping( Prefix.TOPIC)
    public ResponseEntity<Topic> createFhirCastSession( HttpServletRequest request ) throws FhirCastException {
//        String host = request.getHeader("host");
//        String hubUrl = request.getServerName()+":"+request.getServerPort()+Prefix.FHIRCAST;
        String hubUrl = getHubUrl(request);
//        String host = request.ge0tRequestURL().toString();
        FhirCastTopic fhirCastTopic = topicService.createTopic( null );
        Topic topic = new Topic();
        topic.setTopic( fhirCastTopic.getTopic() );
        topic.setFhirUrl( getFhirUrl( request, fhirCastTopic.getTopic() ));
        topic.setHubUrl( hubUrl );
//        ResponseEntity<String> responseEntity = new ResponseEntity( "{ \"topic\" : \""+fhirCastTopic.getTopic()+"\"}", HttpStatus.CREATED );
        ResponseEntity<Topic> responseEntity = new ResponseEntity( topic, HttpStatus.CREATED );
        return responseEntity;
    }

    @PutMapping( Prefix.TOPIC +"/{topicId}" )
    public ResponseEntity<Topic> updateFhirCastService(
            @PathVariable String topicId,
            Principal principal,
            HttpServletRequest request
    ) throws FhirCastException {
        FhirCastTopic fhirCastTopic = topicService.updateTopic( topicId, null);
        Topic topic = new Topic();
        topic.setHubUrl( getHubUrl(request) );
        topic.setTopic( fhirCastTopic.getTopic() );
        topic.setFhirUrl( getFhirUrl( request, topicId ));
        topic.setHubUrl( getHubUrl( request ) );
        ResponseEntity<Topic> responseEntity = new ResponseEntity( topic, HttpStatus.CREATED );
        return responseEntity;
    }

    @DeleteMapping( Prefix.TOPIC +"/{topicId}" )
    public void removeFhirCastService( @PathVariable String topicId) throws FhirCastException {
        topicService.removeTopic( topicId );
    }

    @GetMapping( Prefix.TOPIC +"/{topicId}" )
    public ResponseEntity<Topic> getTopicInformation(@PathVariable String topicId,
                                                     HttpServletRequest request ){
        FhirCastTopic fhirCastTopic = topicService.updateTopic( topicId, null);
        Topic topic = new Topic();
        topic.setTopic( fhirCastTopic.getTopic() );
        topic.setFhirUrl( getFhirUrl( request, fhirCastTopic.getTopic() ));
        topic.setHubUrl( getHubUrl(request) );
//        ResponseEntity<String> responseEntity = new ResponseEntity( "{ \"topic\" : \""+fhirCastTopic.getTopic()+"\"}", HttpStatus.CREATED );
        ResponseEntity<Topic> responseEntity = new ResponseEntity( topic, HttpStatus.CREATED );
        return responseEntity;
    }

    private String getHubUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf(Prefix.TOPIC)) + Prefix.FHIRCAST;
    }

    protected String getFhirUrl(HttpServletRequest request, String topic) {
        return null;
    }
}
