package org.github.philipsonfhir.worklist.cdshooks.service;

import org.apache.maven.shared.invoker.*;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class CdsHooksServiceTest {

    @BeforeClass
    public  static void startCdsServers() throws MavenInvocationException {
        startServer("http://localhost:9641/cdshooks/cds-services", new File("../cdshooks-test-services/pom.xml"),"spring-boot:run");
        startServer( "http://localhost:9404/hapi-fhir-jpaserver/fhir/metadata",new File("../hapi-r4/pom.xml"),"jetty:run"  );

    }

    public  static void startServer(String url, File pomFile, String mvnCmd) throws MavenInvocationException {
        if ( !serverRunning(url)){
//            CompletableFuture completableFuture = new CompletableFuture<>()
//                    .runAsync(() -> {
//                        InvocationResult result = null;
//                        try {
//                            InvocationRequest request = new DefaultInvocationRequest();
//                            request.setPomFile(pomFile);
//                            request.setGoals(Collections.singletonList(mvnCmd));
//
//                            Invoker invoker = new DefaultInvoker();
//
//                            result = invoker.execute(request);
//
//                        } catch (MavenInvocationException ex) {
//                            ex.printStackTrace();
//                        }
//
//                        if (result.getExitCode() != 0) {
//                            throw new IllegalStateException("Build failed.");
//                        }
//                    });
//            int count = 0;
//            int maxWait = 300;//sec
//            boolean started = false;
//            while( count<maxWait && !started ){
//                System.out.println(String.format("waiting %d of %d sec ",count, maxWait));
//                started = serverRunning( url );
//                count++;
//            }
//            if ( !started ){
//                throw new IllegalStateException(String.format("url: %s not started within %d seconds.", url, maxWait ));
//            }
            fail("server not started");
        }
        System.out.println(String.format("Server %s running",url));
    }

    private static boolean serverRunning(String url){
        boolean result = false;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> a = restTemplate.getForEntity(url, String.class);
            result =true;
        } catch ( Throwable a){

        }
        return result;
    }

    @Test
    public void testCdsServiceCall() throws ExecutionException, InterruptedException {


        PrefetchHandler prefetchHandler = new PrefetchHandler("aa");
        String[] cdsServerUrls = new  String[1];
        cdsServerUrls[0] = "http://localhost:9641/cdshooks";

        Practitioner practitioner = (Practitioner) new Practitioner().setId("id1");
        Patient patient = (Patient) new Patient().setId("id2");
        LaunchContext launchContext  =new LaunchContext( practitioner, patient);

        CdsHooksService cdsHooksService = new CdsHooksService("aa", cdsServerUrls );
        CdsHooksCall cdsHooksCall = cdsHooksService.newCdsHooksCall(launchContext );
        Collection<CdsServerCall> cdsServerCallList = cdsHooksCall.getCdsServerCalls();

        assertEquals( 1, cdsServerCallList.size() );
        CdsServerCall cdsServerCall = cdsServerCallList.iterator().next();
        assertEquals( 1, cdsServerCall.getCdsServiceCalls().size() );
        CdsServiceCall cdsServiceCall = cdsServerCall.getCdsServiceCalls().iterator().next();
        assertEquals( 1, cdsServiceCall.getCards().getCards().size() );
    }


    @Test
    public void getCardTest() throws InterruptedException, ExecutionException {
        PrefetchHandler prefetchHandler = new PrefetchHandler("aa");
        String[] cdsServerUrls = new  String[2];
        cdsServerUrls[0] = "aa";
        cdsServerUrls[1] = "bb";
        Practitioner practitioner = (Practitioner) new Practitioner().setId("id1");
        Patient patient = (Patient) new Patient().setId("id2");
        LaunchContext launchContext  =new LaunchContext( practitioner, patient);

        CdsHooksService cdsHooksService = new CdsHooksService("aa", cdsServerUrls );
        CdsHooksCall cdsHooksCall = cdsHooksService.newCdsHooksCall(launchContext );
        Collection<CdsServerCall> cdsServerCallList = cdsHooksCall.getCdsServerCalls();

        assertEquals( 2, cdsServerCallList.size() );


    }


}
