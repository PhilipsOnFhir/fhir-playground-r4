package org.github.philipsonfhir.smartsuite.examples.local.controllers;

import org.github.philipsonfhir.smartsuite.examples.local.services.MyFhirCastService;
import org.github.philipsonfhir.smartsuite.examples.local.services.MyTopicService;
import org.github.philipsonfhir.smartsuite.fhircast.server.FhirCastController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class MyFhircastController extends FhirCastController {
    @Autowired
    public MyFhircastController(MyFhirCastService fhirCastService, MyTopicService topicService) {
        super(fhirCastService, topicService);
    }
}
