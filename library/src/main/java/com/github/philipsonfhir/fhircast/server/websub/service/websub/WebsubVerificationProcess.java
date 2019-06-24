package com.github.philipsonfhir.fhircast.server.websub.service.websub;

import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubClient;
import com.github.philipsonfhir.fhircast.server.websub.model.FhirCastSessionSubscribe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class WebsubVerificationProcess implements Runnable {

    private final FhirCastWebsubClient fhirCastWebsubClient;
    private final FhirCastSessionSubscribe fhirCastSessionSubscribe;
    private final String url;
    private String challenge;
    private boolean ready = false;
    private boolean success;
    private Logger logger = Logger.getLogger( this.getClass().getName() );

    public static WebsubVerificationProcess verify(FhirCastWebsubClient fhirCastWebsubClient, FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        fhirCastWebsubClient.setVerified( false );
        WebsubVerificationProcess websubVerificationProcess = new WebsubVerificationProcess(fhirCastWebsubClient, fhirCastSessionSubscribe);
        Thread thread = new Thread( websubVerificationProcess );
        thread.setName("Verification");
        thread.start();
        return websubVerificationProcess;
    }

    WebsubVerificationProcess(FhirCastWebsubClient fhirCastWebsubClient, FhirCastSessionSubscribe fhirCastSessionSubscribe){
        this.fhirCastWebsubClient = fhirCastWebsubClient;
        this.fhirCastSessionSubscribe = fhirCastSessionSubscribe;
        this.url = fhirCastSessionSubscribe.getHub_callback();
    }

    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate(  );
//        GET https://app.example.com/session/callback/v7tfwuk17a?hub.mode=subscribe&hub.topic=7jaa86kgdudewiaq0wtu&hub.events=patient-open-chart,patient-close-chart&hub.challenge=meu3we944ix80ox HTTP 1.1
        this.challenge = generateChallenge();
        String query = String.format( "?hub.mode=%s&hub.topic=%s&hub.events=%s&hub.challenge=%s&hub.lease+seconds=%s",
            fhirCastSessionSubscribe.getHub_mode(),
            fhirCastSessionSubscribe.getHub_topic(),
            fhirCastSessionSubscribe.getHub_events(),
            challenge,
            "10000000"
            );

         ResponseEntity<String> response = restTemplate.getForEntity( url+query, String.class  );

        this.success = response.getStatusCode().value()>=200 && response.getStatusCode().value()<300 && response.getBody().equalsIgnoreCase( challenge );
        ready=true;
        this.fhirCastWebsubClient.setVerified(success);
        logger.info( "verification of "+fhirCastSessionSubscribe.getHub_callback()+" returned "+success );
    }


    static String generateChallenge() {
        SecureRandom generator = new SecureRandom();
        return String.valueOf(generator.nextInt() & Integer.MAX_VALUE);
    }
}