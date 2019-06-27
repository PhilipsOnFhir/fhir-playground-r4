//package org.github.philipsonfhir.smartonfhir.secureproxy.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.github.philipsonfhir.smartonfhir.PathValues;
//import org.github.philipsonfhir.smartonfhir.SmartonfhirApplication;
//import org.github.philipsonfhir.smartonfhir.basic.controller.service.FhirServer;
//import org.hl7.fhir.r4.model.*;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SmartonfhirApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//
//public class SmartConfigurationTest {
//    @LocalServerPort
//    private long port;
//
//    @Value("${proxy.authorization-endpoint}") String authorizationEndpoint;
//    @Value("${proxy.token-endpoint}") String tokenEndpoint;
//
//    @Test
//    public void getSmartConfiguration() throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseEntity =
//                restTemplate.getForEntity( "http://localhost:"+port+"/"+ PathValues.fhirProxy +"/.well-known/smart-configuration", String.class);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        String response = responseEntity.getBody();
//        assertNotNull( response );
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root = mapper.readTree(response);
//        String wellKnownAuthorization_endpoint = root.path("authorization_endpoint").asText();
//        String wellKnownToken_endpoint = root.path("token_endpoint").asText();
//        JsonNode capabilities = root.path("capabilities");
//
//        assertEquals( authorizationEndpoint, wellKnownAuthorization_endpoint);
//        assertEquals( tokenEndpoint, wellKnownToken_endpoint);
//    }
//
//    @Test
//    public void getSmartConfigurationCPS() {
//        String url = "http://localhost:"+port+"/smartonfhir";
////        FhirServer fhirServer = new FhirServer("http://localhost:"+port+"/"+ PathValues.fhirProxy +"/metadata");
//        FhirServer fhirServer = new FhirServer(url );
//        CapabilityStatement capabilityStatement =fhirServer.getCapabilityStatement();
//        assertNotNull(capabilityStatement);
//
//        assertNotNull( capabilityStatement.getRest() );
//        assertNotEquals( 0, capabilityStatement.getRest().size() );
//        capabilityStatement.getRest().forEach( rest -> {
//            assertNotNull( rest.getSecurity() );
//
//            CapabilityStatement.CapabilityStatementRestSecurityComponent security = rest.getSecurity();
//            assertNotNull( security );
//            assertEquals( 1, security.getService().size());
//            Coding coding = security.getServiceFirstRep().getCodingFirstRep();
//            assertEquals( "http://hl7.org/fhir/restful-security-service", coding.getSystem());
//            assertEquals( "SMART-on-FHIR", coding.getCode());
//
//            Extension extension = security.getExtensionFirstRep();
//            assertNotNull( extension );
//            assertEquals("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris", extension.getUrl());
//            assertEquals( 2, extension.getExtension().size());
//
//            extension.getExtension().forEach( subEx -> {
//                if ( subEx.getUrl().equals("authorize")){
//                    assertEquals( authorizationEndpoint, ((UriType)subEx.getValue()).getValue() );
//                }else if ( subEx.getUrl().equals("token")){
//                    assertEquals( tokenEndpoint, ((UriType)subEx.getValue()).getValue() );
//                } else{
//                    fail("Unknown extention url "+ subEx.getUrl());
//                }
//            });
//
//        });
//    }
//}