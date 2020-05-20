package org.github.philipsonfhir.smartsuite.examples.local.services;

import org.github.philipsonfhir.smartsuite.fhircast.server.topic.TopicService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class MyTopicService extends TopicService {
    public MyTopicService(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
        updateTopic("Topic1", null );
        updateTopic("Topic2", null );
        updateTopic("Topic3", null );
        updateTopic("Topic4", null );
        updateTopic("Topic5", null );
    }
}
