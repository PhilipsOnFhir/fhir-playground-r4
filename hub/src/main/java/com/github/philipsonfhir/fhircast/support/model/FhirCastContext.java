package com.github.philipsonfhir.fhircast.support.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FhirCastContext {
    String key;
    String resource;
}
