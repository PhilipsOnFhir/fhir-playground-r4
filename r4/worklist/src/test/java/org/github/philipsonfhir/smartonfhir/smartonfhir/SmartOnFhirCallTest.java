//package org.github.philipsonfhir.smartonfhir.smartonfhir;
//
//import org.junit.Test;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.web.FilterChainProxy;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
//
//public class SmartOnFhirCallTest implements RedirectReceivedInterface {
//
//    @Test
//    public void smartOnFhirCall() throws IOException, InterruptedException {
//        OAuth2RestTemplate oAuth2RestTemplate;
//        //curl acme:acmesecret@localhost:8080/oauth/token -d grant_type=client_credentials
//        //{"access_token":"370592fd-b9f8-452d-816a-4fd5c6b4b8a6","token_type":"bearer","expires_in":43199,"scope":"read write"}
//
////        URL url = new URL("http://localhost:8080/oauth/token");
////        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
////        conn.setRequestMethod("GET");
//
//        int port = new Random( System.currentTimeMillis() ).nextInt( 100 )+9300;
//        CommunicationListener communicationListener = new CommunicationListener((int) port, this );
//        new Thread( communicationListener ).start();
//
////        String urlStr = url.replace("9641",""+port)
//
//        String urlStr = "http://localhost:9601/oauth/token"
//                +"?response_type=code"
//                +"&client_id=app-client-id" +
//                "&redirect_uri=https%3A%2F%2Fapp%2Fafter-auth" +
//                "&launch=xyz123" +
//                "&scope=launch+patient%2FObservation.read+patient%2FPatient.read+openid+fhirUser" +
//                "&state=98wrghuwuogerg97" +
//                "&aud=https://ehr/fhir";
//        URL url = new URL(urlStr);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//
//        FilterChainProxy a;
//
//        conn.setUseCaches(false);
//        conn.setDoInput(true);
////        conn.setDoOutput(hasoutbody);
//        conn.connect();
//
//        System.out.println(conn.getResponseCode());
//
//        TimeUnit.SECONDS.sleep(10);
//    }
//
//    @Override
//    public void redirectReceived() {
//        System.out.println("redirect received");
//    }
//}
