package org.github.philipsonfhir.cdshooks.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Action {
    public enum ActionType {create, update, delete}

    ActionType type;
    String description;
    String resource;
//    public void setResource( Resource fhirResource ){
//        resource = (FhirContext.forDstu3().newJsonParser()).encodeResourceToString(fhirResource);
//    }
}
