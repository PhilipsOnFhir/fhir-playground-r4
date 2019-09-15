package org.github.philipsonfhir.worklist.fhircast.server.launch;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.FhirCastTopic;
import org.github.philipsonfhir.worklist.fhircast.server.topic.service.TopicService;
import org.github.philipsonfhir.worklist.fhircast.server.websub.domain.JsonRawValueDeserializer;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
//@CrossOrigin(origins = "*")
public class LaunchController {

    private final TopicService topicService;
    FhirContext ourCtx = FhirContext.forR4();

    @Autowired
    public LaunchController(TopicService topicService, @Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        this.topicService = topicService;
    }

    @GetMapping( Prefix.FHIRCAST_LAUNCH+"/{launchId}" )
    public LaunchInfo getLaunchInformation(
            @PathVariable String launchId
    ) throws FhirCastException {
        FhirCastTopic fhirCastTopic = this.topicService.getTopicFromLaunch(launchId);
        LaunchContext launchContext = fhirCastTopic.getFhirCastTopicStudyOrPatient(launchId);
        launchContext.getEncounter();

        return new LaunchInfo(launchContext);
    }

    private class LaunchInfo {
        @JsonRawValue
        @JsonDeserialize(using = JsonRawValueDeserializer.class)
        private final String encounter;

        @JsonRawValue
        @JsonDeserialize(using = JsonRawValueDeserializer.class)
        private final String  imagingStudy;

        @JsonRawValue
        @JsonDeserialize(using = JsonRawValueDeserializer.class)
        String patient;

        public LaunchInfo(LaunchContext launchContext) {
            this.encounter    = getString( launchContext.getEncounter() );
            this.patient      = getString( launchContext.getPatient() );
            this.imagingStudy = getString( launchContext.getImagingStudy());
        }

        String getString( Resource resource ){
            if ( resource!=null ){
                return ourCtx.newJsonParser().encodeResourceToString( resource );
            }
            return null;
        }
    }
}
