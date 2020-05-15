package org.github.philipsonfhir.smartsuite.examples.local.controllers;

import org.github.philipsonfhir.smartsuite.examples.local.services.MyFhirCastWebhookService;
import org.github.philipsonfhir.smartsuite.examples.local.services.MyTopicService;
import org.github.philipsonfhir.smartsuite.fhircast.ExtTopicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class MyTopicController extends ExtTopicController {
    @Autowired
    public MyTopicController(MyTopicService topicService, MyFhirCastWebhookService websubService) {
        super(topicService, websubService, "");
    }
}
