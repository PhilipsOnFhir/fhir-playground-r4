package org.github.philipsonfhir.cdshooks.test.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionUpdate {
    List<Suggestion> suggestion;
    String patientId;
    String cdrId;
    String encounterId;
    String studyId;
}
