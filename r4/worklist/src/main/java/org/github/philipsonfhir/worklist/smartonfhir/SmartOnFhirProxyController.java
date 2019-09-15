package org.github.philipsonfhir.worklist.smartonfhir;

import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.github.philipsonfhir.smartonfhir.basic.controller.FhirProxyController;
import org.github.philipsonfhir.smartonfhir.secureproxy.support.SmartOnFhirServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
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
            value = Prefix.FHIRCAST_FHIR +"/auth"
    )
    public RedirectView authRedirect( RedirectAttributes attributes ){
        return new RedirectView(tokenEndpoint);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = prefix+"/.well-known/smart-configuration",
//            value = "/"+ PathValues.smartonfhirProxy+"/test",
            produces = "application/json"
    )
    public SmartConfiguration getSmartConfiguration( @PathVariable String topicId, HttpServletRequest request ){
        String url = request.getRequestURL().toString();
        url.substring(0, url.indexOf(Prefix.FHIRCAST_FHIR));

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
            @PathVariable String topicId,
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String resourceType,
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request
    ){
        String url = request.getRequestURL().toString();
        url.substring(0, url.indexOf(Prefix.FHIRCAST_FHIR));
        return  super.getResourceType( request, accept, resourceType, queryParams );
    }

    @RequestMapping (
            method = RequestMethod.GET,
            value = prefix+"/{resourceType}/{resourceId}"
    )
    public ResponseEntity<String> getResource(
            @PathVariable String topicId,
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            @PathVariable String resourceType,
            @RequestParam Map<String, String> queryParams,
            @PathVariable String resourceId
    ){
        return  super.getResource( accept, resourceType, resourceId, queryParams );
    }

    private class SmartConfiguration {
        public String authorization_endpoint;
        public String token_endpoint;
        public String capabilities;

    }
}
