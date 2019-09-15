package org.github.philipsonfhir.worklist.fhircast.server.topic.service;

import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.github.philipsonfhir.worklist.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class TopicService {
    private static final Logger logger = Logger.getLogger( TopicService.class.getName() );
    private Map<String, FhirCastTopic> _topicMap = new TreeMap<>(  );

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TopicService(ApplicationEventPublisher applicationEventPublisher ){
        this.applicationEventPublisher =applicationEventPublisher;
    }

    public FhirCastTopic createTopic(Practitioner practitioner) {
        FhirCastTopic fhirCastTopic = new FhirCastTopic( this, practitioner);
        logger.info( "create topic "+fhirCastTopic.getTopic() );
        _topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
        return fhirCastTopic;
    }

    public FhirCastTopic updateTopic(String topic, Practitioner practitioner) {
        logger.info( "update topic "+topic );
        FhirCastTopic fhirCastTopic = new FhirCastTopic( this, topic, practitioner );
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
            fhirCastTopic.userLogout();
            this._topicMap.remove( topic );
        }
        else {
            throw new FhirCastException( "Unknown topic id :" + topic );
        }
    }

    public Collection<FhirCastTopic> getTopics(String name) {
        List res = _topicMap.values().stream()
                .filter( fhirCastTopic -> fhirCastTopic.ofUser(name))
                .collect(Collectors.toList());
        return Collections.unmodifiableCollection( res );
    }


    public void publishEvent(FhirCastTopicEvent fhirCastWorkflowEvent) {
        this.applicationEventPublisher.publishEvent( fhirCastWorkflowEvent );
    }

    public FhirCastTopic getTopicFromLaunch(String launch) {
        Iterator<FhirCastTopic> it = _topicMap.values().iterator();
        while ( it.hasNext() ) {
            FhirCastTopic fct = it.next();
            LaunchContext launchContext = fct.getFhirCastTopicStudyOrPatient(launch);
            if( launchContext !=null ) {
                return fct;
            }
        }
        return null;
    }

}
