package com.github.philipsonfhir.fhircast.server.service;

import com.github.philipsonfhir.fhircast.server.websub.service.FhirCastClientData;
import com.github.philipsonfhir.fhircast.support.websub.FhircastEventType;
import org.junit.Test;
import static org.junit.Assert.*;

public class FhirCastWebsubClientDataTest {

    @Test
    public void testAddRemoveSubscribtions(){
        String clientCallbackUrl = "someCallbackUrl";

        FhirCastClientData fhirCastClientData = new FhirCastClientData( clientCallbackUrl, "secret" );

        assertEquals( clientCallbackUrl, fhirCastClientData.getClientCallbackUrl() );

        String subscribtions = FhircastEventType.OPEN_PATIENT_CHART + "," + FhircastEventType.CLOSE_PATIENT_CHART + "," + FhircastEventType.USER_LOGOUT;
        fhirCastClientData.subscribe( subscribtions );

        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_PATIENT_CHART) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.CLOSE_PATIENT_CHART) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.USER_LOGOUT) );
        assertFalse( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_IMAGING_STUDY ));

        fhirCastClientData.subscribe( FhircastEventType.OPEN_IMAGING_STUDY+"" );

        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_PATIENT_CHART) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.CLOSE_PATIENT_CHART) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.USER_LOGOUT) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_IMAGING_STUDY ));

        fhirCastClientData.unsubscribe( subscribtions );
        assertFalse( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_PATIENT_CHART) );
        assertFalse( fhirCastClientData.hasSubscription( FhircastEventType.CLOSE_PATIENT_CHART) );
        assertFalse( fhirCastClientData.hasSubscription( FhircastEventType.USER_LOGOUT) );
        assertTrue( fhirCastClientData.hasSubscription( FhircastEventType.OPEN_IMAGING_STUDY ));
    }

}
