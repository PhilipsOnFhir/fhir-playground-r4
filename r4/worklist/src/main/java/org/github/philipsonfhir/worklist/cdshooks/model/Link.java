package org.github.philipsonfhir.worklist.cdshooks.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {
    String label;
    String url;
    String type;
    String appContext;
}
