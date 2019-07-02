//package org.github.philipsonfhir.fhircast.server.websocket;
//
//import java.net.URI;
//import jdk.incubator.http.HttpClient;
//import jdk.incubator.http.WebSocket;
//
//public class WsTest {
//
//    public static void main(String s[]) throws InterruptedException {
//
//        HttpClient client = HttpClient.newHttpClient();
//        WebSocket builder = client.newWebSocketBuilder()
//            .buildAsync(
//                URI.create("ws://localhost:8080/JavaWebsocket/websocket"),
//                new WebSocketListener())
//            .join();
//
//        builder.sendText("codeNuclear", true);
//
//    }
//}
