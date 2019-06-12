package org.github.philipsonfhir.fhircast.auth;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        String state= "98wrghuwuogerg97";

        WebDriver driver = new ChromeDriver();

        System.out.println("GET /oauth/response");
        driver.get("http://localhost:"+port+"/oauth/authorize?response_type=code&client_id=fhircasthub&redirect_uri="+redirectUri+"&launch=xyz123&scope=read%20write&state="+state+"&aud=https://ehr/fhir");
        Thread.sleep(1000);  // Let the user actually see something!

        System.out.println("login");
        assertEquals(baseUrl+"/login", driver.getCurrentUrl());
        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        username.sendKeys("admin");
        password.sendKeys("admin");
        driver.findElement(By.xpath("/html/body/div/form/button")).click();

        System.out.println("agree on scope");
        if ( !driver.getCurrentUrl().startsWith(redirectUri) ) {
            assertEquals(baseUrl + "/oauth/authorize?response_type=code&client_id=fhircasthub&redirect_uri=" + redirectUri + "&launch=xyz123&scope=read%20write&state=98wrghuwuogerg97&aud=https://ehr/fhir", driver.getCurrentUrl());
            //        searchBox.sendKeys("ChromeDriver");
            //        searchBox.submit();
            driver.findElement(By.name("scope.read")).click();
            driver.findElement(By.name("scope.write")).click();
            driver.findElement(By.name("authorize")).click();

            Thread.sleep(1000);  // Let the user actually see something!
        }

        String localUrl = driver.getCurrentUrl();
        assertTrue(localUrl.startsWith(redirectUri));
        assertTrue(localUrl.endsWith("state="+state));

        String code = localUrl.substring(localUrl.indexOf("code=")+5);
        code = code.substring(0, code.indexOf("&"));

        System.out.println("Get token");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        httpHeaders.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString("fhircasthub:fhircast-secret".getBytes()));

        MultiValueMap<String,String> map =new LinkedMultiValueMap<String, String>();
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
        driver.quit();
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
