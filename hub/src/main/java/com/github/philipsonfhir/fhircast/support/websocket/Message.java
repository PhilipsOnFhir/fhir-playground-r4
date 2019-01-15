package com.github.philipsonfhir.fhircast.support.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message {
    String message;
    String secret;
}
