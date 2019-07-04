package org.github.philipsonfhir.fhircast.server.websub.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum FhircastChannelType {
    WEBSOCKET("websocket"),
    REST("rest");

    private String name;

    private static final Map<String,FhircastEventType> ENUM_MAP;

    FhircastChannelType (String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    // Build an immutable map of String name to enum pairs.
    // Any Map impl can be used.

    static {
        Map<String,FhircastEventType> map = new ConcurrentHashMap<String,FhircastEventType>();
        for (FhircastEventType instance : FhircastEventType.values()) {
            map.put(instance.getName(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static FhircastEventType get (String name) {
        return ENUM_MAP.get(name);
    }
    @Override
    public String toString() {
        return name;
    }
}

