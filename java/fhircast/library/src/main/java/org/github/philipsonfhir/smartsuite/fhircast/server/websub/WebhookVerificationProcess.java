package org.github.philipsonfhir.smartsuite.fhircast.server.websub;

import org.github.philipsonfhir.smartsuite.fhircast.server.domain.SubscriptionUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

public class WebhookVerificationProcess  implements Runnable {

    private final FhirCastWebhookClient fhirCastWebsubClient;
    private final SubscriptionUpdate fhirCastSubscribtionRequest;
    private final String url;
    private boolean ready = false;
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger( this.getClass().getName() );
    private String challenge;
    private boolean success = false;

    public static WebhookVerificationProcess verify(FhirCastWebhookClient fhirCastWebsubClient, SubscriptionUpdate fhirCastSubscribtionRequest) {
        fhirCastWebsubClient.setVerified( false );
        WebhookVerificationProcess websubVerificationProcess = new WebhookVerificationProcess(fhirCastWebsubClient, fhirCastSubscribtionRequest);
        Thread thread = new Thread( websubVerificationProcess );
        thread.setName("Verification");
        thread.start();
        return websubVerificationProcess;
    }

    WebhookVerificationProcess(FhirCastWebhookClient fhirCastWebsubClient, SubscriptionUpdate fhirCastSubscribtionRequest){
        this.fhirCastWebsubClient = fhirCastWebsubClient;
        this.fhirCastSubscribtionRequest = fhirCastSubscribtionRequest;
        this.url = fhirCastSubscribtionRequest.getHub_callback();
    }

    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate(  );
//        GET https://app.example.com/session/callback/v7tfwuk17a?hub.mode=subscribe&hub.topic=7jaa86kgdudewiaq0wtu&hub.events=patient-open-chart,patient-close-chart&hub.challenge=meu3we944ix80ox HTTP 1.1
        this.challenge = generateChallenge();
        String query = String.format( "?hub.mode=%s&hub.topic=%s&hub.events=%s&hub.challenge=%s&hub.lease_seconds=%s",
                fhirCastSubscribtionRequest.getHub_mode(),
                fhirCastSubscribtionRequest.getHub_topic(),
                fhirCastSubscribtionRequest.getHub_events(),
                challenge,
                "10000000"
            );

         ResponseEntity<String> response = restTemplate.getForEntity( url+query, String.class  );

        this.success = response.getStatusCode().value()>=200 && response.getStatusCode().value()<300 && response.getBody().equalsIgnoreCase( challenge );
        ready=true;
        // TODO add resent of new events.
        this.fhirCastWebsubClient.setVerified(success);
        logger.info( "verification of "+fhirCastSubscribtionRequest.getHub_callback()+" returned "+success );
    }


    static String generateChallenge() {
        SecureRandom generator = new SecureRandom();
        return String.valueOf(generator.nextInt() & Integer.MAX_VALUE);
    }
}
