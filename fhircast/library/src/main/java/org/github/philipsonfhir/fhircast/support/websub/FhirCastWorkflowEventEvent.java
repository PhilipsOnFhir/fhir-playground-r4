package org.github.philipsonfhir.fhircast.support.websub;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.github.philipsonfhir.fhircast.support.FhirCastException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class FhirCastWorkflowEventEvent {
    @JsonProperty("hub.topic")      String hub_topic;
    @JsonProperty("hub.event")     FhircastEventType hub_event;
    List<FhirCastContext> context = new ArrayList<>();

    public Patient retrievePatientFromContext( ) throws FhirCastException {
        Optional<FhirCastContext> opt = context
            .stream()
            .filter( fhirCastContext -> fhirCastContext.getKey().equals( "patient" ) )
            .findFirst();
        if ( opt.isPresent() ){
            return (Patient) opt.get().retrieveFhirResource();
        }
        throw new FhirCastException( "Context should contain patient" );
    }
}
