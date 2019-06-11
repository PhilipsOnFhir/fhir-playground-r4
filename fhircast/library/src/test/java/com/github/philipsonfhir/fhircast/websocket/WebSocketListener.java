//package com.github.philipsonfhir.fhircast.websocket;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CompletionStage;
//
//import jdk.incubator.http.WebSocket;
//
//public class WebSocketListener implements WebSocket.Listener {
//
//    @Override
//    public CompletionStage<?> onText(WebSocket webSocket,
//                                     CharSequence message, WebSocket.MessagePart part){
//
//        // Reuqesting next message
//        webSocket.request(1);
//
//        // Print the message when it's available
//        return CompletableFuture.completedFuture(message)
//            .thenAccept(System.out::println);
//    }
//}