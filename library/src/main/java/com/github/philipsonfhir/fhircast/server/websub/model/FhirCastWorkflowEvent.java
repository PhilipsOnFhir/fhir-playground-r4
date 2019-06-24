package com.github.philipsonfhir.fhircast.server.websub.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@ToString
public class FhirCastWorkflowEvent {
    String timestamp;
    String id;
    FhirCastWorkflowEventEvent event;
    FhirCastSessionSubscribe subscription;

    public void initialize(){

        this.timestamp = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
        this.id =  "FC"+System.currentTimeMillis();
    }

}
