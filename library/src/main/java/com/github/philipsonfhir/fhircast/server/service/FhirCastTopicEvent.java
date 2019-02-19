package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import com.github.philipsonfhir.fhircast.support.websub.FhircastEventType;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
import java.util.TreeMap;

public class FhirCastTopicEvent extends ApplicationEvent {

    private final String _topic;
    FhircastEventType   _eventType;
    Map<String, Object> _context = new TreeMap<>();

    public FhirCastTopicEvent(Object source, String topic, FhircastEventType fhircastEventType, Map<String, Object> contexy) {
        super( source );
        _eventType = fhircastEventType;
        _context = contexy;
        _topic = topic;
    }

    public FhircastEventType getEventType(){ return _eventType; }
    public Map<String,Object> getContext(){ return _context; };

    public String getTopic() {
        return _topic;
    }
}
