//package org.github.philipsonfhir.fhircast.topic;
//
//import org.github.philipsonfhir.fhircast.common.FhirCastException;
//import org.github.philipsonfhir.fhircast.common.Prefix;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RestController
//@CrossOrigin(origins = "*")
//public class FhirCastTopicController {
//
//    private final FhirCastTopicService fhirCastTopicService;
//
//    @Autowired
//    public FhirCastTopicController( FhirCastTopicService fhirCastTopicService ){
//        this.fhirCastTopicService = fhirCastTopicService;
//    }
//
//    @PostMapping( Prefix.BASE )
//    public ResponseEntity<String> createFhirCastSession(
//            HttpServletRequest request,
//            @RequestBody String requestBody) {
//        System.out.println( request.getLocalAddr() );
//        System.out.println( request.getLocalName() );
//
//        FhirCastTopic fhirCastTopic = fhirCastTopicService.createTopic();
//        ResponseEntity<String> responseEntity = new ResponseEntity( fhirCastTopic.getTopic(), HttpStatus.CREATED );
//
//        return responseEntity;
//    }
//
////    @GetMapping( Prefix.FHIRCAST )
////    public List<String> getFhirCastServices()  {
////        return fhirCastTopicService.getTopics().stream()
////                .map( fhirCastTopic -> fhirCastTopic.getTopic() )
////                .collect( Collectors.toList());
////    }
//
//    @PutMapping( Prefix.BASE+"/{sessionId}" )
//    public void updateFhirCastService( @PathVariable String sessionId) throws FhirCastException {
//        fhirCastTopicService.updateTopic( sessionId );
//    }
//
//    @DeleteMapping( Prefix.BASE+"/{sessionId}" )
//    public void removeFhirCastService( @PathVariable String sessionId) throws FhirCastException {
//        fhirCastTopicService.removeTopic( sessionId );
//    }
//
//}
