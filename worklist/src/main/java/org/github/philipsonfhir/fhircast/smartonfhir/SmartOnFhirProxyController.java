package org.github.philipsonfhir.fhircast.smartonfhir;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.github.philipsonfhir.smartonfhir.PathValues;
import org.github.philipsonfhir.smartonfhir.basic.controller.FhirProxyController;
import org.github.philipsonfhir.smartonfhir.secureproxy.support.SmartOnFhirServer;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin( origins = "*")
@RestController
public class SmartOnFhirProxyController extends FhirProxyController {

    private final String prefix = "api/fhircast/fhir/{topicId}";
    private final String authorizationEndpoint;
    private final String tokenEndpoint;
    private org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass());

    SmartOnFhirProxyController(
            @Value("${proxy.fhirserver.url}") String fhirServerUrl,
            @Value("${proxy.authorization-endpoint}") String authorizationEndpoint,
            @Value("${proxy.token-endpoint}") String tokenEndpoint
    ){
        super( new SmartOnFhirServer(fhirServerUrl, authorizationEndpoint, tokenEndpoint) );
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = prefix+"/.well-known/smart-configuration",
//            value = "/"+ PathValues.smartonfhirProxy+"/test",
            produces = "application/json"
    )
    public SmartConfiguration getSmartConfiguration( @PathVariable String topicId ){
        SmartConfiguration smartConfiguration = new SmartConfiguration();
        smartConfiguration.authorization_endpoint = authorizationEndpoint;
        smartConfiguration.token_endpoint = tokenEndpoint;
        smartConfiguration.capabilities = "[\"launch-ehr\", \"client-public\", \"client-confidential-symmetric\", \"context-ehr-patient\", \"sso-openid-connect\"]";
        return smartConfiguration;
    }

    @RequestMapping (
            method = RequestMethod.GET,
            value = prefix+"/{resourceType}"
    )
    public ResponseEntity<String> getResourceType(
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String resourceType,
            @RequestParam Map<String, String> queryParams,
            @PathVariable String topicId
    ){
        return  super.getResourceType( accept, resourceType, queryParams );
    }


    private class SmartConfiguration {
        public String authorization_endpoint;
        public String token_endpoint;
        public String capabilities;

    }
}
