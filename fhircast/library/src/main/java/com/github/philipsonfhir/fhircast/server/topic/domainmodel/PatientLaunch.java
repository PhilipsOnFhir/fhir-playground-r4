package com.github.philipsonfhir.fhircast.server.topic.domainmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatientLaunch {
    List<FhirChange> fhirChangeList;
//    List<Scope> scopeList;
}