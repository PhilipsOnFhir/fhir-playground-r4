package org.github.philipsonfhir.smartsuite.fhircast.server.topic;

import org.hl7.fhir.r4.model.Resource;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.Iterator;

public class FhirCastTopic {
    private final ApplicationEventPublisher applicationEventPublisher;
    private String topic;
    private String user;
    private FhirCastAnchor currentAnchor;
    private HashMap<String, FhirCastAnchor> fhirCastAnchorMap = new HashMap<>();

    public FhirCastTopic(ApplicationEventPublisher applicationEventPublisher, String user ) {
        this(  applicationEventPublisher, "FC-topic-"+System.currentTimeMillis(), user );
    }

    public FhirCastTopic( ApplicationEventPublisher applicationEventPublisher, String topic, String user) {
        this.user = user;
        this.topic = topic;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public String getTopic() {
        return topic;
    }

    public boolean ofUser(String name) {
        return (this.user!=null ? this.user.equals(name): name==null );
    }

    public void closeTopic() {
        fhirCastAnchorMap.entrySet().forEach( anchor -> {
            fhirCastAnchorMap.remove( anchor );
            this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent( this, FhirCastTopicEvent.EventType.CLOSE, anchor.getValue() ));
        });
        this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent( this, FhirCastTopicEvent.EventType.LOGOUT ));
    }

    public void userLogout() { closeTopic(); }

    public void userHibernate() { }

    public void openAnchor(Resource resource) {
        FhirCastAnchor fhirCastAnchor = fhirCastAnchorMap.get( FhirCastAnchor.getAnchorKey(resource) );
        String key = FhirCastAnchor.getAnchorKey( resource);
        if ( fhirCastAnchor == null ){
            fhirCastAnchor = new FhirCastAnchor( resource );
            fhirCastAnchorMap.put(fhirCastAnchor.getKey(), fhirCastAnchor );
        }
        this.currentAnchor = fhirCastAnchorMap.get(key);
        this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent(this, FhirCastTopicEvent.EventType.OPEN, fhirCastAnchor));
    }


    public void closeAnchor( Resource resource ) {
        FhirCastAnchor fhirCastAnchor = fhirCastAnchorMap.get( FhirCastAnchor.getAnchorKey(resource) );
        if ( fhirCastAnchor!=null ){
            fhirCastAnchorMap.remove( fhirCastAnchor.getKey() );
            this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent(this, FhirCastTopicEvent.EventType.CLOSE, fhirCastAnchor ));

            Iterator<FhirCastAnchor> it = fhirCastAnchorMap.values().iterator();
            if ( it.hasNext() ) {
                currentAnchor = it.next();
                this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent(this, FhirCastTopicEvent.EventType.OPEN, fhirCastAnchor));
            }
        }

    }

    public void syncError(ApplicationEvent event) {
        this.applicationEventPublisher.publishEvent( new FhirCastTopicEvent(this, FhirCastTopicEvent.EventType.SYNCERROR ));
    }

}
