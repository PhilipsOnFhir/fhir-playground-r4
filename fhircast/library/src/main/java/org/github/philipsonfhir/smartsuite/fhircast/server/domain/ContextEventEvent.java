package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class ContextEventEvent {
        @JsonProperty("hub.topic")      String hub_topic;
        @JsonProperty("hub.event")
        String hub_event;
        List<Context> context = new ArrayList<>();

        public Patient retrievePatientFromContext( ) throws FhirCastException {
                Optional<Context> opt = context
                        .stream()
                        .filter( fhirCastContext -> fhirCastContext.getKey().equals( "patient" ) )
                        .findFirst();
                if ( opt.isPresent() ){
                        return (Patient) opt.get().retrieveFhirResource();
                }
                throw new FhirCastException( "Context should contain patient" );
        }

        public ImagingStudy retrieveImagingStudyFromContext() throws FhirCastException {
                Optional<Context> opt = context
                        .stream()
                        .filter( fhirCastContext -> fhirCastContext.getKey().equals( "study" ) )
                        .findFirst();
                if ( opt.isPresent() ){
                        return (ImagingStudy) opt.get().retrieveFhirResource();
                }
                throw new FhirCastException( "Context should contain patient" );
        }
}
