package org.github.philipsonfhir.fhirproxy.common.util;


import org.github.philipsonfhir.fhirproxy.common.util.BasePath;
import org.github.philipsonfhir.fhirproxy.common.util.BasePathParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasePathParserTest {
    @Test
    public void basePathTest(){
        BasePathParser basePathParser = new BasePathParser( "/base/{topic}/{resourceType}/{resourceId}" );
        {
            BasePath basePath = basePathParser.parse("/base/21472819/Patient/349329402");
            assertEquals( "21472819", basePath.getPathElement( "topic") );
            assertEquals( "Patient", basePath.getPathElement( "resourceType") );
            assertEquals( "349329402", basePath.getPathElement( "resourceId") );
        }
        {
            BasePath basePath = basePathParser.parse("/base/21472819/Patient");
            assertEquals( "21472819", basePath.getPathElement( "topic") );
            assertEquals( "Patient", basePath.getPathElement( "resourceType") );
            assertEquals( null, basePath.getPathElement( "resourceId") );
        }

    }
}
