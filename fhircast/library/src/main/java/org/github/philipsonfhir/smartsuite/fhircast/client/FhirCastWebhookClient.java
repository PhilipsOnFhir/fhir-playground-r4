package org.github.philipsonfhir.smartsuite.fhircast.client;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebHookSubscribtionRequest;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebHookSubscribtionResponse;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class FhirCastWebhookClient {

    private final static  Random random = new Random( System.currentTimeMillis() );
    private final String topicId;
    private final int port;
    private final String hubUrl;
    private final CommunicationListener communicationListener;
    private final String clientUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private FhirCastWebHookSubscribtionRequest currentSubscriptionRequest = null;
    private FhirCastWebHookSubscribtionResponse currentSubscriptionResponse;
    private boolean verified = false;
    private boolean eventReceived = false;
    private Object eventSync = new Object();
    private boolean denialReceived = false;
    private Object denialSync = new Object();
    private ContextEvent currentEvent;

    public FhirCastWebhookClient(String hubUrl, String topicId ) {
        this( hubUrl, "http://localhost", topicId );
    }

    public FhirCastWebhookClient(String hubUrl, String clientUrl, String topicId ) {
        this.hubUrl = hubUrl;
        this.clientUrl = (clientUrl.endsWith(":")?clientUrl:clientUrl+":");
        this.topicId = topicId ;

        port = random.nextInt( 1000 )+10000;
        System.out.println("Port : "+port);
        communicationListener = new CommunicationListener( port, this );
        new Thread( communicationListener ).start();
    }

    public String getTopicId() {
        return topicId;
    }


    public void subscribeWebsubAll() throws FhirCastException {
        subscribeWebsubAll( 3600 );
    }

    /** subscribe to all events */
    public void subscribeWebsubAll(long delay) throws FhirCastException {
//        FhirCastWebHookSubscribtionRequest subscribtionRequest = new FhirCastWebHookSubscribtionRequest();
//        subscribtionRequest.setHub_mode("subscribe");
//        subscribtionRequest.setHub_topic(this.topicId);
//        subscribtionRequest.setHub_events("*-*,*");
//        subscribtionRequest.setHub_callback("http://localhost:"+port );
//        subscribtionRequest.setHub_secret( "secret"+random.nextLong() );
//        subscribtionRequest.setHub_lease_seconds( ""+delay );
//        this.communicationListener.setSecret( subscribtionRequest.getHub_secret() );
        subscribe( "*-*,*", delay );

//        updateSubscription( subscribtionRequest);
    }

    public void subscribe(String events, long delay) throws FhirCastException {
        FhirCastWebHookSubscribtionRequest subscribtionRequest = new FhirCastWebHookSubscribtionRequest();
        subscribtionRequest.setHub_mode("subscribe");
        subscribtionRequest.setHub_topic(this.topicId);
        subscribtionRequest.setHub_events(events);
        subscribtionRequest.setHub_callback(clientUrl+port );
        subscribtionRequest.setHub_secret( "secret"+random.nextLong() );
        subscribtionRequest.setHub_lease_seconds( ""+delay );
        this.communicationListener.setSecret( subscribtionRequest.getHub_secret() );

        updateSubscription( subscribtionRequest);
    }

    void unsubscribeWebsubAll() throws FhirCastException {
//        FhirCastWebHookSubscribtionRequest fhirCastWebHookSubscribtionRequest = new FhirCastWebHookSubscribtionRequest();
//        fhirCastWebHookSubscribtionRequest.setHub_mode("unsubscribe");
//        fhirCastWebHookSubscribtionRequest.setHub_topic(this.topicId);
//        fhirCastWebHookSubscribtionRequest.setHub_events("*-*");
//        fhirCastWebHookSubscribtionRequest.setHub_callback("http://localhost:"+port );
//        fhirCastWebHookSubscribtionRequest.setHub_secret( "secret"+random.nextLong() );
//
//        updateSubscription( fhirCastWebHookSubscribtionRequest);
        unsubscribe("*-*");
    }

    public void unsubscribe(String events ) throws FhirCastException {
        FhirCastWebHookSubscribtionRequest fhirCastWebHookSubscribtionRequest = new FhirCastWebHookSubscribtionRequest();
        fhirCastWebHookSubscribtionRequest.setHub_mode("unsubscribe");
        fhirCastWebHookSubscribtionRequest.setHub_topic(this.topicId);
        fhirCastWebHookSubscribtionRequest.setHub_events(events);
        fhirCastWebHookSubscribtionRequest.setHub_callback(clientUrl+port );
        fhirCastWebHookSubscribtionRequest.setHub_secret( "secret"+random.nextLong() );

        updateSubscription( fhirCastWebHookSubscribtionRequest);
    }

    private void updateSubscription(FhirCastWebHookSubscribtionRequest subscribtionRequest) throws FhirCastException {
        this.currentSubscriptionRequest = subscribtionRequest;
        // TODO check for: SHALL have a Content-Type header of application/x-www-form-urlencoded

        // register form message converter
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new FormHttpMessageConverter() );

        // create form parameters as a MultiValueMap
        final MultiValueMap<String, String> formVars = subscribtionRequest.getFormVars();

        // send the request -- our service returns a String in the body
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity( hubUrl, formVars, String.class );

//        ResponseEntity<String> responseEntity = restTemplate.postForEntity( hubUrl, fhirCastWebHookSubscribtionRequest, String.class );
        if ( responseEntity.getStatusCode()!= HttpStatus.ACCEPTED ) {
            throw new FhirCastException("Subscription update failed");
        }
    }

    public void newEvent(ContextEvent fhirCastWorkflowEvent) {
        synchronized ( eventSync ){
            System.out.println(fhirCastWorkflowEvent);
            this.currentEvent = fhirCastWorkflowEvent;
            this.eventReceived = true;
            eventSync.notifyAll();
        }
    }

    void verifySubscription(FhirCastWebHookSubscribtionResponse response) throws FhirCastException {
        if ( this.currentSubscriptionRequest==null ){
            throw new FhirCastException("No request send");
        }

        this.currentSubscriptionResponse = response;
        if ( !currentSubscriptionRequest.getHub_topic().equals(response.getHub_topic())){
            throw new FhirCastException("Illegal topic");
        }

        synchronized (this){
            this.verified=true;
            this.notifyAll();
        }
    }

    public void waitForVerfication() throws FhirCastException {
        synchronized ( this ) {
            if (!this.verified) {
                try {
                    this.wait(10000);
                } catch (InterruptedException e) {
                    throw new FhirCastException("Verification failed");
                }
            }
            if (!this.verified) {
                throw new FhirCastException("Verification failed");
            }
        }
    }

    public void waitForEvent() throws FhirCastException {
        synchronized ( eventSync ) {
            if (!this.eventReceived) {
                try {
                    eventSync.wait(10000);
                } catch (InterruptedException e) {
                    throw new FhirCastException("No event received time out");
                }
            }
            if (!this.eventReceived) {
                throw new FhirCastException("No event received time out");
            }
        }
    }

    public void waitForDenial() throws FhirCastException {
        synchronized ( denialSync ) {
            if (!this.denialReceived) {
                try {
                    denialSync.wait(20000);
                } catch (InterruptedException e) {
                    throw new FhirCastException("No denial event received time out");
                }
            }
            if (!this.denialReceived) {
                throw new FhirCastException("No denial event received time out");
            }
        }
    }

    public void resetEventWait(){
        this.eventReceived = false;
    }

    public ContextEvent getLastEvent() {
        return currentEvent;
    }

    public void setReceptionErrorMode() {
        this.communicationListener.refuseEvents();
    }

    public void denialEventReceived(String events) {
        synchronized (denialSync ){
            this.denialReceived = true;
            System.out.println("events denied = " + events);
            denialSync.notifyAll();
        }
    }
    public String status(){
        String status = ( this.verified? "verified": "not verified" );
        return status;
    }

    public String subscribedEvents(){
        return ( this.currentSubscriptionResponse==null?
                "-":
                this.currentSubscriptionResponse.getHub_events()
        );
    }

}
