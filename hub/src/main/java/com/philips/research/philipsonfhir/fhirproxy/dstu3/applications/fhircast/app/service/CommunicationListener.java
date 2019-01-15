package com.philips.research.philipsonfhir.fhirproxy.dstu3.applications.fhircast.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.applications.fhircast.app.FhirCastApplication;
import com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model.FhirCastWorkflowEvent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class CommunicationListener implements Runnable {
    private final int port;
    private final FhirCastClient client;
    Logger logger = Logger.getLogger(this.getClass().getName());
    private String headerData;
    private String contentData;
    private boolean continueListening = true;

    CommunicationListener(int port, FhirCastClient client ){
        this.client = client;
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
                        String parameters = headerData.substring( headerData.indexOf( "?" ) + 1, headerData.indexOf( "HTTP" ) );
                        String str = parameters;
                        String[] params = str.split( "&" );
                        Map<String, String> queryParams = new TreeMap<>();
                        for ( String param : params ) {
                            String[] parts = param.split( "=" );
                            queryParams.put( parts[0], parts[1] );
                        }

                        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + queryParams.get( "hub.challenge" );
                        client.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
                        client.close();
                    } else if ( headerData.startsWith( "POST" )){
                        // new event
                        ObjectMapper objectMapper = new ObjectMapper();
                        FhirCastWorkflowEvent fhirCastWorkflowEvent = objectMapper.readValue( contentData, FhirCastWorkflowEvent.class );
                        this.client.newEvent( fhirCastWorkflowEvent );
                        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                        client.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
                        client.close();
                    } else{
                        logger.warning( "unknown event "+headerData );
                    }

                } catch ( MalformedURLException ex ) {
                    System.err.println( client.getLocalAddress() + " is not a parseable URL" );

                } catch ( IOException ex ) {
                    System.err.println( ex.getMessage() );
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public String getHeaderToArray(InputStream inputStream) {

        String headerTempData = "";
        String contentTmpData = "";

        // chain the InputStream to a Reader
        Reader reader = new InputStreamReader(inputStream);
        try {
            int c;
            while ((c = reader.read()) != -1) {
                System.out.print((char) c);
                headerTempData += (char) c;

                if (headerTempData.contains("\r\n\r\n"))
                    break;
            }

            String[] headerfields = headerTempData.replace( "\r\n","\n" ).split( "\n" );
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
        headerData = headerTempData;

        return headerTempData;
    }
}
