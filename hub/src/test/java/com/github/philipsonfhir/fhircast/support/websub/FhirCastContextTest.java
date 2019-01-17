package com.github.philipsonfhir.fhircast.support.websub;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FhirCastContextTest {

    @Test
    public void testSerializaDeserialize() throws IOException {
        FhirContext ourCtx = FhirContext.forDstu3();
        ObjectMapper objectMapper = new ObjectMapper(  );

        Patient patient = (Patient) new Patient()
            .addName( new HumanName().setFamily( "familyName" ) )
            .setActive( true )
            .setId( "testID ");

        FhirCastContext fhirCastContextAlt = new FhirCastContext();
        fhirCastContextAlt.setKey( "patient" );
        fhirCastContextAlt.setResource( ourCtx.newJsonParser().encodeResourceToString( patient ) );

        String json = objectMapper.writeValueAsString( fhirCastContextAlt );
        System.out.println(json);

        FhirCastContext deserialize = objectMapper.readValue( json, FhirCastContext.class );

        assertEquals( fhirCastContextAlt.getKey(), deserialize.getKey() );
        assertEquals( fhirCastContextAlt.resource, deserialize.getResource() );
    }

}