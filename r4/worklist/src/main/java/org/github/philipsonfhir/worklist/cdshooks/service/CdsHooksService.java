package org.github.philipsonfhir.worklist.cdshooks.service;


import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
public class CdsHooksService {
    private static Logger logger = Logger.getLogger("CdsHooksService");
    private final String fhirServerUrl;
    private final String[] cdsServerUrls;


    List<String> cdsHooksServers = new ArrayList<>();
    private Map<String, CdsHooksCall> cdsHooksCalls = new TreeMap<>();

    public CdsHooksService( @Value("${proxy.fhirserver.url}") String fhirServerUrl, @Value("${cdshooks.services}") String[] cdsServerUrls ){
        this.fhirServerUrl = fhirServerUrl;
        this.cdsServerUrls = cdsServerUrls;
    }

    public CdsHooksCall newCdsHooksCall(LaunchContext launchContext) {
        CompletableFuture<CdsHooksCall> completableFuture = new CompletableFuture<>();
        logger.info( "LNCH:"+launchContext.getLaunch()+" retrieve CDSHooks cards");
        Iterator<Map.Entry<String, CdsHooksCall>> it = cdsHooksCalls.entrySet().iterator();
        while ( it.hasNext() ){
            Map.Entry<String, CdsHooksCall> entry = it.next();
            if ( entry.getValue().offCurrentLaunch(launchContext.getLaunch())){
                it.remove();
            }
        }

        CdsHooksCall cdsHooksCall = new CdsHooksCall( launchContext, cdsServerUrls, fhirServerUrl );
//        completableFuture.
//        Thread thread = new Thread(cdsHooksCall);
//        thread.setName("process "+cdsHooksCall);
//        thread.start();
        cdsHooksCalls.put(cdsHooksCall.getId(), cdsHooksCall );
        return cdsHooksCall;
    }

    public Collection<CdsServiceCall> getCdsServerCalls(String serverCall) {
        List<CdsServerCall> cdsServerCalls = new ArrayList<>();

        return null;
    }

    public CdsHooksCall getCdsHooksCall(String cdsHooksCall) {
        return this.cdsHooksCalls.get(cdsHooksCall);
    }
}
