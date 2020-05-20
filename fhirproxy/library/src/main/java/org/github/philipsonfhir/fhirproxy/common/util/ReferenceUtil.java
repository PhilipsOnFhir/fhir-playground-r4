package org.github.philipsonfhir.fhirproxy.common.util;


import lombok.Getter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.utilities.Utilities;

public class ReferenceUtil {
    public static ParsedReference parseReference(Reference reference ){
        return new ParsedReference(reference);
    }

    public static Reference getReference(Resource resource) {
        return new Reference( resource.getResourceType()+ "/" + resource.getIdElement().getIdPart() );
    }

    public static String getReference(IdType idt) {
        return idt.getResourceType()+"/"+idt.getIdPart();
    }

    @Getter
    public static class ParsedReference {
        private String url;
        private String resourceId = null;
        private String version = null;
        private String resourceType = null;

        public ParsedReference(Reference reference) {
            String ref = reference.getReference();
            if ( Utilities.isURL(ref) ){
                this.url = ref;
            } else {
                String[] parts = ref.split("/");
                switch (parts.length) {
                    case 3:
                        this.version = parts[2];
                    case 2:
                        this.resourceId = parts[1];
                    case 1:
                        this.resourceType = parts[0];
                }
            }
        }

        public boolean hasResourceId() {
            return resourceId!=null;
        }

        public boolean isUrl() {
            return url!=null;
        }
    }
}
