package org.github.philipsonfhir.worklist.fhircast.server.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.TopicService;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.github.philipsonfhir.smartonfhir.basic.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RestController
//@CrossOrigin(origins = "*")
public class FhirCastTopicController {

    private final TopicService topicService;
    private final FhirServer fhirServer;
    //     private TopicService topicService;
    @Value("${proxy.fhirserver.url}") String fhirServerUrl;
    FhirContext ourCtx = FhirContext.forR4();

    @Autowired
    public FhirCastTopicController(TopicService topicService, @Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        this.topicService = topicService;
        this.fhirServer = new FhirServer(fhirServerUrl);
    }

    @GetMapping( Prefix.FHIRCAST )
    public String getPractioner( Principal principal ) throws FhirCastException {
        Practitioner practitioner = getPractitioner( principal.getName() );
        return ourCtx.newJsonParser().encodeResourceToString( practitioner );
    }


    @PostMapping( Prefix.FHIRCAST_TOPIC )
    public ResponseEntity<String> createFhirCastSession(
        HttpServletRequest request,
        Principal principal
    ) throws FhirCastException {
        System.out.println( request.getLocalAddr() );
        System.out.println( request.getLocalName() );
        Practitioner practitioner = getPractitioner( principal.getName() );
        FhirCastTopic fhirCastTopic = topicService.createTopic( practitioner );
        ResponseEntity<String> responseEntity = new ResponseEntity( "{ \"topicID\" : \""+fhirCastTopic.getTopic()+"\"}", HttpStatus.CREATED );

        return responseEntity;
    }

    @GetMapping( Prefix.FHIRCAST_TOPIC )
    public List<String> getFhirCastServices( Principal principal )  {

        return topicService.getTopics(principal.getName()).stream()
            .map( fhirCastTopic -> fhirCastTopic.getTopic() )
            .collect( Collectors.toList());
    }

    @PutMapping( Prefix.FHIRCAST_TOPIC+"/{sessionId}" )
    public void updateFhirCastService( @PathVariable String sessionId, Principal principal ) throws FhirCastException {
        topicService.updateTopic( sessionId,  getPractitioner(principal.getName()) );
    }

    @DeleteMapping( Prefix.FHIRCAST_TOPIC+"/{sessionId}" )
    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
        topicService.removeTopic( sessionId );
    }

    @PostMapping( Prefix.FHIRCAST_TOPIC+"/{sessionId}" )
    public String createFhirCastTopicLaunch(
            @PathVariable String sessionId,
            @RequestBody  String launchResource
    ) throws FhirCastException {
        String launch = this.topicService.getTopic(sessionId).openLaunch( ourCtx.newJsonParser().parseResource( launchResource ) );
        return launch;
    }

    private Practitioner getPractitioner(String name) throws FhirCastException {
        Practitioner practitioner;
        Map<String, String > param = new HashMap<>();
        param.put("identifier",name);
        IBaseResource iBaseResource = this.fhirServer.doSearch( ResourceType.Practitioner.name(), param );

        if( ((Bundle) iBaseResource).getResourceType() == ResourceType.Bundle ){
            Bundle bundle = (Bundle)iBaseResource;
            if ( bundle.getEntry().isEmpty() ){
                practitioner = new Practitioner()
                        .addIdentifier( new Identifier()
                                .setSystem("userid")
                                .setValue(name)
                        )
                        .addName( new HumanName()
                                .setFamily(name)
                        )
                ;
                MethodOutcome methodOutcome = fhirServer.doPost( practitioner );
                practitioner = (Practitioner) methodOutcome.getResource();
            } else {
                practitioner = (Practitioner) bundle.getEntryFirstRep().getResource();
            }
        } else {
            throw new FhirCastException("Unexpected return type "+((Bundle) iBaseResource).getResourceType() );
        }

        return practitioner;
    }
}
