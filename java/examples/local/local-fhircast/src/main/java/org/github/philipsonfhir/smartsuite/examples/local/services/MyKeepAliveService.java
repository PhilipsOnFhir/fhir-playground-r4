package org.github.philipsonfhir.smartsuite.examples.local.services;

import org.github.philipsonfhir.smartsuite.fhircast.KeepAliveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class MyKeepAliveService extends KeepAliveService {
    @Autowired
    public MyKeepAliveService(
            MyTopicService topicService,
            MyFhirCastService fhirCastService,
            @Value("${fhircast.alive.active:false}") boolean active,
            @Value("${fhircast.alive.period:1000}") long period
    ) {
        super(topicService, fhirCastService, active, period);
    }
}
