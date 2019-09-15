package org.github.philipsonfhir.cdshooks.test.service;

import org.github.philipsonfhir.cdshooks.test.model.*;
import org.github.philipsonfhir.cdshooks.test.server.UnknownCdsHooksServiceException;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CdsHooksService {
    private Map<String, CdsServiceImplementationInterface> cdsServices = new TreeMap<>();

    public List<CdsService> getCdsServices() {
        List<CdsService> result = cdsServices.values().stream()
                .map( cdsServiceImplementation -> cdsServiceImplementation.getCdsService() )
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    public CdsServiceResponse callCdsService(String serviceId, CdsServiceCallBody body) throws UnknownCdsHooksServiceException {
        CdsServiceImplementationInterface cdsServiceImplementationInterface =
                cdsServices.get(serviceId);
        if( cdsServiceImplementationInterface == null ){
            throw new UnknownCdsHooksServiceException();
        }

        CdsServiceResponse cdsServiceResponse
                = cdsServiceImplementationInterface.getCallCdsService( body );

        return cdsServiceResponse;
    }

    public void addCdsService(CdsServiceImplementationInterface cdsService ) {
        cdsServices.put( cdsService.getCdsService().getId(), cdsService );
    }
}
