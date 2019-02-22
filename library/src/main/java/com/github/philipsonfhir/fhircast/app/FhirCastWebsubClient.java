package com.github.philipsonfhir.fhircast.app;

import ca.uhn.fhir.context.FhirContext;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.websub.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

public class FhirCastWebsubClient {

    private final int port;

    private FhirContext ourCtx = FhirContext.forDstu3();
    private String baseUrl;
    private String topicUrl = null;
    RestTemplate restTemplate = new RestTemplate(  );
    String topicId = null;
    private Patient patient = null ;
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private Map<String, IBaseResource> context = new TreeMap<String, IBaseResource>(  );

    //server.port=9601","fhircast.topic=123
    @Value("${server.port}")
    public void setPort( String port ) throws FhirCastException {
        this.baseUrl = "http://localhost:"+port;
        System.out.println(baseUrl);
        initialize();
    }

    private void initialize() throws FhirCastException {
        if ( baseUrl!=null && topicId !=null ){
        this.topicUrl = baseUrl+"/"+ topicId;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( this.baseUrl, "{}", String.class  );
        if ( responseEntity.getStatusCode()!= HttpStatus.CREATED ){
            throw new FhirCastException("Error creating session");
        }
        this.topicId = responseEntity.getBody();

        }
    }

    //server.port=9601","fhircast.topic=123
    @Value("${fhircast.topic}")
    public void setTopic( String topic ) throws FhirCastException {
        this.topicId = topic;
        System.out.println( topicId );
        initialize();
    }


    public FhirCastWebsubClient(String url, String topicId) {
        this.baseUrl = url;
        this.topicUrl = url+"/"+ topicId;
        restTemplate.put( this.topicUrl, String.class  );
        this.topicId = topicId;

        port = new Random( System.currentTimeMillis() ).nextInt( 100 )+9300;
        CommunicationListener communicationListener = new CommunicationListener( port, this );
        new Thread( communicationListener ).start();

    }

    public List<String> getSessions(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity( this.baseUrl, String.class  );
        System.out.println(responseEntity);
        return new ArrayList<>(  );
    }

    public void logout() {
        try {
            restTemplate.delete( this.topicUrl );
        } catch ( HttpServerErrorException error ){
            System.out.println("Http error "+ error.getStatusText());
        }
        this.topicId = null;
    }

    public void close() {
        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent= new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_topic( this.topicId );
        fhirCastWorkflowEventEvent.setHub_event( FhircastEventType.CLOSE_PATIENT_CHART );

        List<FhirCastContext> fhirCastContextList = new ArrayList<>(  );
        FhirCastContext fhirCastContext = new FhirCastContext();
        fhirCastContext.setKey( "patient" );
        fhirCastContext.setResource( patient );
        fhirCastContextList.add( fhirCastContext );
        fhirCastWorkflowEventEvent.setContext( fhirCastContextList );

        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        Date today = new Date();
        fhirCastWorkflowEvent.setTimestamp( ""+today );
        fhirCastWorkflowEvent.setId( UUID.randomUUID().toString() );
        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );

        RestTemplate restTemplate = new RestTemplate(  );
        logger.info("send event");
        restTemplate.postForLocation( this.topicUrl+"/websub", fhirCastWorkflowEvent );
    }

    public void unSubscribePatientChange() {
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_callback( "http://localhost:"+port );
        fhirCastSessionSubscribe.setHub_mode( "unsubscribe" );
        fhirCastSessionSubscribe.setHub_topic( topicId );
        fhirCastSessionSubscribe.setHub_secret("mysecret");
        fhirCastSessionSubscribe.setHub_events( FhircastEventType.OPEN_PATIENT_CHART+","+ FhircastEventType.SWITCH_PATIENT_CHART+","+ FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.USER_LOGOUT ); //"patient-open-chart,patient-logout-chart" );
        restTemplate.postForEntity( topicUrl+"/websub", fhirCastSessionSubscribe, String.class );
    }

    public void subscribePatientChange() {
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_callback( "http://localhost:"+port );
        fhirCastSessionSubscribe.setHub_mode( "subscribe" );
        fhirCastSessionSubscribe.setHub_topic( topicId );
        fhirCastSessionSubscribe.setHub_secret("mysecret");
        fhirCastSessionSubscribe.setHub_events( FhircastEventType.OPEN_PATIENT_CHART+","+ FhircastEventType.SWITCH_PATIENT_CHART+","+ FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.USER_LOGOUT ); //"patient-open-chart,patient-logout-chart" );
        restTemplate.postForEntity( topicUrl+"/websub", fhirCastSessionSubscribe, String.class );
    }

    public Patient getCurrentPatient() {
        return this.patient;
    }

    public void setCurrentPatient(Patient patient) {


        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent= new FhirCastWorkflowEventEvent();
        fhirCastWorkflowEventEvent.setHub_topic( this.topicId );
        fhirCastWorkflowEventEvent.setHub_event( FhircastEventType.OPEN_PATIENT_CHART );
        this.patient = patient;

        List<FhirCastContext> fhirCastContextList = new ArrayList<>(  );
        FhirCastContext fhirCastContext = new FhirCastContext();
        fhirCastContext.setKey( "patient" );
        fhirCastContext.setResource( patient );
        fhirCastContextList.add( fhirCastContext );
        fhirCastWorkflowEventEvent.setContext( fhirCastContextList );

        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
        Date today = new Date();
        fhirCastWorkflowEvent.setTimestamp( ""+today );
        fhirCastWorkflowEvent.setId( UUID.randomUUID().toString() );
        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );

        RestTemplate restTemplate = new RestTemplate(  );
        logger.info("send event");
        restTemplate.postForLocation( this.topicUrl+"/websub", fhirCastWorkflowEvent );
    }


    public void newEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        logger.info(  "New event "+fhirCastWorkflowEvent.getEvent() );
//        switch( fhirCastWorkflowEvent.getEvent().getHub_event() ){
//            case OPEN_PATIENT_CHART:
//            case SWITCH_PATIENT_CHART:
        FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent = fhirCastWorkflowEvent.getEvent();
        for( FhirCastContext fhirCastContext: fhirCastWorkflowEventEvent.getContext()){
//                    FhirResource fhirResource = fhirCastContext.getResource();
            IBaseResource resource = ourCtx.newJsonParser().parseResource( fhirCastContext.getResource() );
            this.context.put( fhirCastContext.getKey(), resource);
        }
//                break;
//        }
        if ( this.context.containsKey( "patient" )){
            this.patient = (Patient) this.context.get( "patient" );
        }

    }

    public void getContext() {
        RestTemplate restTemplate =  new RestTemplate(  );
        FhirCastWorkflowEvent fhirCastWorkflowEvent = restTemplate.getForObject( topicUrl+"/websub", FhirCastWorkflowEvent.class );
        System.out.println(fhirCastWorkflowEvent);
    }

}

