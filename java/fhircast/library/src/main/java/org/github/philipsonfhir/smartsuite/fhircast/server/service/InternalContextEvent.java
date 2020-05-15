package org.github.philipsonfhir.smartsuite.fhircast.server.service;

import lombok.Getter;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.springframework.context.ApplicationEvent;

@Getter
public class InternalContextEvent extends ApplicationEvent {
    private final ContextEvent contextEvent;

    public InternalContextEvent( Object source, ContextEvent contextEvent) {
        super(source);
        this.contextEvent=contextEvent;
    }
}
