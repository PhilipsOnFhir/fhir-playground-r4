package org.github.philipsonfhir.cdshooks.test.model;

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
