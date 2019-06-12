package org.github.philipsonfhir.smartonfhir.secureproxy.controller;

import org.github.philipsonfhir.smartonfhir.PathValues;
import org.github.philipsonfhir.smartonfhir.basic.controller.FhirProxyController;
import org.github.philipsonfhir.smartonfhir.secureproxy.support.SmartOnFhirServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmartOnFhirProxyController extends FhirProxyController {


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
            value = "/"+ PathValues.fhirProxy+"/.well-known/smart-configuration",
//            value = "/"+ PathValues.smartonfhirProxy+"/test",
            produces = "application/json"
    )
    public SmartConfiguration getSmartConfiguration(){
        SmartConfiguration smartConfiguration = new SmartConfiguration();
        smartConfiguration.authorization_endpoint = authorizationEndpoint;
        smartConfiguration.token_endpoint = tokenEndpoint;
        smartConfiguration.capabilities = "[\"launch-ehr\", \"client-public\", \"client-confidential-symmetric\", \"context-ehr-patient\", \"sso-openid-connect\"]";
        return smartConfiguration;
    }


    private class SmartConfiguration {
        public String authorization_endpoint;
        public String token_endpoint;
        public String capabilities;

    }
}