package org.github.philipsonfhir.worklist.cdshooks.service.hooks;

import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;

public class PatientViewHook {
    public static TriggeredHook getTriggeredHook(LaunchContext launchContext) {
        TriggeredHook triggeredHook = new TriggeredHook();
        triggeredHook.setHook("patient-view");
        String user = launchContext.getEncounter().getParticipantFirstRep().getIndividual().getReference();
        if ( user!=null) {
            triggeredHook.getContext().put("userId", user );
        }
        triggeredHook.getContext().put( "encounterId", launchContext.getEncounter().getId() );
        triggeredHook.getContext().put( "patientId", launchContext.getPatient().getId() );
        return triggeredHook;
    }
}
