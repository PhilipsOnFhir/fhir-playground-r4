//package org.github.philipsonfhir.fhircast.topic;
//
//import org.github.philipsonfhir.fhircast.common.FhirCastException;
//
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.logging.Logger;
//
//public class FhirCastTopicService {
//    private static final Logger logger = Logger.getLogger( FhirCastTopicService.class.getName() );
//    private Map<String, FhirCastTopic> topicMap = new TreeMap<>(  );
//
//    public FhirCastTopic createTopic() {
//        FhirCastTopic fhirCastTopic = new FhirCastTopic();
//        logger.info( "create topic "+fhirCastTopic.getTopic() );
//        this.topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
//        return fhirCastTopic;
//    }
//
//    public FhirCastTopic updateTopic(String topic) {
//        logger.info( "update topic "+topic );
//        FhirCastTopic fhirCastTopic = topicMap.get(topic);
//        if ( fhirCastTopic==null ){
//            fhirCastTopic = new FhirCastTopic( topic );
//            topicMap.put( fhirCastTopic.getTopic(), fhirCastTopic );
//        }
//        return fhirCastTopic;
//    }
//
//    public void removeTopic(String topic) throws FhirCastException {
//        logger.info( "remove topic "+topic );
//        if( topicMap.containsKey( topic )) {
//            FhirCastTopic fhirCastTopic = topicMap.get(topic);
//            fhirCastTopic.userLogout();
//            topicMap.remove( topic );
//        }
//        else {
//            throw new FhirCastException( "Unknown topic id :" + topic );
//        }
//    }
//
//    public FhirCastTopic getTopic(String topic) throws FhirCastException {
//        if( topicMap.containsKey( topic )) {
//            return topicMap.get( topic );
//        }
//        throw new FhirCastException( "Unknown topic id :"+topic );
//    }
//}
