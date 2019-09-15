package org.github.philipsonfhir.worklist.cdshooks.service;

import org.github.philipsonfhir.worklist.cdshooks.service.hooks.PatientViewHook;
import org.github.philipsonfhir.worklist.cdshooks.service.hooks.TriggeredHook;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class CdsHooksCall implements Runnable{
    private final LaunchContext launchContext;
    private final String[] cdsServerUrls;
    private final String fhirServerUrl;
    private Map<String, CdsServerCall > cdsServerCalls =new HashMap<>();
    private final String id;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    CompletableFuture<Void> future;

    public CdsHooksCall(LaunchContext launchContext, String[] cdsServerUrls, String fhirServerUrl ) {
        this.id  = "CH-CdsHooksCall-"+ System.currentTimeMillis();
        this.launchContext = launchContext;
        this.cdsServerUrls = cdsServerUrls;
        this.fhirServerUrl = fhirServerUrl;
        future = CompletableFuture.runAsync(this);
    }

    public Collection<CdsServerCall> getCdsServerCalls() throws ExecutionException, InterruptedException {
        future.get();
        return cdsServerCalls.values();
    }

    public CdsServerCall getCdsServerCall( String cdsServerCallId) throws ExecutionException, InterruptedException {
        future.get();
        return cdsServerCalls.get(cdsServerCallId);
    }

    public void run() {
        logger.info( launchContext.getLaunch()+"-"+id+"-retrieve CDSHooks cards");
        try {
            System.out.println(" ..");
            PrefetchHandler prefetchHandler = new PrefetchHandler(fhirServerUrl);
            System.out.println(" ..");
            List<TriggeredHook> triggeredHooks =  determineTriggeredHooks();

            for ( String cdsServerUrl: cdsServerUrls ){
                CdsServerCall cdsServerCall = new CdsServerCall( cdsServerUrl, launchContext, prefetchHandler, triggeredHooks );
                cdsServerCalls.put( cdsServerCall.getId(), cdsServerCall );
//                Thread thread = new Thread(cdsServerCall);
//                thread.setName("process "+cdsServerUrl);
//                thread.start();
            }
        }
        catch (Throwable a){
            a.printStackTrace();
        }
        logger.info( launchContext.getLaunch()+"-"+id+"-retrieve CDSHooks cards-finished");
    }

    private List<TriggeredHook> determineTriggeredHooks() {
        List<TriggeredHook> triggeredHooks = new ArrayList<>();
        triggeredHooks.add(PatientViewHook.getTriggeredHook( launchContext ));

        return triggeredHooks;
    }

    public boolean offCurrentLaunch(String launch) {
        return this.launchContext.getLaunch().equals(launch);
    }

    public String getId() {
        return this.id;
    }
}
