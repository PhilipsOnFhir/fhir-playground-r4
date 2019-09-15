package org.github.philipsonfhir.worklist.cdshooks;

import org.github.philipsonfhir.worklist.cdshooks.model.Cards;
import org.github.philipsonfhir.worklist.cdshooks.service.CdsHooksCall;
import org.github.philipsonfhir.worklist.cdshooks.service.CdsHooksService;
import org.github.philipsonfhir.worklist.cdshooks.service.CdsServerCall;
import org.github.philipsonfhir.worklist.cdshooks.service.CdsServiceCall;
import org.github.philipsonfhir.worklist.fhircast.server.Prefix;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchContext;
import org.github.philipsonfhir.worklist.fhircast.server.launch.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class CDSHooksController {
    private final LaunchService launchService;
    private final CdsHooksService cdsHooksService;
    @Value("${cdshooks.services}") String[] fhirServerUrls;

    @Autowired
    public CDSHooksController(LaunchService launchService, CdsHooksService cdsHooksService ){
        this.launchService = launchService;
        this.cdsHooksService = cdsHooksService;
    }

    @GetMapping( Prefix.FHIRCAST_LAUNCH+"/{launchId}/cdshooks" )
    List<String> getCdsUpdate( @PathVariable String launchId ) throws ExecutionException, InterruptedException {
        LaunchContext launchContext = this.launchService.getLaunchContext( launchId );
        CdsHooksCall cdsHooksCall = cdsHooksService.newCdsHooksCall( launchContext );
        return cdsHooksCall.getCdsServerCalls().stream().map(cdsServerCall -> cdsServerCall.getId()).collect(Collectors.toList());
    }

    @GetMapping( Prefix.FHIRCAST_LAUNCH+"/{launchId}/cdshooks/{cdsHooksCall}" )
    List<String> getServiceCalls( @PathVariable String launchId, @PathVariable String cdsHooksCall ) throws ExecutionException, InterruptedException {
        LaunchContext launchContext = this.launchService.getLaunchContext( launchId );

        CdsHooksCall cdsHooksCall1 = cdsHooksService.getCdsHooksCall(cdsHooksCall);
        return cdsHooksCall1.getCdsServerCalls().stream().map(cdsServerCall -> cdsServerCall.getId()).collect(Collectors.toList());
    }

    @GetMapping( Prefix.FHIRCAST_LAUNCH+"/{launchId}/cdshooks/{cdsHooksCall}/{servercall}" )
    List<String> getServiceCalls(@PathVariable String launchId, @PathVariable String cdsHooksCall, @PathVariable String servercall ) throws ExecutionException, InterruptedException {
        LaunchContext launchContext = this.launchService.getLaunchContext( launchId );

        CdsHooksCall cdsHooksCal1 = cdsHooksService.getCdsHooksCall(cdsHooksCall);
        CdsServerCall cdsServerCall = cdsHooksCal1.getCdsServerCall( servercall );

        return cdsServerCall.getCdsServiceCalls().stream().map(cdsServiceCall -> cdsServiceCall.getId()).collect(Collectors.toList());
    }

    @GetMapping( Prefix.FHIRCAST_LAUNCH+"/{launchId}/cdshooks/{cdsHooksCall}/{serverCall}/{serviceCall}" )
    Cards getServiceCallCards(@PathVariable String launchId, @PathVariable String cdsHooksCall, @PathVariable String serverCall, @PathVariable String serviceCall ) throws ExecutionException, InterruptedException {
        LaunchContext launchContext = this.launchService.getLaunchContext( launchId );

        CdsHooksCall cdsHooksCal1 = cdsHooksService.getCdsHooksCall(cdsHooksCall);
        CdsServerCall cdsServerCall = cdsHooksCal1.getCdsServerCall( serverCall );
        CdsServiceCall cdsServiceCall = cdsServerCall.getCdsServiceCall(serviceCall);
        return cdsServiceCall.getCards();
    }
}
