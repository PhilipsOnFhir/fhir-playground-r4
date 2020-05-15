package org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TopicList {
    String hubUrl;
    List<String> topic = new ArrayList<>();
    String topicUrl;
    String authUrl;
    String tokenUrl;
}
