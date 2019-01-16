package com.github.philipsonfhir.fhircast.server;

import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;

public interface EventChannelListener {
    void sendEvent(FhirCastWorkflowEventEvent event);
}
