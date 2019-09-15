package org.github.philipsonfhir.cdshooks.test.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CdsServiceRequest {
    private String hook;
    private String hookInstance;
    private String fhirServer;
    private String fhirAuthorization;
    private String user;
//    private Map<String, String> context;
    private PrefetchResult prefetch;

    private Map<String, String> context = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> any() {
        return context;
    }

    @JsonAnySetter
    public void set(String name, String value) {
        context.put(name, value);
    }
}
