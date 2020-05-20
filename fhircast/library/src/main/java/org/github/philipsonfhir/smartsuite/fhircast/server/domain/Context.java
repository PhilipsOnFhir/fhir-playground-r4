package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Resource;

@Getter
@Setter
@ToString
public class Context {
    String key;
    @JsonRawValue
    @JsonDeserialize(using = JsonRawValueDeserializer.class)
    String resource;

    static final private FhirContext ourCtx = FhirContext.forR4();
    public IBaseResource retrieveFhirResource(){
        return ourCtx.newJsonParser().parseResource( resource );
    }
    public void setResource(String resource ){
        this.resource = resource;
    }
    public void setResource(Resource resource ){
        this.resource = ourCtx.newJsonParser().encodeResourceToString( resource );
    }
}