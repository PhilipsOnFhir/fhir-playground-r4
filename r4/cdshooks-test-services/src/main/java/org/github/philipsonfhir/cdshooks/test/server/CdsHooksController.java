package org.github.philipsonfhir.cdshooks.test.server;

import org.github.philipsonfhir.cdshooks.test.model.CdsServiceCallBody;
import org.github.philipsonfhir.cdshooks.test.model.CdsServiceResponse;
import org.github.philipsonfhir.cdshooks.test.model.CdsServices;
import org.github.philipsonfhir.cdshooks.test.service.CdsHooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class CdsHooksController {
    @Autowired
    CdsHooksService cdsHooksService;

    public CdsHooksController() throws IOException {
    }

    @GetMapping("/cdshooks/cds-services")
    @ResponseBody
    public CdsServices getServices() {
        CdsServices cdsServices = new CdsServices();
        cdsServices.setServices( cdsHooksService.getCdsServices());
        return  cdsServices;
    }

    @PostMapping( "/cdshooks/cds-services/{serviceId}")
    public CdsServiceResponse callCdsService(@PathVariable String serviceId, @RequestBody CdsServiceCallBody body ) throws UnknownCdsHooksServiceException {
        return cdsHooksService.callCdsService( serviceId, body) ;
    }

}
