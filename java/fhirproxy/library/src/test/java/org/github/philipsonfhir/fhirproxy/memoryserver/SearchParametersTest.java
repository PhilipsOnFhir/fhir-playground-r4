package org.github.philipsonfhir.fhirproxy.memoryserver;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ActivityDefinition;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class SearchParametersTest {

    @Test
    public void readData(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("memoryserver/search-parameters.json");
        FhirContext ourCtx= FhirContext.forR4();

        IBaseResource baseResource = ourCtx.newJsonParser().parseResource(is);

        Bundle bundle = (Bundle) baseResource;
        assertNotNull( bundle );
    }

    @Test
    public void createResource(){
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.getSearchParam(ResourceType.ServiceRequest, "based-on" );

        ActivityDefinition activityDefinition = new ActivityDefinition();
        activityDefinition.setId("someID");
        activityDefinition.setUrl("http://canonical.url");

        assertTrue( searchParameters.checkResource( activityDefinition, "url", activityDefinition.getUrl() ) );
    }
}
