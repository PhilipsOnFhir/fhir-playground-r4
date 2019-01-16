package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;

public interface EventChannelListener {
    public void sendEvent(FhirCastWorkflowEvent event );
}
