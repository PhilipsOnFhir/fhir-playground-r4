package org.github.philipsonfhir.smartsuite.fhircast.server.topic.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Topic {
    String topic = "-";
    String hubUrl = "-";
    String fhirUrl = "-";
}
