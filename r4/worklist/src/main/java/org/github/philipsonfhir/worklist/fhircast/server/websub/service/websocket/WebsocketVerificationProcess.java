package org.github.philipsonfhir.worklist.fhircast.server.websub.service.websocket;

public class WebsocketVerificationProcess implements Runnable {
    @Override
    public void run() {

    }
//
//    private final FhirCastWebsubClient fhirCastWebsubClient;
//    private final FhirCastSessionSubscribe fhirCastSessionSubscribe;
//    private final String url;
//    private String challenge;
//    private boolean ready = false;
//    private boolean success;
//    private Logger logger = Logger.getLogger( this.getClass().getName() );
//
//    private static StateEnum state;
//    private enum StateEnum { IDLE, VERIFYING, VERIFIED, UNVERIFIED };
//
//    public static WebsocketVerificationProcess verify(FhirCastWebsubClient fhirCastWebsubClient, FhirCastSessionSubscribe fhirCastSessionSubscribe) {
//        fhirCastWebsubClient.setVerified( false );
//        state = state.IDLE;
//        WebsocketVerificationProcess websubVerificationProcess = new WebsocketVerificationProcess(fhirCastWebsubClient, fhirCastSessionSubscribe);
//        Thread thread = new Thread( websubVerificationProcess );
//        thread.setName("Verification");
//        thread.start();
//        return websubVerificationProcess;
//    }
//
//    WebsocketVerificationProcess(FhirCastWebsubClient fhirCastWebsubClient, FhirCastSessionSubscribe fhirCastSessionSubscribe){
//        this.fhirCastWebsubClient = fhirCastWebsubClient;
//        this.fhirCastSessionSubscribe = fhirCastSessionSubscribe;
//        this.url = fhirCastSessionSubscribe.getHub_callback();
//    }
//
//    @Override
//    public void run() {
//        RestTemplate restTemplate = new RestTemplate(  );
////        GET https://app.example.com/session/callback/v7tfwuk17a?hub.mode=subscribe&hub.topic=7jaa86kgdudewiaq0wtu&hub.events=patient-open-chart,patient-close-chart&hub.challenge=meu3we944ix80ox HTTP 1.1
//        this.challenge = generateChallenge();
//        String query = String.format( "?hub.mode=%s&hub.topic=%s&hub.events=%s&hub.challenge=%s&hub.lease+seconds=%s",
//            fhirCastSessionSubscribe.getHub_mode(),
//            fhirCastSessionSubscribe.getHub_topic(),
//            fhirCastSessionSubscribe.getHub_events(),
//            challenge,
//            "10000000"
//            );
//
//         ResponseEntity<String> response = restTemplate.getForEntity( url+query, String.class  );
//
//        this.success = response.getStatusCode().value()>=200 && response.getStatusCode().value()<300 && response.getBody().equalsIgnoreCase( challenge );
//        ready=true;
//        this.fhirCastWebsubClient.setVerified(success);
//        logger.info( "verification of "+fhirCastSessionSubscribe.getHub_callback()+" returned "+success );
//    }
//
//
//    static String generateChallenge() {
//        SecureRandom generator = new SecureRandom();
//        return String.valueOf(generator.nextInt() & Integer.MAX_VALUE);
//    }
}
