//package com.github.philipsonfhir.fhircast.server.ws;
//
//import com.github.philipsonfhir.fhircast.server.topic.FhirCastTopicEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Controller;
//
//import javax.websocket.EncodeException;
//import java.io.IOException;
//
//@Controller
//public class appserver implements ApplicationListener<FhirCastTopicEvent> {
//    public appserver(){
//
//    }
//
//    @Override
//    public void onApplicationEvent(FhirCastTopicEvent event) {
//        Message message = new Message();
//        message.setContent( event.getTopic()+"-"+event.getEventType() );
//        try {
//            ChatEndpoint.broadcast( message );
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        } catch ( EncodeException e ) {
//            e.printStackTrace();
//        }
//    }
//}
