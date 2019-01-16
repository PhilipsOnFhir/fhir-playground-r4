package com.github.philipsonfhir.fhircast.server.websub.service;

import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;

public interface EventChannelListener {
    void sendEvent(FhirCastWorkflowEventEvent event);
}
