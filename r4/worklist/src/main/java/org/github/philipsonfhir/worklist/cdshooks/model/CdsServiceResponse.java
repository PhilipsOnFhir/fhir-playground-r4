package org.github.philipsonfhir.worklist.cdshooks.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CdsServiceResponse {
    List<Card> cards;
}
