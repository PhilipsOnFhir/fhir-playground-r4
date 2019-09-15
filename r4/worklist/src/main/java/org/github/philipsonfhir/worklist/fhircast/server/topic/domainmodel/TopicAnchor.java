package org.github.philipsonfhir.worklist.fhircast.server.topic.domainmodel;

import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

@Getter
@Setter
public class TopicAnchor {
    List<PatientLaunch> patientPatientLaunchList;
    ImagingStudy imagingStudy;
    Patient patient;
}

