package org.github.philipsonfhir.fhirproxy.common.util;

import com.google.common.io.Resources;
import org.fhir.ucum.Decimal;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Duration;
import org.hl7.fhir.r4.model.Quantity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class QuantityUtil {
    private static UcumEssenceService ucumEssenceService = null;
    private static UcumEssenceService getUncumEssenceService() throws UcumException {
        try {
            ucumEssenceService = new UcumEssenceService( Resources.getResource("ucum-essence.xml").openStream() );
        } catch (IOException e) {
            throw new UcumException("Cannot load UCUM definition file");
        }
        return ucumEssenceService;
    }

    public static DateTimeType substract(DateTimeType baseTime, Duration duration) {
//        UcumEssenceService ucumEssenceService = new UcumEssenceService()

        Calendar cal = baseTime.toCalendar();
        TimeZone tz = cal.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        LocalDateTime baseLdt = LocalDateTime.ofInstant(cal.toInstant(), zid);
        LocalDateTime resLdt = baseLdt;


        int value = duration.getValue().intValue();
        switch( duration.getCode() ){
            case "a":
                resLdt=baseLdt.minusYears(value);
                break;
            case "mo":
                resLdt=baseLdt.minusMonths(value);
                break;
            case "d":
                resLdt=baseLdt.minusDays(value);
                break;
            case "h":
                resLdt=baseLdt.minusHours(value);
                break;
            case "min":
                resLdt=baseLdt.minusMinutes(value);
                break;
            case "s":
                resLdt=baseLdt.minusSeconds(value);
                break;
        }

        DateTimeType result =new DateTimeType( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(  resLdt ));
        result.setTimeZone( tz );

        return result;
    }

    public static Quantity convert( Quantity quantity, Coding unit) throws UcumException {
        Decimal dec = new Decimal( quantity.getValue().toEngineeringString() );
        Decimal convDec = ucumEssenceService.convert( dec, quantity.getCode(), unit.getCode() );
        Quantity result = new Quantity()
                .setSystem( unit.getSystem())
                .setCode( unit.getCode() )
                .setUnit( (unit.hasDisplay()? unit.getDisplay():unit.getCode()))
                ;
        return result;
    }
}
