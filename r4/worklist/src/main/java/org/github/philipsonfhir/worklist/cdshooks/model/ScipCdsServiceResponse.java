package org.github.philipsonfhir.worklist.cdshooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScipCdsServiceResponse extends CdsServiceResponse{
    private String tileUrl=null;
    private String fullViewUrl=null;
}
