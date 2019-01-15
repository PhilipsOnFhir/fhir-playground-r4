package com.philips.research.philipsonfhir.fhirproxy.dstu3.applications.fhircast.app.service;

import ca.uhn.fhir.context.FhirContext;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.service.FhirCastException;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Controller
public class FhirCastClient {

    private FhirContext ourCtx = FhirContext.forDstu3();
    private String baseUrl;
    private String topicUrl = null;
    RestTemplate restTemplate = new RestTemplate(  );
    String sessionId = null;
    private Patient patient = null ;
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private Map<String, IBaseResource> context = new TreeMap<String, IBaseResource>(  );

    //server.port=9601","fhircast.topic=123
    @Value("${server.port}")
    public void setPort( String port ) throws FhirCastException {
        this.baseUrl = "http://localhost:"+port;
        System.out.println(baseUrl);
        initialize();
//        this.topicUrl = url+"/"+sessionId;
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.baseUrl, "{}", String.class  );
//        if ( responseEntity.getStatusCode()!= HttpStatus.CREATED ){
//            throw new FhirCastException("Error creating session");
//        }
//        this.sessionId = responseEntity.getBody();
    }

    private void initialize() throws FhirCastException {
        if ( baseUrl!=null && sessionId!=null ){
        this.topicUrl = baseUrl+"/"+sessionId;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.baseUrl, "{}", String.class  );
        if ( responseEntity.getStatusCode()!= HttpStatus.CREATED ){
            throw new FhirCastException("Error creating session");
        }
        this.sessionId = responseEntity.getBody();

        }
    }

    //server.port=9601","fhircast.topic=123
    @Value("${fhircast.topic}")
    public void setTopic( String topic ) throws FhirCastException {
        this.sessionId = topic;
        System.out.println(sessionId);
        initialize();
//        baseUrl = url;
//        this.topicUrl = url+"/"+sessionId;
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.baseUrl, "{}", String.class  );
//        if ( responseEntity.getStatusCode()!= HttpStatus.CREATED ){
//            throw new FhirCastException("Error creating session");
//        }
//        this.sessionId = responseEntity.getBody();
    }

    public FhirCastClient() throws FhirCastException {
    }

    public FhirCastClient(String url) throws FhirCastException {
        this.baseUrl = url;
        this.topicUrl = url+"/"+sessionId;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.baseUrl, "{}", String.class  );
        if ( responseEntity.getStatusCode()!= HttpStatus.CREATED ){
            throw new FhirCastException("Error creating session");
        }
        this.sessionId = responseEntity.getBody();
    }

    public FhirCastClient(String url, String sessionId) {
        this.baseUrl = url;
        this.topicUrl = url+"/"+sessionId;
        restTemplate.put( this.topicUrl, String.class  );
        this.sessionId = sessionId;
    }

    public List<String> getSessions(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity( this.baseUrl, String.class  );
        System.out.println(responseEntity);
        return new ArrayList<>(  );
    }

    public void close() {
        restTemplate.delete( this.topicUrl );
        this.sessionId = null;
    }

    public void subscribePatientChange() {
        int port = 6840;
        CommunicationListener communicationListener = new CommunicationListener( port, this );
        new Thread( communicationListener ).start();
//        Host: hub.example.com
//        Authorization: Bearer i8hweunweunweofiwweoijewiweh
//        hub.callback=https%3A%2F%2Fapp.example.com%2Fsession%2Fcallback%2Fv7tfwuk17a&
//        hub.mode=updateSubscriptions
//        &hub.topic=https%3A%2F%2F
//        hub.example.com%2F7jaa86kgdudewiaq0wtu&
//        hub.secret=shhh-this-is-a-secret&
//        hub.events=patient-open-chart,patient-close-chart
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_callback( "http://localhost:"+port );
        fhirCastSessionSubscribe.setHub_mode( "subscribe" );
        fhirCastSessionSubscribe.setHub_topic( sessionId );
        fhirCastSessionSubscribe.setHub_secret("mysecret");
        fhirCastSessionSubscribe.setHub_events( FhircastEventType.OPEN_PATIENT_CHART+","+ FhircastEventType.SWITCH_PATIENT_CHART+","+ FhircastEventType.CLOSE_PATIENT_CHART ); //"patient-open-chart,patient-close-chart" );
        restTemplate.postForEntity( topicUrl, fhirCastSessionSubscribe, String.class );
    }

    public Patient getCurrentPatient() {
        return this.patient;
    }

    public void setCurrentPatient(Patient patient) {

        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent= new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_topic( this.sessionId );
        if ( this.patient ==null ){
            fhirCastWorkflowEventEvent.setHub_event( FhircastEventType.OPEN_PATIENT_CHART );
        } else if ( !this.patient.getId().equals( patient.getId()  )) {
            fhirCastWorkflowEventEvent.setHub_event( FhircastEventType.SWITCH_PATIENT_CHART );
        }
        else{
            return;
        }
        this.patient = patient;

        List<FhirCastContext> fhirCastContextList = new ArrayList<>(  );
        FhirCastContext fhirCastContext = new FhirCastContext();
        fhirCastContext.setKey( "patient" );
        fhirCastContext.setResource( ourCtx.newJsonParser().encodeResourceToString( patient ) );
        fhirCastContextList.add( fhirCastContext );
        fhirCastWorkflowEventEvent.setContext( fhirCastContextList );

        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        Date today = new Date();
        fhirCastWorkflowEvent.setTimestamp( ""+today );
        fhirCastWorkflowEvent.setId( UUID.randomUUID().toString() );
        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );

        RestTemplate restTemplate = new RestTemplate(  );
        logger.info("send event");
        restTemplate.postForLocation( this.topicUrl, fhirCastWorkflowEvent );
    }


    public void newEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        logger.info(  "New event "+fhirCastWorkflowEvent.getEvent().getContext() );
        switch( fhirCastWorkflowEvent.getEvent().getHub_event() ){
            case OPEN_PATIENT_CHART:
            case SWITCH_PATIENT_CHART:
                FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = fhirCastWorkflowEvent.getEvent();
                for( FhirCastContext fhirCastContext: fhirCastWorkflowEventEvent.getContext()){
                    String resourceStr = fhirCastContext.getResource();
                    IBaseResource resource = ourCtx.newJsonParser().parseResource( resourceStr );
                    this.context.put( fhirCastContext.getKey(), resource);
                }
                break;
        }
        if ( this.context.containsKey( "patient" )){
            this.patient = (Patient) this.context.get( "patient" );
        }

    }

    public void getContext() {
        RestTemplate restTemplate =  new RestTemplate(  );
        FhirCastWorkflowEvent fhirCastWorkflowEvent = restTemplate.getForObject( topicUrl, FhirCastWorkflowEvent.class );
        System.out.println(fhirCastWorkflowEvent);
    }
}

