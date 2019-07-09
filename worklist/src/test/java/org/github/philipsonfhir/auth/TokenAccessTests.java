package org.github.philipsonfhir.auth;

import org.github.philipsonfhir.fhircast.WorklistServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorklistServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenAccessTests {
    @LocalServerPort
    private long port;

    @Test
    public void contextServerManagerLogin(){
        String baseUrl = String.format("http://localhost:%d", port);

        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientId("contextserver-manager");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setAccessTokenUri(baseUrl+"/oauth/token");
        resourceDetails.setGrantType("client_credentials");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext );

        restTemplate.getAccessToken();
        restTemplate.getForObject(baseUrl+"/hello", String.class );


    }

}
