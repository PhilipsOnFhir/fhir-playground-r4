package com.github.philipsonfhir.fhircast.app;

import ca.uhn.fhir.context.FhirContext;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.server.websub.model.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class FhirCastWebsubClient {

    private WebsubCommunicationListener websubCommunicationListener = null;
    private WebsocketCommunicationListener websocketCommunicationListener = null;
    private int port;
    private final boolean rest;


    private FhirContext ourCtx = FhirContext.forDstu3();
    private String baseUrl;
    private String topicUrl = null;
    RestTemplate restTemplate = new RestTemplate(  );
    String topicId = null;
    private Patient patient = null ;
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private Map<String, IBaseResource> context = new TreeMap<String, IBaseResource>(  );
    private List<FhirCastWorkflowEvent> events = new ArrayList<>();


    //server.port=9601","fhircast.topic=123
    @Value("${server.port}")
    public void setPort( int port ) throws FhirCastException {
        this.baseUrl = "http://localhost:"+port;
        this.port = port;
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

    public FhirCastWebsubClient(String url, String topicId) throws DeploymentException, IOException, URISyntaxException {
        this( url, topicId, true );

    }

    public FhirCastWebsubClient(String baseUrl, String topicId, boolean rest) throws DeploymentException, IOException, URISyntaxException {
        this.rest = rest;
        this.baseUrl = baseUrl;
        this.topicUrl = baseUrl+"/"+ topicId;
        restTemplate.put( this.topicUrl, String.class  );
        this.topicId = topicId;

        if ( rest ) {
            port = new Random(System.currentTimeMillis()).nextInt(100) + 9300;
            websubCommunicationListener = new WebsubCommunicationListener(port, this);
            Thread thread = new Thread(websubCommunicationListener);
            thread.setName("com-list");
            thread.start();
        } else {
            websocketCommunicationListener = new WebsocketCommunicationListener( this );
        }
    }

    public List<String> getSessions(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity( this.baseUrl, String.class  );
        System.out.println(responseEntity);
        return new ArrayList<>(  );
    }

    public void logout() {
        try {
            restTemplate.delete(this.topicUrl);
            this.topicId = null;
        } catch (HttpServerErrorException error) {
            System.out.println("Http error " + error.getStatusText());
        }
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


        RestTemplate restTemplate = new RestTemplate();
        logger.info("send event");
        restTemplate.postForLocation(this.topicUrl, fhirCastWorkflowEvent);
    }

    public void unSubscribePatientChange() throws DeploymentException, IOException, URISyntaxException {
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_mode( "unsubscribe" );
        fhirCastSessionSubscribe.setHub_topic( topicId );
        fhirCastSessionSubscribe.setHub_secret("mysecret");
        fhirCastSessionSubscribe.setHub_channel_type("websocket");
        fhirCastSessionSubscribe.setHub_events( FhircastEventType.OPEN_PATIENT_CHART+","+ FhircastEventType.SWITCH_PATIENT_CHART+","+ FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.USER_LOGOUT ); //"patient-open-chart,patient-logout-chart" );

        ResponseEntity<String> websockUrl = restTemplate.postForEntity(topicUrl, fhirCastSessionSubscribe, String.class);

        if ( !this.rest  ) {
            this.websocketCommunicationListener.connect( websockUrl.getBody() );
        }
    }

    public void subscribePatientChange() throws DeploymentException, IOException, URISyntaxException {
        FhirCastSessionSubscribe fhirCastSessionSubscribe = new FhirCastSessionSubscribe();
        fhirCastSessionSubscribe.setHub_callback( "http://localhost:"+port );
        fhirCastSessionSubscribe.setHub_mode( "subscribe" );
        fhirCastSessionSubscribe.setHub_topic( topicId );
        fhirCastSessionSubscribe.setHub_secret("mysecret");
        fhirCastSessionSubscribe.setHub_events( FhircastEventType.OPEN_PATIENT_CHART+","+ FhircastEventType.SWITCH_PATIENT_CHART+","+ FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.CLOSE_PATIENT_CHART+","+FhircastEventType.USER_LOGOUT ); //"patient-open-chart,patient-logout-chart" );
        fhirCastSessionSubscribe.setHub_channel_type("websocket");
        ResponseEntity<String> websockUrl = restTemplate.postForEntity( topicUrl, fhirCastSessionSubscribe, String.class );

        if ( !this.rest  ) {
            this.websocketCommunicationListener.connect( websockUrl.getBody() );
        }
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
        restTemplate.postForLocation( this.topicUrl, fhirCastWorkflowEvent );
    }

    public List<FhirCastWorkflowEvent> getEvents(){
        return Collections.unmodifiableList(events);
    }

    public void clearEvents(){
        this.events.clear();
    }

    public void newEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent) {
        logger.info(  "New event "+fhirCastWorkflowEvent.getEvent() );
        this.events.add( fhirCastWorkflowEvent );
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
        FhirCastWorkflowEvent fhirCastWorkflowEvent = restTemplate.getForObject( topicUrl+"/model", FhirCastWorkflowEvent.class );
        System.out.println(fhirCastWorkflowEvent);
    }

}

