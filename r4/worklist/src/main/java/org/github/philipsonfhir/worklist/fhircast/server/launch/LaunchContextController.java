package org.github.philipsonfhir.worklist.fhircast.server.launch;

import ca.uhn.fhir.context.FhirContext;
import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.github.philipsonfhir.worklist.fhircast.server.topic.domainmodel.FhirChange;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class LaunchContextController {
    private Logger logger = Logger.getLogger(LaunchContextController.class.getName());
    private FhirContext ourCtx = FhirContext.forR4();
    private TopicService topicService;

    @Autowired
    public LaunchContextController( TopicService topicService ){
        this.topicService = topicService;
    }

    @RequestMapping (
            method = RequestMethod.GET,
            value = Prefix.FHIRCAST_LAUNCH+"/{launchId}/context"
    )
    public List<FhirChange> getChanges(
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String launchId
    ) {
        return getLauchContext(launchId).getChanges();
    }



    //////////////////////////////////////////////////////////////////////////////////
    private LaunchContext getLauchContext( String launchId ){
        FhirCastTopic fhirCastTopic = this.topicService.getTopicFromLaunch(launchId);
        LaunchContext launchContext = fhirCastTopic.getFhirCastTopicStudyOrPatient(launchId);
        return launchContext;
    }
}
