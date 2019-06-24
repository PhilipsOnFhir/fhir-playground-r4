package com.github.philipsonfhir.fhircast.server.websub.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum FhircastEventType {
    OPEN_PATIENT_CHART("open-patient-chart"),
    SWITCH_PATIENT_CHART("switch-patient-chart"),
    CLOSE_PATIENT_CHART("close-patient-chart"),
    OPEN_IMAGING_STUDY("open-imaging-study"),
    SWITCH_IMAGING_STUDY("switch-imaging-study"),
    CLOSE_IMAGING_STUDY("close-imaging-study"),
    USER_LOGOUT("user-logout"),
    USER_HIBERNATE("user-hibernate");

    private String name;

    private static final Map<String,FhircastEventType> ENUM_MAP;

    FhircastEventType (String name) {
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
