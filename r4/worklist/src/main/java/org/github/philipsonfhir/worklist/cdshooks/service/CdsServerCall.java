package org.github.philipsonfhir.worklist.cdshooks.service;

import org.github.philipsonfhir.worklist.cdshooks.model.CdsService;
import org.github.philipsonfhir.worklist.cdshooks.model.CdsServices;
import org.github.philipsonfhir.worklist.cdshooks.service.hooks.TriggeredHook;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public  class CdsServerCall implements Runnable {
    private final PrefetchHandler prefetchHandler;
    private final List<TriggeredHook> triggeredHooks;
    private final CompletableFuture<Void> future;
    Logger logger = Logger.getLogger(this.getClass().getName());
    private final String id;
    private final LaunchContext launchContext;
    private final String cdsServerUrl;
    private final RestTemplate restTemplate;
    private Map<String, CdsServiceCall> cdsServiceCallMap = new HashMap<>();

    public CdsServerCall(String cdsServerUrl, LaunchContext launchContext, PrefetchHandler prefetchHandler, List<TriggeredHook> triggeredHooks) {
        this.id  = "CH-ServerCall-"+ System.currentTimeMillis();
        this.cdsServerUrl = cdsServerUrl;
        this.launchContext = launchContext;
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        this.restTemplate = restTemplateBuilder.build();
        this.prefetchHandler = prefetchHandler;
        this.triggeredHooks = triggeredHooks;
        future = CompletableFuture.runAsync(this);
    }

    public String getId() {
        return id;
    }

    @Override
    public synchronized void run() {
        logger.info( String.format("LNCH:%s-%s run",launchContext.getLaunch(), id));

        CdsServices cdsServices = getCdsHooksCdsServices( cdsServerUrl );

        for ( TriggeredHook triggeredHook: triggeredHooks ){
            for (CdsService cdsService: cdsServices.getServices()) {
                if (cdsService.getHook().equals(triggeredHook.getHook())){
                    cdsService.setUrl( (cdsServerUrl.endsWith("/")?cdsServerUrl:cdsServerUrl+"/" )+"cds-services/"+cdsService.getId());
                    CdsServiceCall cdsServiceCall = new CdsServiceCall(launchContext, cdsService, prefetchHandler, triggeredHook);
                    this.cdsServiceCallMap.put(cdsServiceCall.getId(), cdsServiceCall);
                }
            }

        }
        logger.info( String.format("LNCH:%s-%s finished",launchContext.getLaunch(), id));

    }

    public synchronized Collection<CdsServiceCall> getCdsServiceCalls() throws ExecutionException, InterruptedException {
        future.get();
        return this.cdsServiceCallMap.values();
    }

    private CdsServices getCdsHooksCdsServices(String url)  {
        try {
            String serverUrl = (url.endsWith("/cds-services")?url:url+"/cds-services" );
            CdsServices cdsServices = this.restTemplate.getForObject(serverUrl, CdsServices.class);
            return (cdsServices == null ? new CdsServices() : cdsServices);
        }catch( Exception e ){
            logger.info("error receiving service list from url.");
            e.printStackTrace();
            return new CdsServices();
        }
    }

    public synchronized CdsServiceCall getCdsServiceCall(String servicecallId ) throws ExecutionException, InterruptedException {
        future.get();
        return this.cdsServiceCallMap.get(servicecallId );
    }


}
