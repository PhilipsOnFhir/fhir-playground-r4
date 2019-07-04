package org.github.philipsonfhir.fhircast.server.websub.service.websub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.fhircast.server.websub.service.FhirCastWebsubClient;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastSessionSubscribe;
import org.github.philipsonfhir.fhircast.server.websub.domain.FhirCastWorkflowEvent;
import org.github.philipsonfhir.fhircast.support.NotImplementedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

public class FhircastWebsubClientRest extends FhirCastWebsubClient {
    private Logger logger = Logger.getLogger( this.getClass().getName() );
    private final String clientCallbackUrl;

    public FhircastWebsubClientRest(FhirCastSessionSubscribe fhirCastSessionSubscribe) {
        super( fhirCastSessionSubscribe.getHub_secret() );

        // TODO change to channel-type-endpoint?
        this.clientCallbackUrl = fhirCastSessionSubscribe.getHub_callback();
        WebsubVerificationProcess.verify(this , fhirCastSessionSubscribe );

    }

    public String getClientCallbackUrl() {
        return clientCallbackUrl;
    }

    RestTemplate restTemplate = new RestTemplate();
    @Override
    public void  sendEvent(FhirCastWorkflowEvent fhirCastWorkflowEvent ) throws FhirCastException {

        // send model events
        logger.info("eventReceived " + fhirCastWorkflowEvent);
        if (isVerified() && hasSubscription(fhirCastWorkflowEvent.getEvent().getHub_event())) {
            logger.info("Sending event to " + getClientCallbackUrl());

            HttpHeaders httpHeaders = new HttpHeaders();
            ObjectMapper objectMapper = new ObjectMapper();
            String content = null;
            try {
                content = objectMapper.writeValueAsString(fhirCastWorkflowEvent);
            } catch (JsonProcessingException e) {
                throw new FhirCastException("parsing Event failed");
            }

            httpHeaders.add("X-Hub-Signature", calculateHMAC(content));

            HttpEntity<String> entity = new HttpEntity<>(content, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(
                    getClientCallbackUrl(),
                    HttpMethod.POST, entity, String.class
            );
            logger.info("Sending event to " + getClientCallbackUrl() + " : " + response.getStatusCode());
        }
    }

    @Override
    public void subscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws NotImplementedException {
        super.subscribe( fhirCastSessionSubscribe.getHub_events());
    }

    @Override
    public void unsubscribe(FhirCastSessionSubscribe fhirCastSessionSubscribe) throws NotImplementedException {
        super.subscribe( fhirCastSessionSubscribe.getHub_events() );
    }

    @Override
    public String getSubscriptionReturn() {
        return "";
    }

}
