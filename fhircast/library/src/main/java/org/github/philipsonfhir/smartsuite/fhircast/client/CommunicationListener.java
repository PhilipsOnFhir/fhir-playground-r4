package org.github.philipsonfhir.smartsuite.fhircast.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.ContextEvent;
import org.github.philipsonfhir.smartsuite.fhircast.server.domain.FhirCastWebHookSubscribtionResponse;
import org.github.philipsonfhir.smartsuite.fhircast.server.service.util.Hmac;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;

public class CommunicationListener implements Runnable {
    private final int port;
    private final FhirCastWebhookClient webhookClient;
    private String secret = "undefined";
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String headerData;
    private String contentData;
    private boolean continueListening = true;
    private boolean refuseEvents=false;

    CommunicationListener(int port, FhirCastWebhookClient webhookClient){
        this.webhookClient = webhookClient;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            logger.info( "listener started" );

            ServerSocket server = new ServerSocket( port );

            while ( continueListening ) {
                Socket client = server.accept();
                logger.info( "callback received" );
                this.headerData ="";
                this.contentData = "";


                try {
                    InputStream raw = client.getInputStream(); // ARM
                    headerData = getHeaderToArray( raw );

                    if ( headerData.startsWith( "GET" )){
                        // verification
                        String str = headerData.substring( headerData.indexOf( "?" ) + 1, headerData.indexOf( "HTTP" ) );
                        String[] params = str.split( "&" );
                        Map<String, String> queryParams = new TreeMap<>();
                        for ( String param : params ) {
                            String[] parts = param.split( "=" );
                            queryParams.put( parts[0], parts[1] );
                        }
                        String mode = queryParams.get("hub.mode");
                        switch (mode) {
                            case "subscribe":
                            case "unsubscribe":
                                FhirCastWebHookSubscribtionResponse fhirCastWebHookSubscribtionResponse = new FhirCastWebHookSubscribtionResponse();
                                fhirCastWebHookSubscribtionResponse.setHub_mode(queryParams.get("hub.mode"));
                                fhirCastWebHookSubscribtionResponse.setHub_topic(queryParams.get("hub.topic"));
                                fhirCastWebHookSubscribtionResponse.setHub_events(queryParams.get("hub.events"));
                                fhirCastWebHookSubscribtionResponse.setHub_challenge(queryParams.get("hub.challenge"));
                                fhirCastWebHookSubscribtionResponse.setHub_leaseSeconds(queryParams.get("hub.lease_seconds"));
                                String httpResponse;
                                try {
                                    this.webhookClient.verifySubscription(fhirCastWebHookSubscribtionResponse);
                                    httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + queryParams.get("hub.challenge");
                                } catch (FhirCastException e) {
                                    httpResponse = "HTTP/1.1 500 Something went wrong\r\n\r\n" + e.getMessage();
                                }

                                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                                client.close();
                                break;
                            case "denied":
                                logger.info("Denial received for events "+queryParams.get("hub.events"));
                                httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                                client.close();
                                this.webhookClient.denialEventReceived( queryParams.get("hub.events") );
                        }
                    } else if ( headerData.startsWith( "POST" )){
                        // new event
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            ContextEvent fhirCastWorkflowEvent = objectMapper.readValue(contentData, ContextEvent.class);
                            String xhubSignatureheaderName = "X-Hub-Signature:";
                            Optional<String> signature = headerData.lines()
                                    .filter(line -> line.startsWith(xhubSignatureheaderName))
                                    .map(line -> line.substring(xhubSignatureheaderName.length()))
                                    .findFirst();
                            String hmac = Hmac.calculateHMAC(secret, contentData);
                            if (signature.isPresent() && !signature.get().strip().equals(hmac)) {
                                logger.warning("HMAC check of event failed");
                            }
                            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                            if ( this.refuseEvents ){
                                httpResponse = "HTTP/1.1 500 FAILED\r\n\r\n";
                            } else {
                                this.webhookClient.newEvent(fhirCastWorkflowEvent);
                            }
                            client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                            client.close();
                        } catch (FhirCastException e) {
                            String msg = "HMAC verification failed";
                            logger.warning("HMAC verification failed");
                            String httpResponse = "HTTP/1.1 500 "+msg+"\r\n\r\n";
                            client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                            client.close();
                        }
                    } else{
                        logger.warning( "unknown event "+headerData );
                    }
                } catch ( MalformedURLException ex ) {
                    logger.warning(client.getLocalAddress() + " is not a parseable URL" );
                    client.close();
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private String getHeaderToArray(InputStream inputStream) {

        StringBuilder headerTempData = new StringBuilder();
        String contentTmpData = "";

        // chain the InputStream to a Reader
        Reader reader = new InputStreamReader(inputStream);
        try {
            int c;
            while ((c = reader.read()) != -1) {
//                System.out.print((char) c);
                headerTempData.append((char) c);

                if (headerTempData.toString().contains("\r\n\r\n"))
                    break;
            }

            String[] headerfields = headerTempData.toString().replace( "\r\n","\n" ).split( "\n" );
            for( String headerField: headerfields ) {
                String cl = "Content-Length:";
                if ( headerField.startsWith( cl ) ) {
                    String lengthStr = headerField.substring( cl.length() ).trim();
                    long lenght = Long.parseLong( lengthStr );
                    for ( int i = 0;i<lenght;i++){
                        c = reader.read();
                        contentTmpData = contentTmpData+ (char) c;
//                        System.out.println( i+" : "+contentTmpData);
                    }
                    contentData = contentTmpData;
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        headerData = headerTempData.toString();

        return headerTempData.toString();
    }

    public void setSecret(String hub_secret) {
        this.secret = hub_secret;
    }

    void refuseEvents() {
        this.refuseEvents= true;
    }
}
