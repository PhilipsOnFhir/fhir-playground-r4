package org.github.philipsonfhir.fhircast.server.topic.service;

import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;

public class FhirCastTopicStudyOrPatient {
    private Patient _patient;
    private ImagingStudy _imagingStudy;

    public FhirCastTopicStudyOrPatient( Patient patient) {
        _patient = patient;
    }

    public FhirCastTopicStudyOrPatient(FhirCastTopic fhirCastTopic, ImagingStudy study) {
        _imagingStudy = study;
    }

    public Resource getPatient() {
        return _patient;
    }

    public Resource getImagingStudy() {
        return _imagingStudy;
    }

    public boolean hasPatient() {
        return _patient!=null;
    }

    public boolean hasImagingStudy() {
        return _imagingStudy!=null;
    }

    @Override
    public boolean equals(Object object ){
        if ( object instanceof FhirCastTopicStudyOrPatient ) {
            boolean result = true;
            FhirCastTopicStudyOrPatient fhirCastTopicStudyOrPatient = (FhirCastTopicStudyOrPatient) object;
            if ( hasPatient() != fhirCastTopicStudyOrPatient.hasPatient() || hasImagingStudy() != fhirCastTopicStudyOrPatient.hasImagingStudy() ) {
                return false;
            }
            if ( hasPatient()
                && !_patient.getId().equals( fhirCastTopicStudyOrPatient.getPatient().getId() ) ) {
                return false;
            }
            if ( hasImagingStudy()
                && !_imagingStudy.getId().equals( fhirCastTopicStudyOrPatient.getImagingStudy().getId() ) ) {
                return false;
            }
            return true;
        }
        return false;
    }

//    public void closePatientChart() {
//        _fhirCastTopic.eventReceived(FhircastEventType.CLOSE_PATIENT_CHART, null );
//    }
}
