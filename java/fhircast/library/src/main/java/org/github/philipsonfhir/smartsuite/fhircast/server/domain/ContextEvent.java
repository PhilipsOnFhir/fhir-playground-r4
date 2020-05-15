package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@ToString
public class ContextEvent {
    String timestamp;
    String id;
    ContextEventEvent event;
    SubscriptionUpdate subscription;

    public ContextEvent(MultiValueMap<String, String> multipleFormVars) {
        timestamp = multipleFormVars.getFirst("timestamp");
        id = multipleFormVars.getFirst("id");
//        event = new ContextEventEvent( multipleFormVars.get)
    }

    public ContextEvent() {
    }

    public void initialize(){
        this.timestamp = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
        this.id =  "FC-alive-"+System.currentTimeMillis();
    }

    public Date obtainDateTimeStamp(){
        OffsetDateTime odt = OffsetDateTime.parse( "2010-01-01T12:00:00+01:00" );
        Instant instant = odt.toInstant();  // Instant is always in UTC.
        return java.util.Date.from( instant );
    }
}
