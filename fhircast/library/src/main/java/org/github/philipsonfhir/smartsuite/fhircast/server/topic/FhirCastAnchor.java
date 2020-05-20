package org.github.philipsonfhir.smartsuite.fhircast.server.topic;

import org.hl7.fhir.r4.model.Resource;

public class FhirCastAnchor {
    private Resource resource;

    public FhirCastAnchor(Resource resource) {
        this.resource = resource;
    }

    public static String getAnchorKey(Resource resource) {
        return resource.getResourceType().name()+"/"+resource.getId();
    }

    public String getKey() {
        return getAnchorKey( resource );
    }

    public Resource getAnchorResource() {
        return resource;
    }
}
