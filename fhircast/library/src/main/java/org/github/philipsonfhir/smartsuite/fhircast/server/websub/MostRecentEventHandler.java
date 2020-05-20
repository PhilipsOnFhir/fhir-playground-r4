package org.github.philipsonfhir.smartsuite.fhircast.server.websub;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostRecentEventHandler {
    private final Map<String,ContextEvent> mostRecentEvents = new HashMap<>();

    public void newEvent(ContextEvent contextEvent) {
        mostRecentEvents.put( contextEvent.getEvent().getHub_event(), contextEvent );
    }

    public List<ContextEvent> getSortedEvents() {
        List<ContextEvent> list = mostRecentEvents.values().stream()
                .sorted((ce1,ce2)-> ce2.obtainDateTimeStamp().compareTo(ce2.obtainDateTimeStamp()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }
}
