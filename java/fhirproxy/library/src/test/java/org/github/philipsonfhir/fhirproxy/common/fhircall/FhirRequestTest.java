package org.github.philipsonfhir.fhirproxy.common.fhircall;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FhirRequestTest {

    @Test
    public void testParseUrl() throws FhirProxyException {
        try{
            String base = "http://localhost:9345/fhir";
            FhirRequest fhirRequest = new FhirRequest(base, "http://localhost:9444/fhir/Patient", "name=Broccoli");
            fail();
        }catch (FhirProxyException e){}

        String base = "http://localhost:9345/fhir";
        {
            FhirRequest fhirRequest = new FhirRequest(base, base+ "/$export", "");
            assertEquals("$export", fhirRequest.getOperationName());
            assertEquals(null, fhirRequest.getResourceType());
            assertEquals(null, fhirRequest.getResourceId());
            assertEquals(0, fhirRequest.getQueryMap().size());
        }
        {
            FhirRequest fhirRequest = new FhirRequest(base, base+ "/Patient", "name=Broccoli");
            assertEquals("Patient", fhirRequest.getResourceType());
            assertEquals(null, fhirRequest.getResourceId());
            assertEquals("Broccoli", fhirRequest.getQueryMap().get("name"));
        }
        {
            FhirRequest fhirRequest = new FhirRequest(base + "/", base+"/PlanDefinition/3240/$apply", "name=Broccoli&a=b");
            assertEquals("PlanDefinition", fhirRequest.getResourceType());
            assertEquals("3240", fhirRequest.getResourceId());
            assertEquals( null, fhirRequest.getParam());
            assertEquals( "$apply", fhirRequest.getOperationName());
            assertEquals("Broccoli", fhirRequest.getQueryMap().get("name"));
            assertEquals("b", fhirRequest.getQueryMap().get("a"));
        }
        {
            FhirRequest fhirRequest = new FhirRequest(base, base+ "/Patient", "name=Broccoli");
            assertEquals("Patient", fhirRequest.getResourceType());
            assertEquals(null, fhirRequest.getResourceId());
            assertEquals("Broccoli", fhirRequest.getQueryMap().get("name"));
        }
        {
            FhirRequest fhirRequest = new FhirRequest(base + "/", base+"/Patient/3240", "name=Broccoli");
            assertEquals("Patient", fhirRequest.getResourceType());
            assertEquals("3240", fhirRequest.getResourceId());
            assertEquals("Broccoli", fhirRequest.getQueryMap().get("name"));
        }
    }
}
