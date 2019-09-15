package org.github.philipsonfhir.worklist.cdshooks.service;

import org.github.philipsonfhir.worklist.cdshooks.model.*;
import org.github.philipsonfhir.worklist.cdshooks.service.hooks.TriggeredHook;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class CdsServiceCall implements Runnable{

    private final CdsService cdsService;
    private final PrefetchHandler prefetchHandler;
    private final TriggeredHook triggeredHook;
    private final CompletableFuture<Void> future;
    Logger logger = Logger.getLogger(this.getClass().getName());
    private final String id;
    private final LaunchContext launchContext;
    private final RestTemplate restTemplate;
    private boolean ready = false;
    private Cards cards = new Cards();

    public CdsServiceCall(LaunchContext launchContext, CdsService cdsService, PrefetchHandler prefetchHandler, TriggeredHook triggeredHook ){
        this.id  = "CH-ServiceCall-"+ System.currentTimeMillis();
        this.launchContext = launchContext;
        this.cdsService = cdsService;
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        this.restTemplate = restTemplateBuilder.build();
        this.prefetchHandler = prefetchHandler;
        this.triggeredHook = triggeredHook;
        future = CompletableFuture.runAsync(this);
    }

    @Override
    public synchronized void run() {
        logger.info( String.format("LNCH:%s-%s run",launchContext.getLaunch(), id));
        CdsServiceRequest cdsServiceRequest = new CdsServiceRequest();
        cdsServiceRequest.setHook( triggeredHook.getHook() );
        cdsServiceRequest.setHookInstance( triggeredHook.getHookInstance() );
        //TODO
//        cdsServiceRequest.setFhirServer( );
//        cdsServiceRequest.setFhirAuthorization();

        cdsServiceRequest.setContext( triggeredHook.getContext() );

        PrefetchResult prefetchResult = new PrefetchResult();
        Prefetch prefetch  = cdsService.getPrefetch();
        if ( prefetch!=null ) {
            for (Map.Entry<String, String> entry : cdsService.getPrefetch().any().entrySet()) {
                prefetchResult.set(entry.getKey(), prefetchHandler.getPrefetch(triggeredHook.getContext(), entry.getValue()));
            }
        }
        cdsServiceRequest.setPrefetch(prefetchResult);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        try {
            cards = restTemplate.postForObject(cdsService.getUrl(), cdsServiceRequest, Cards.class);
        }
        catch( Exception e ){
            e.printStackTrace();
        }
        logger.info( String.format("LNCH:%s-%s finished",launchContext.getLaunch(), id));
    }

    public String getId() {
        return id;
    }

    public synchronized Cards getCards() throws ExecutionException, InterruptedException {
        future.get();
        return cards;
    }

}
