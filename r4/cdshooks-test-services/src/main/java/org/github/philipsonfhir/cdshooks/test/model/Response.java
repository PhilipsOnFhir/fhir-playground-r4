package org.github.philipsonfhir.cdshooks.test.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    String hookId;
    long responseId;
    long lastChange;
    List<Card> cards;
}
