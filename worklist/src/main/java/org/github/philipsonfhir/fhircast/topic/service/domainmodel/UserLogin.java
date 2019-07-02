package org.github.philipsonfhir.fhircast.server.topic.domainmodel;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.List;

@Getter
@Setter
public class UserLogin {
    private String topic;
    Principal user;
    TopicAnchor primary;
    List<TopicAnchor> topicAnchorList;
    List<PatientLaunch> topicPatientLaunchList;
}
