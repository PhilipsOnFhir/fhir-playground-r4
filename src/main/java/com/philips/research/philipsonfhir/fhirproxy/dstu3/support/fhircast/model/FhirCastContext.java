package com.philips.research.philipsonfhir.fhirproxy.dstu3.support.fhircast.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.dstu3.model.Resource;

@Getter
@Setter
@ToString
public class FhirCastContext {
    String key;
    String resource;
}
