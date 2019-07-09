package org.github.philipsonfhir.auth;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.github.philipsonfhir.fhircast.WorklistServer;
import org.github.philipsonfhir.fhircast.server.Prefix;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorklistServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTests {

    @LocalServerPort
    private long port;

    @BeforeClass
    public static void beforeClass(){
        System.setProperty("webdriver.chrome.driver", "C:/Data/CONNECT/chromedriver/chromedriver74.exe");
    }

    @Test
    public void login() throws InterruptedException, IOException {
//        port = 8080;

        String baseUrl = "http://localhost:"+port;
        String redirectUri  = "http://localhost:8080/test";
        String clientId = "worklist";
        String clientIdSecret = "worklist-secret";
        String[] arScope = {"topic"};

        String scopes = arScope[0];
        for ( int i=1; i<arScope.length; i++ ){
            scopes+=","+arScope[0];
        }

        WebDriver driver = new ChromeDriver();

        String state= "98wrghuwuogerg97";

        doStep1(baseUrl, redirectUri, driver, state, clientId, scopes);

        agreeOnScope(baseUrl, redirectUri, driver, clientId, arScope);

        String code = getCode(redirectUri, driver, state);

        String accessToken = getAccessToken(redirectUri, code, clientId, clientIdSecret);

        RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);

        //Create a new HttpEntity
        final HttpEntity<String> entity = new HttpEntity<String>(headers);

        //Execute the method writing your HttpEntity to the request
        ResponseEntity<String> response = restTemplate.exchange(baseUrl+ Prefix.FHIRCAST_TOPIC, HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
        assertTrue( response.getBody().startsWith("["));
        driver.quit();
    }

    private String getAccessToken(String redirectUri, String code, String clientId, String clientIdSecret ) throws IOException {
        System.out.println("Get token");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        httpHeaders.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString((clientId+":"+clientIdSecret).getBytes()));

        MultiValueMap<String, String> map =new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("code",code );
        map.add("redirect_uri", redirectUri );

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);


        String tokenUrl="http://localhost:"+port+"/oauth/token";

        ResponseEntity<String> response =
                restTemplate.exchange(tokenUrl,
                        HttpMethod.POST,
                        entity,
                        String.class);

        assertEquals( HttpStatus.OK, response.getStatusCode() );
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue( jsonNode.has("access_token"));
        assertTrue( jsonNode.has("token_type"));
        assertTrue( jsonNode.has("scope"));
        assertTrue( jsonNode.get("access_token").asText().length()>1);
        assertTrue( jsonNode.get("token_type").asText().length()>1);
        assertTrue( jsonNode.get("scope").asText().length()>1);

        System.out.println(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    private String getCode(String redirectUri, WebDriver driver, String state) {
        String localUrl = driver.getCurrentUrl();
        System.out.println(localUrl);
        assertTrue(localUrl.startsWith(redirectUri));
        assertTrue(localUrl.endsWith("state="+state));

        String code = localUrl.substring(localUrl.indexOf("code=")+5);
        code = code.substring(0, code.indexOf("&"));
        return code;
    }

    private void agreeOnScope(String baseUrl, String redirectUri, WebDriver driver, String clientId, String[] scopes ) throws InterruptedException {
        System.out.println("agree on scope");
        if ( !driver.getCurrentUrl().startsWith(redirectUri) ) {
            assertEquals(baseUrl + "/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri=" + redirectUri + "&launch=xyz123&scope="+scopes[0]+"&state=98wrghuwuogerg97&aud=https://ehr/fhir", driver.getCurrentUrl());
            //        searchBox.sendKeys("ChromeDriver");
            //        searchBox.submit();
            for ( String scope: scopes) {
                driver.findElement(By.name("scope."+scope )).click();
//                driver.findElement(By.name("scope.write")).click();
            }
            driver.findElement(By.name("authorize")).click();
            Thread.sleep(1000);  // Let the user actually see something!
        }
    }

    private void doStep1(String baseUrl, String redirectUri, WebDriver driver, String state, String clientId, String scopes) throws InterruptedException {
        System.out.println("GET /oauth/response");
        driver.get("http://localhost:"+port+"/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri+"&launch=xyz123&scope="+scopes+"&state="+state+"&aud=https://ehr/fhir");
        Thread.sleep(1000);  // Let the user actually see something!

        System.out.println("login");
        assertEquals(baseUrl+"/login", driver.getCurrentUrl());
        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        username.sendKeys("admin");
        password.sendKeys("admin");
        driver.findElement(By.xpath("/html/body/div/form/button")).click();
    }

    @Test

    public void chromeSearch() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com/xhtml");
        Thread.sleep(5000);  // Let the user actually see something!
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("ChromeDriver");
        searchBox.submit();
        Thread.sleep(5000);  // Let the user actually see something!
        driver.quit();
    }


}
