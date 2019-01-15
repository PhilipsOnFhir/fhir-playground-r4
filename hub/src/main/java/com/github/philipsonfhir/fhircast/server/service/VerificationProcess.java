package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.support.websub.*;
import com.sun.javafx.binding.StringFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class VerificationProcess implements Runnable {

    private final FhirCastClientData fhirCastClientData;
    private final FhirCastSessionSubscribe fhirCastSessionSubscribe;
    private final String url;
    private String challenge;
    private boolean ready = false;
    private boolean success;
    private Logger logger = Logger.getLogger( this.getClass().getName() );

    public static VerificationProcess verify(FhirCastClientData fhirCastClientData, FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        fhirCastClientData.setVerified( false );
        VerificationProcess verificationProcess = new VerificationProcess( fhirCastClientData, fhirCastSessionSubscribe);
        new Thread( verificationProcess ).start();
        return verificationProcess;
    }

    VerificationProcess(FhirCastClientData fhirCastClientData, FhirCastSessionSubscribe fhirCastSessionSubscribe){
        this.fhirCastClientData = fhirCastClientData;
        this.fhirCastSessionSubscribe = fhirCastSessionSubscribe;
        this.url = fhirCastSessionSubscribe.getHub_callback();
    }

    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate(  );
//        GET https://app.example.com/session/callback/v7tfwuk17a?hub.mode=subscribe&hub.topic=7jaa86kgdudewiaq0wtu&hub.events=patient-open-chart,patient-close-chart&hub.challenge=meu3we944ix80ox HTTP 1.1
        this.challenge = generateChallenge();
        String query = StringFormatter.format( "?hub.mode=%s&hub.topic=%s&hub.events=%s&hub.challenge=%s&hub.lease+seconds=%s",
            fhirCastSessionSubscribe.getHub_mode(),
            fhirCastSessionSubscribe.getHub_topic(),
            fhirCastSessionSubscribe.getHub_events(),
            challenge,
            "10000000"
            ).getValue();

         ResponseEntity<String> response = restTemplate.getForEntity( url+query, String.class  );

        this.success = response.getStatusCode().value()>=200 && response.getStatusCode().value()<300 && response.getBody().equalsIgnoreCase( challenge );
        ready=true;
        this.fhirCastClientData.setVerified(success);
        logger.info( "verification of "+fhirCastSessionSubscribe.getHub_callback()+" returned "+success );
    }


    static String generateChallenge() {
        SecureRandom generator = new SecureRandom();
        return String.valueOf(generator.nextInt() & Integer.MAX_VALUE);
    }
}
