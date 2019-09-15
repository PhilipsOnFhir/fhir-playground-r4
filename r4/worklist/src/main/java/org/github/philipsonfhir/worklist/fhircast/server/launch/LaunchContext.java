package org.github.philipsonfhir.worklist.fhircast.server.launch;

import org.github.philipsonfhir.worklist.fhircast.server.topic.domainmodel.FhirChange;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchContext {
    private Patient patient;
    private ImagingStudy imagingStudy;
    private Encounter encounter;
    private String launch = "LNCH"+System.currentTimeMillis();

    public LaunchContext(Practitioner practitioner, Patient patient) {
        this.patient = patient;
        this.encounter = (Encounter) new Encounter()
                .setStatus( Encounter.EncounterStatus.INPROGRESS )
                .setSubject( new Reference(  patient ) )
                .addParticipant( new Encounter.EncounterParticipantComponent()
                        .addType( new CodeableConcept().addCoding( new Coding()
                                .setSystem("http://hl7.org/fhir/ValueSet/encounter-participant-type")
                                .setCode("PPRF")
                        ))
                        .setIndividualTarget( practitioner )
                )
        .setId( launch )
        ;
    }

    public LaunchContext(ImagingStudy study) {
        imagingStudy = study;
//        if ( study.hasSubject() ){
//            this.patient = study.getSubject().getReference();
//        }
    }

    public Resource getPatient() {
        return patient;
    }

    public Resource getImagingStudy() {
        return imagingStudy;
    }

    public boolean hasPatient() {
        return patient !=null;
    }

    public boolean hasImagingStudy() {
        return imagingStudy !=null;
    }

    @Override
    public boolean equals(Object object ){
        if ( object instanceof LaunchContext) {
            boolean result = true;
            LaunchContext launchContext = (LaunchContext) object;
            if ( hasPatient() != launchContext.hasPatient() || hasImagingStudy() != launchContext.hasImagingStudy() ) {
                return false;
            }
            if ( hasPatient()
                && !patient.getIdElement().getIdPart().equals( launchContext.getPatient().getIdElement().getIdPart() ) ) {
                return false;
            }
            if ( hasImagingStudy()
                && !imagingStudy.getIdElement().getIdPart().equals( launchContext.getImagingStudy().getIdElement().getIdPart() ) ) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean contains(Patient patient) {
        if ( hasPatient() && this.patient.getId().equals(patient.getId() )){
            return true;
        }
        return false;
    }

    public boolean contains(ImagingStudy study ) {
        if ( hasImagingStudy() && imagingStudy.getId().equals(study.getId() )){
            return true;
        }
        return false;
    }

    public String getLaunch() {
        return this.launch;
    }

    public Encounter getEncounter() {
        return encounter;
    }



    List<FhirChange> fhirChangeList = new ArrayList<>();

    public List<FhirChange> getChanges() {
        return Collections.unmodifiableList(fhirChangeList);
    }
}
