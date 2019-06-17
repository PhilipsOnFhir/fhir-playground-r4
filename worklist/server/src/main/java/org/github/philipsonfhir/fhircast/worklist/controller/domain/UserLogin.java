package org.github.philipsonfhir.fhircast.worklist.controller.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserLogin {
    String userName;
    List<UserSession> userSessionList;
}
