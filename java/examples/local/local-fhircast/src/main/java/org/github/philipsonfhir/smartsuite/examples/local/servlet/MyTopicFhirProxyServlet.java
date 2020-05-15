package org.github.philipsonfhir.smartsuite.examples.local.servlet;

import org.github.philipsonfhir.smartsuite.examples.local.services.MyFhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.ExtPrefix;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.github.philipsonfhir.smartsuite.persistance.TopicFhirProxyServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.annotation.WebServlet;
import java.util.Optional;

@WebServlet(urlPatterns = ExtPrefix.FHIR_TOPIC +"/*")
@CrossOrigin(origins = "*")
public class MyTopicFhirProxyServlet extends TopicFhirProxyServlet {
    @Autowired
    public MyTopicFhirProxyServlet(MyFhirCastService fhircastService,
                                   @Value("${fhircast.fhirserver.url:#{null}}") Optional<String> optFhirServerUrl,
                                   @Value("${fhircast.fhirserver.memory:false}") boolean inMemory
    ) throws FhirCastException {
        super(fhircastService, optFhirServerUrl, inMemory);
    }
}
