package org.github.philipsonfhir.worklist.fhircast.server.launch;

import org.github.philipsonfhir.worklist.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LaunchService {
    private final TopicService topicService;

    @Autowired
    public LaunchService(TopicService topicService ){
        this.topicService = topicService;
    }

    public LaunchContext getLaunchContext( String launchId ) {
        FhirCastTopic fhirCastTopic = this.topicService.getTopicFromLaunch(launchId);
        LaunchContext launchContext = fhirCastTopic.getFhirCastTopicStudyOrPatient(launchId);
        return launchContext;
    }
}
