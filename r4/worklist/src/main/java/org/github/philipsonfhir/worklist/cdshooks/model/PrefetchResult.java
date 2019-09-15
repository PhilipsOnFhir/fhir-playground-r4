package org.github.philipsonfhir.worklist.cdshooks.model;

        import com.fasterxml.jackson.annotation.JsonAnyGetter;
        import com.fasterxml.jackson.annotation.JsonAnySetter;
        import org.hl7.fhir.r4.model.Bundle;

        import java.util.HashMap;
        import java.util.Map;

public class PrefetchResult {
    private Map<String, Bundle> other = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Bundle> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, Bundle value) {
        other.put(name, value);
    }
}
