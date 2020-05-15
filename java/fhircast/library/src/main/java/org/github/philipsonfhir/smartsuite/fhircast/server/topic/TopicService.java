package org.github.philipsonfhir.smartsuite.fhircast.server.topic;


import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
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
    public TopicService( ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public FhirCastTopic createTopic(String user) {
        FhirCastTopic fhirCastTopic = new FhirCastTopic( applicationEventPublisher, user);
        logger.info( "create topic "+fhirCastTopic.getTopic() );
        _topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
        return fhirCastTopic;
    }

    public FhirCastTopic updateTopic( String topic, String user ) {
        logger.info( "update topic "+topic );
        FhirCastTopic fhirCastTopic = new FhirCastTopic( applicationEventPublisher, topic, user );
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
        logger.info( "get topics" );
        List res = _topicMap.values().stream()
                .filter( fhirCastTopic -> fhirCastTopic.ofUser(name))
                .collect(Collectors.toList());
        return Collections.unmodifiableCollection( res );
    }

    public boolean hasTopic(String topic) {
        return _topicMap.containsKey( topic );
    }


//    public void publishEvent(FhirCastTopicEvent fhirCastWorkflowEvent) {
//        this.applicationEventPublisher.publishEvent( fhirCastWorkflowEvent );
//    }

//    public FhirCastTopic getTopicFromLaunch(String launch) {
//        Iterator<FhirCastTopic> it = _topicMap.values().iterator();
//        while ( it.hasNext() ) {
//            FhirCastTopic fct = it.next();
//            LaunchContext launchContext = fct.getFhirCastTopicStudyOrPatient(launch);
//            if( launchContext !=null ) {
//                return fct;
//            }
//        }
//        return null;
//    }

}
