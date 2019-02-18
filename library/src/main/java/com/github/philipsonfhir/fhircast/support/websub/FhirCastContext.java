package com.github.philipsonfhir.fhircast.support.websub;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FhirCastContext {
    String key;
    @JsonRawValue
    @JsonDeserialize(using = JsonRawValueDeserializer.class)
    String resource;
}
