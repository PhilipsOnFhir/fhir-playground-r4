package org.github.philipsonfhir.fhirproxy.async.model;

import ca.uhn.fhir.context.FhirContext;
import org.github.philipsonfhir.fhirproxy.async.service.AsyncService;
import org.github.philipsonfhir.fhirproxy.async.service.BundleRetriever;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AsyncSession {
    private static FhirContext ourCtx = FhirContext.forR4();
    private final Date transActionTime;
//    private final FhirRequest fhirRequest;
//    private final FhirCall fhirCall;
    private final CompletableFuture<AsyncResult> completableFuture;
    private final FhirCall fhirCall;
    private final String callUrl;
    private AsyncResult asyncResult=null;

//    public AsyncSession(FhirCall fhirCall) {
//        this.transActionTime = new Date();
//        this.fhirCall = fhirCall;
//        completableFuture = CompletableFuture
//                .supplyAsync( () ->{
//                    AsyncService.logger.info( "Async processing start" );
//                    try {
//                        fhirCall.execute();
//                        IBaseResource iBaseResource = fhirCall.getResource();
//
//                        asyncResult = new AsyncResult();
//                        if ( iBaseResource instanceof Bundle) {
//                            Bundle bundle = (Bundle)iBaseResource;
//                            asyncResult.addBundle( bundle );
//                            BundleRetriever bundleRetriever = new BundleRetriever( fhirCall.getFhirServer(), bundle );
//                            asyncResult.addResources( bundleRetriever.retrieveAllResources() );
////                    processBundle( asyncResult, bundle, fhirRequest.getFhirServer() );
//                        } else if ( iBaseResource instanceof Resource) {
//                            asyncResult.addResource( (Resource)iBaseResource );
//                        }
//
//                    } catch (FHIRException e ) {
//                        e.printStackTrace();
//                    }
//
//                    AsyncService.logger.info( "Async processing done" );
//                    return asyncResult;
//                });
//    }

    public AsyncSession(String callUrl, FhirCall fhirCall) {
        this.callUrl = callUrl;
        this.transActionTime = new Date();
        this.fhirCall = fhirCall;

        completableFuture = CompletableFuture
                .supplyAsync( () ->{
                    AsyncService.logger.info( "Async processing start" );
                    try {
                        fhirCall.execute();
                        IBaseResource iBaseResource = fhirCall.getResource();

                        asyncResult = new AsyncResult();
                        if ( iBaseResource instanceof Bundle) {
                            Bundle bundle = (Bundle)iBaseResource;
                            asyncResult.addBundle( bundle );
                            BundleRetriever bundleRetriever = new BundleRetriever( fhirCall.getFhirServer(), bundle );
                            asyncResult.addResources( bundleRetriever.retrieveAllResources() );
//                    processBundle( asyncResult, bundle, fhirRequest.getFhirServer() );
                        } else if ( iBaseResource instanceof Resource) {
                            asyncResult.addResource( (Resource)iBaseResource );
                        }

                    } catch (FHIRException | FhirProxyException e ) {
                        e.printStackTrace();
                    }

                    AsyncService.logger.info( "Async processing done" );
                    return asyncResult;
                });
    }



    public boolean isReady() {
        return completableFuture.isDone();
    }

    public Date getTransActionTime() {
        return transActionTime;
    }


    public List<Resource> getResultResources(String resourceName) throws FhirProxyException {
        try {
            return completableFuture.get().resultTreeMap.get(resourceName).values()
                .stream().collect(Collectors.toList());
        } catch (InterruptedException|ExecutionException e) {
            throw new FhirProxyException(e);
        }
    }

    public List<String> getResultResourceNames() throws FhirProxyException {
        try {
            return completableFuture.get().resultTreeMap.keySet().stream()
                    .collect(Collectors.toList());
        } catch (InterruptedException|ExecutionException e) {
            throw new FhirProxyException(e);
        }
//        return  completableFuture.get().resultTreeMap.keySet().stream()
//            .collect(Collectors.toList());
    }


    public int getResourceCount(String resourceName) throws FhirProxyException {
        try {
            return completableFuture.get().resultTreeMap.get( resourceName ).size();
        } catch (InterruptedException|ExecutionException e) {
            throw new FhirProxyException(e);
        }
//        return processor.getResult().resultTreeMap.get( resourceName ).size();
    }


    public String getProcessDescription() {
        return this.fhirCall.getStatusDescription();
    }

    public String getRequestUrl() {
        return this.callUrl;
    }

    public Map<String, OperationOutcome> getErrorMap() {
        if ( this.fhirCall != null ) {
            return this.fhirCall.getErrors();
        }
        return new TreeMap<>();
    }


//    private class AsyncSessionProcessor implements Runnable {
//        private boolean isDone = false;
//
//        @Override
//        public synchronized void run() {
//            AsyncService.logger.info( "Async processing start" );
//            try {
//                IBaseResource iBaseResource = fhirRequest.getResource();
//
//                asyncResult = new AsyncResult();
//                if ( iBaseResource instanceof Bundle) {
//                    Bundle bundle = (Bundle)iBaseResource;
//                    asyncResult.addBundle( bundle );
//                    BundleRetriever bundleRetriever = new BundleRetriever( fhirRequest.getFhirServer(), bundle );
//                    asyncResult.addResources( bundleRetriever.retrieveAllResources() );
////                    processBundle( asyncResult, bundle, fhirRequest.getFhirServer() );
//                } else if ( iBaseResource instanceof Resource) {
//                    asyncResult.addResource( (Resource)iBaseResource );
//                }
//
//            } catch (FHIRException e ) {
//                e.printStackTrace();
//            }
//
//            AsyncService.logger.info( "Async processing done" );
//            isDone = true;
//        }
//
//
//
//
//        public AsyncResult getResult() {
//            return asyncResult;
//        }
//
//    }


}

