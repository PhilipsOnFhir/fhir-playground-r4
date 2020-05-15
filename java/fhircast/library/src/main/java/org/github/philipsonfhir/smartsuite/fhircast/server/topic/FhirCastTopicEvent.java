package org.github.philipsonfhir.smartsuite.fhircast.server.topic;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class FhirCastTopicEvent extends ApplicationEvent {

    private final EventType eventType;
    private final FhirCastAnchor anchor;
    private final String topic;

    public enum EventType{ OPEN, CLOSE, LOGOUT, SYNCERROR  }

    public FhirCastTopicEvent(Object source, EventType eventType ) {
        this( source, eventType, null );
    }

    public FhirCastTopicEvent(Object source, EventType eventType, FhirCastAnchor anchor) {
        super(source);
        this.topic = ((FhirCastTopic)source).getTopic();
        this.eventType = eventType;
        this.anchor = anchor;
    }

//    private final String _topic;
//    FhircastEventType   _eventType;
//    Map<String, Object> _context = new TreeMap<>();

//    public FhirCastTopicEvent(Object source, String topic, FhircastEventType fhircastEventType, Map<String, Object> contexy) {
//        super( source );
//        _eventType = fhircastEventType;
//        _context = contexy;
//        _topic = topic;
//    }
//
//    public FhircastEventType getEventType(){ return _eventType; }
//    public Map<String,Object> getContext(){ return _context; };
//
//    public String getTopic() {
//        return _topic;
//    }
}
