package org.github.philipsonfhir.worklist.cdshooks.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatientData{
    String patientKey;
    String patientInstitution;
    String patientLastName;
    String patientFirstName;
    String patientGender;
    String patientDateOfBirth;
}
