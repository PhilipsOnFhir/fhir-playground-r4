package org.github.philipsonfhir.fhircast.topic.service;

public class FhirCastTopic {
    private final String topicId;


    public FhirCastTopic() {
        this( "FC" + System.currentTimeMillis());
    }

    public FhirCastTopic( String topicId ){
        this.topicId = topicId;
    }

    public String getTopic() {
        return this.topicId;
    }
}
