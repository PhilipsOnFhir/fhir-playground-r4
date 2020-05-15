package org.github.philipsonfhir.smartsuite.examples.local.controllers;

import org.github.philipsonfhir.smartsuite.examples.local.services.MyTopicService;
import org.github.philipsonfhir.smartsuite.fhircast.rest.FhirCastRestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class MyFhirCastRestController extends FhirCastRestController {
    public MyFhirCastRestController(MyTopicService topicService) {
        super(topicService);
    }
}
