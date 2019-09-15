package org.github.philipsonfhir.worklist.cdshooks.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;

import java.util.HashMap;
import java.util.Map;

public class PrefetchHandler {
    private final IGenericClient client;
    FhirContext ourCtx = FhirContext.forR4();
    Map<String, Bundle> cache = new HashMap<>();

    PrefetchHandler( String fhirUrl ){
        client = ourCtx.newRestfulGenericClient(fhirUrl);
    }

    public Bundle getPrefetch(Map<String,String> context, String prefetch) {
        if ( cache.containsKey(prefetch)){
            return cache.get(prefetch);
        } else {
            String fhirQuery = prefetch;
            for (Map.Entry<String, String> entry : context.entrySet()) {
                String indicator = "{{context." + entry.getKey() + "}}";

                if (fhirQuery.contains(indicator)) {
                    fhirQuery.replace(indicator, entry.getValue());
                }
            }
            Bundle bundle = client.search().byUrl(fhirQuery).returnBundle(Bundle.class).execute();

            return bundle;
        }
    }
}
