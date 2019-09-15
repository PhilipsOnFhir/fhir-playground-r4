package org.github.philipsonfhir.cdshooks.test.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.HashMap;
import java.util.Map;

public class PrefetchResult {
    private Map<String, String> other = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, String value) {
        other.put(name, value);
    }
}
