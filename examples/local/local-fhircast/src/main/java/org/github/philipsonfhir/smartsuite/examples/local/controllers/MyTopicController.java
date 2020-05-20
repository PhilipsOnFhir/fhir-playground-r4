package org.github.philipsonfhir.smartsuite.examples.local.controllers;

import org.github.philipsonfhir.smartsuite.examples.local.services.MyFhirCastWebhookService;
import org.github.philipsonfhir.smartsuite.examples.local.services.MyTopicService;
import org.github.philipsonfhir.smartsuite.fhircast.server.TopicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class MyTopicController extends TopicController {
    @Autowired
    public MyTopicController(
            MyTopicService topicService,
            MyFhirCastWebhookService websubService,
            @Value("${fhircast.fhirserver.url:#{null}}") String fhirServerUrl
    ) {
        super( topicService, websubService, fhirServerUrl );
    }
}
