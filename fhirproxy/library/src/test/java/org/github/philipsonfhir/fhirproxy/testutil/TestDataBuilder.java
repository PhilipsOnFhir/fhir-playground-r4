package org.github.philipsonfhir.fhirproxy.testutil;

import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class TestDataBuilder {

    // TODO
    static Random random = new Random(684 );

    public static Practitioner createPractitioner(String id, String birthDateString) {
        HumanName name = NameGenerator.createPractitionerHumanName();
        AdministrativeGender gender = ( random.nextBoolean()? AdministrativeGender.MALE: AdministrativeGender.FEMALE);

        Practitioner practitioner = (Practitioner) new Practitioner()
                .addName( name )
                .setGender( gender )
                .setBirthDate( createDate(birthDateString))
                .setId(id);

        return practitioner;
    }

    static Date createDate(String dateString) {
        LocalDate localeDate = LocalDate.parse(dateString);
        return Date.from(localeDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Patient createPatient(String id, String birthDateString) {
        AdministrativeGender gender = ( random.nextBoolean()? AdministrativeGender.MALE: AdministrativeGender.FEMALE);

        Patient patient = (Patient) new Patient()
                .addName( NameGenerator.createPatientHumanName())
                .setGender(gender)
                .setBirthDate(createDate(birthDateString))
                .setId(id);

        return patient;
    }

    public static Patient createPatient(String id, String birthDateString, Practitioner practitioner) {
        AdministrativeGender gender = ( random.nextBoolean()? AdministrativeGender.MALE: AdministrativeGender.FEMALE);

        Patient patient = createPatient( id, birthDateString );
        patient.addGeneralPractitioner( new Reference(practitioner));

        return patient;
    }

    public static Encounter createEncounter(String id, Patient patient ) {
        Encounter encounter = (Encounter) new Encounter()
                .setStatus( Encounter.EncounterStatus.INPROGRESS )
                .setSubject( new Reference().setReference( "Patient/"+patient.getId()) )
                .setId("Encounter-"+id+"-"+patient.getId());
        return encounter;
    }

    public static Procedure createProcedure(String id, Patient patient, Practitioner practitioner, String dateString) {
        Date start = createDate(dateString);
        Date end   = new Date(); end.setTime( start.getTime()+60000*15);
        Procedure procedure = (Procedure) new Procedure()
                .setStatus(Procedure.ProcedureStatus.COMPLETED)
                .setPerformed(new Period().setStart(start).setEnd(end))
                .setSubject(new Reference().setReference("Patient/"+patient.getId()))
                .addPerformer( new Procedure.ProcedurePerformerComponent()
                    .setActor(new Reference().setReference("Practitioner/"+patient.getId()))
                )
                .setId(id);
        return procedure;
    }


}
