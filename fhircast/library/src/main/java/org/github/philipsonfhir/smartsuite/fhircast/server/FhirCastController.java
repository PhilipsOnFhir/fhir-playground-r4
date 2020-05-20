package org.github.philipsonfhir.smartsuite.fhircast.server;

import org.github.philipsonfhir.smartsuite.Prefix;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.HubPostData;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.FhirCastService;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.github.philipsonfhir.smartsuite.fhircast.server.topic.TopicService;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class FhirCastController {

    private final FhirCastService fhirCastService;
    private final TopicService topicService;

    @Autowired
    public FhirCastController(FhirCastService fhirCastService, TopicService topicService ){
        this.fhirCastService = fhirCastService;
        this.topicService = topicService;
    }

    @RequestMapping( value = Prefix.FHIRCAST,  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public ResponseEntity<String> SendMessage(
            HttpServletRequest request,
            @RequestBody HubPostData body
    ){
        ResponseEntity<String> responseEntity = new ResponseEntity<>( HttpStatus.ACCEPTED );
//        this.hub_channel_type!=null && this.id==null;
        if ( body.isSubscribeUnsubscribe() ) {
            return new ResponseEntity<>("wrong protocol - should be FORM-URL-ENCODED", HttpStatus.UNSUPPORTED_MEDIA_TYPE );
        } else {
            ContextEvent fhircastEvent = body.getFhirCastEvent();
            if ( fhircastEvent.getEvent()==null ){
                return new ResponseEntity<>( "Empty event", HttpStatus.BAD_REQUEST);
            }
            if ( fhircastEvent.getEvent().getHub_topic()==null ){
                return new ResponseEntity<>( "Topic not set", HttpStatus.BAD_REQUEST);
            }
            if ( !topicService.hasTopic( fhircastEvent.getEvent().getHub_topic() ) ){
                return new ResponseEntity<>( "Topic does not exist", HttpStatus.NOT_FOUND );
            }

            SendEventResult sendEventResult = fhirCastService.sendEvent(fhircastEvent);
            if ( sendEventResult.hasErrorOccurred()){
                return new ResponseEntity<>( "One or more messages has not not been delivered.", HttpStatus.GATEWAY_TIMEOUT );
            }
        }
        return responseEntity;    }

    @RequestMapping( value = Prefix.FHIRCAST,  method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    @ResponseBody
    ResponseEntity<String> subscribeOrUnsubscribe(
            @RequestBody final MultiValueMap<String, String > formVars,
            HttpServletRequest request
    ){
        ResponseEntity<String> responseEntity = new ResponseEntity<>( HttpStatus.ACCEPTED );
        HubPostData body;
        boolean isSubscribeUnsubscribe = formVars.containsKey("hub.channel.type") && !formVars.containsKey("hub.id");
//        this.hub_channel_type!=null && this.id==null;
        try {
            if ( isSubscribeUnsubscribe ) {
                SubscriptionUpdate subscriptionUpdate = new SubscriptionUpdate( formVars );
                if ( subscriptionUpdate.getHub_topic() ==null ){
                    return new ResponseEntity<>( "Topic not set", HttpStatus.BAD_REQUEST);
                }
                if ( !topicService.hasTopic( subscriptionUpdate.getHub_topic()) ){
                    return new ResponseEntity<>( "Topic does not exist", HttpStatus.NOT_FOUND );
                }

                String wsId = fhirCastService.subscribeOrUnsubscribe( subscriptionUpdate );
                String wsUrl =
                        ( wsId.isBlank() ? "" : "ws"+//"wss"+
                        request.getRequestURL().substring(request.getRequestURL().indexOf(":"), request.getRequestURL().lastIndexOf(Prefix.FHIRCAST)) +
                        Prefix.FHIRCAST_WEBSOCKET+"/"+wsId
                );
                responseEntity = new ResponseEntity<>( wsUrl, HttpStatus.ACCEPTED );
            } else {
                return new ResponseEntity<>("wrong protocol", HttpStatus.UNSUPPORTED_MEDIA_TYPE );
            }
        } catch ( FhirCastException e ){
            responseEntity = new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }




}
