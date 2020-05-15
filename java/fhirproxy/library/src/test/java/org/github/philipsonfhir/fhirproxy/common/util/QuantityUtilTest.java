package org.github.philipsonfhir.fhirproxy.common.util;

import org.fhir.ucum.Decimal;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Duration;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class QuantityUtilTest {
    UcumEssenceService ucumService;
    @Before
    public void loadService() throws UcumException {
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream("ucum-essence.xml");
        InputStream bis = new BufferedInputStream(inputStream);

        ucumService = new UcumEssenceService(bis);
//
    }
    @Test
    public void testBasic() throws IOException, UcumException {
        Decimal result = ucumService.convert(new Decimal("15"), "/min", "/h");
        System.out.println(result.asDecimal()); // Prints 911
    }

    @Test
    public void testDateTimeOperations() throws IOException {
//        DateTimeType base = DateTimeType.now();
        DateTimeType start = new DateTimeType("2020-03-30T10:27:04+02:00");
        DateTimeType base = new DateTimeType("2020-07-30T10:27:04+02:00");
//        base.setMonth( base.getMonth()+4);
        DateTimeType res = QuantityUtil.substract(base, (Duration) new Duration().setValue(1).setCode("mo"));
        assertNotNull(res);

        assertEquals( base.getHour(), res.getHour());
        assertEquals( base.getMonth().intValue(), res.getMonth()+1);
        assertEquals( base.getTzHour(), res.getTzHour());
//        base.setMonth(1);
//        res = QuantityUtil.substract(base, (Duration) new Duration().setValue(3).setCode("mo"));
//        assertNotNull(res);
//
//        assertEquals( base.getHour(), res.getHour());
//        assertEquals( 11, res.getMonth()+1);

        // convert to milliseconds
//        Decimal milliSec = ucumService.convert(new Decimal("1"), "/ms", "/mo");
//
//        ucumService.convert(new Decimal("1"), "/d", "/mo").asScientific();
//        assertEquals( 30.5, ucumService.convert(new Decimal("1"), "/d", "/mo").asInteger());

    }
}
