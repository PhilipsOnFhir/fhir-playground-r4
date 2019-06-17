package org.github.philipsonfhir.fhircast.user;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhircast.user.domain.UserSession;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserSessionController {

    @Value("${fhir.server.url}")
    String fhirServerUrl;

//    @GetMapping("user")
    @RequestMapping(value = "/user", method = RequestMethod.GET, headers="Accept=application/json")
    public String currentUser(
            @RequestHeader(value = "Accept", defaultValue = "application/fhir+json") String accept,
            Principal principal ) {
        FhirContext ourCtx = FhirContext.forR4();
        IGenericClient genericClient = ourCtx.newRestfulGenericClient( fhirServerUrl );
        Bundle bundle = (Bundle) genericClient.search().byUrl("Practitioner?identifier="+principal.getName()).execute();
        Practitioner practitioner = ( bundle.isEmpty()? null : (Practitioner) bundle.getEntryFirstRep().getResource());

        return ourCtx.newJsonParser().encodeResourceToString(practitioner);
    }

    @GetMapping("session")
    public List<String> currentUserSessions(Principal principal ){
        List<UserSession> userSessionList = new ArrayList<>();
        // this.sessionService.getSessions(  principal.getName );
        return userSessionList.stream()
                .map( userSession -> userSession.getSessionId())
                .collect(Collectors.toList());
    }
}
