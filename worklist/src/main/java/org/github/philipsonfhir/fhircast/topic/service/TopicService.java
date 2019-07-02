package org.github.philipsonfhir.fhircast.topic.service;

import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Component
public class TopicService {
    private static final Logger logger = Logger.getLogger( TopicService.class.getName() );
    private Map<String, FhirCastTopic> _topicMap = new TreeMap<>(  );

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TopicService(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher =applicationEventPublisher;
    }

    public FhirCastTopic createTopic() {
        FhirCastTopic fhirCastTopic = new FhirCastTopic( this );
        logger.info( "create topic "+fhirCastTopic.getTopic() );
        _topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
        return fhirCastTopic;
    }

    public FhirCastTopic updateTopic(String topic) {
        logger.info( "update topic "+topic );
        FhirCastTopic fhirCastTopic = new FhirCastTopic( this, topic );
        _topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
        return fhirCastTopic;
    }

    public FhirCastTopic getTopic(String topic) throws FhirCastException {
        if( _topicMap.containsKey( topic )) {
            return _topicMap.get( topic );
        }
        throw new FhirCastException( "Unknown topic id :"+topic );
    }


    public void removeTopic(String topic) throws FhirCastException {
        logger.info( "remove topic "+topic );
        if( _topicMap.containsKey( topic )) {
            FhirCastTopic fhirCastTopic = _topicMap.get(topic);
//            fhirCastTopic.userLogout();
            this._topicMap.remove( topic );
        }
        else {
            throw new FhirCastException( "Unknown topic id :" + topic );
        }
    }

    public Collection<FhirCastTopic> getTopics() {
        return Collections.unmodifiableCollection( _topicMap.values() );
    }


    public void publishEvent(FhirCastTopicEvent fhirCastWorkflowEvent) {
        this.applicationEventPublisher.publishEvent( fhirCastWorkflowEvent );
    }

}
