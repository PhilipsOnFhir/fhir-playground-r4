package com.github.philipsonfhir.fhircast.server.websocket;

import com.github.philipsonfhir.fhircast.server.service.FhirCastContextService;
import com.github.philipsonfhir.fhircast.server.service.FhirCastTopic;
import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubService;
import com.github.philipsonfhir.fhircast.support.FhirCastException;
import com.github.philipsonfhir.fhircast.support.NotImplementedException;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastContext;
import com.github.philipsonfhir.fhircast.support.websub.FhirCastWorkflowEventEvent;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastWebsocketController  {

    private Logger logger = Logger.getLogger( this.getClass().getName());

    @Autowired
    private FhirCastContextService fhirCastContextService;
    private Map<String, WebsocketEventSender > map = new TreeMap<>(  );

//    @SubscribeMapping("/fhircast/*")
//    public void subscribe(@DestinationVariable String topic,@DestinationVariable String event  ) {
//        logger.info("WebSocket subscribe "+topic+"/"+event );
//        try {
//            fhirCastContextService.updateTopic( topic );
//            WebsocketEventSender websocketEventSender = map.get( topic );
//            if ( websocketEventSender == null ) {
//                websocketEventSender = new WebsocketEventSender( topic );
//                map.put( topic, websocketEventSender );
//                fhirCastContextService.getTopic( topic ).registerFhirCastTopicEventListener( websocketEventSender );
//            }
//        }catch ( FhirCastException e ){
//            e.printStackTrace();
//        }
//    }

    @MessageMapping("/fhircast/{topic}/{event}")
    @SendTo("/hub/fhircast/{topic}/{event}")
    public FhirCastWorkflowEventEvent fhircastEvent(@DestinationVariable String topic,@DestinationVariable String event, FhirCastWorkflowEventEvent fhirCastEvent) throws NotImplementedException {
        logger.info("WebSocket event "+topic+"/"+event+"/"+fhirCastEvent );
        return fhirCastEvent;
    }

    public void eventReceived(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException, NotImplementedException {
        FhirCastTopic fhirCastTopic = this.fhirCastContextService.getTopic( fhirCastWorkflowEventEvent.getHub_topic() );
        switch( fhirCastWorkflowEventEvent.getHub_event() ){
            case OPEN_PATIENT_CHART: {
                Patient patient = getPatientFromContext( fhirCastWorkflowEventEvent );
                fhirCastTopic.openPatientChart( patient );
                break;
            }
            case CLOSE_PATIENT_CHART:
                fhirCastTopic.closeCurrent();
                break;
            case SWITCH_PATIENT_CHART:{
                Patient patient = getPatientFromContext( fhirCastWorkflowEventEvent );
                fhirCastTopic.switchPatient( patient );
                break;
            }
            default:
                throw new NotImplementedException(  );
        }
//        FhirCastWorkflowEvent fhirCastWorkflowEvent = new FhirCastWorkflowEvent();
//        fhirCastWorkflowEvent.setEvent( fhirCastWorkflowEventEvent );
//        fhirCastWorkflowEvent.setId( "WS"+System.currentTimeMillis() );
//        fhirCastWorkflowEvent.setTimestamp( ""+new Date() );
//        eventReceived( fhirCastWorkflowEventEvent.getHub_topic(), fhirCastWorkflowEvent );
    }

    private Patient getPatientFromContext(FhirCastWorkflowEventEvent fhirCastWorkflowEventEvent ) throws FhirCastException {
        Optional<FhirCastContext> opt = fhirCastWorkflowEventEvent.getContext()
            .stream()
            .filter( fhirCastContext -> fhirCastContext.getKey().equals( "patient" ) )
            .findFirst();
        if ( opt.isPresent() ){
            return (Patient) opt.get().retrieveFhirResource();
        }
        throw new FhirCastException( "Context should contain patient" );
    }
}
