package org.github.philipsonfhir.cdshooks.test.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SessionStatus {
    String sessionId;
    long lastChange;

    List<Long> responseIds = new ArrayList<>();

}
