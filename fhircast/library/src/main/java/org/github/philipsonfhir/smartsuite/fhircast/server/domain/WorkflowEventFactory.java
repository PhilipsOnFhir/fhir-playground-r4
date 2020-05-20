package org.github.philipsonfhir.smartsuite.fhircast.server.domain;

import org.github.philipsonfhir.smartsuite.fhircast.server.service.SendEventResult;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WorkflowEventFactory {
    // ===========================================================================
    public static ContextEvent createOpenEvent(Patient patient) {
        return createEvent( patient, FhircastEventType.PATIENT_OPEN );
    }

    public static ContextEvent createCloseEvent(Patient patient) {
        return createEvent( patient, FhircastEventType.PATIENT_CLOSE );
    }

    private static ContextEvent createEvent(Patient patient, FhircastEventType eventType) {
        List<Context> contextList = new ArrayList<>();

        Context context = new Context();
        context.setKey( "patient" );
        context.setResource( new Patient()
                .setIdentifier(patient.getIdentifier())
                .setId( patient.getId() )
        );
        contextList.add( context );

        return createEvent( contextList, eventType );
    }

    // ===========================================================================
    public static ContextEvent createOpenEvent(ImagingStudy study ) {
        return createEvent( study, null, FhircastEventType.IMAGINGSTUDY_OPEN );
    }

    public static ContextEvent createOpenEvent(ImagingStudy study, Patient patient ) {
        return createEvent( study, patient, FhircastEventType.IMAGINGSTUDY_OPEN );
    }

    public static ContextEvent createCloseEvent(ImagingStudy study ) {
        return createEvent( study, null, FhircastEventType.IMAGINGSTUDY_CLOSE );
    }

    public static ContextEvent createCloseEvent(ImagingStudy study, Patient patient ) {
        return createEvent( study, patient, FhircastEventType.IMAGINGSTUDY_CLOSE );
    }

    private static ContextEvent createEvent(ImagingStudy imagingStudy, Patient patient, FhircastEventType eventType) {
        List<Context> contextList = new ArrayList<>();

        Context imageStudContext = new Context();
        imageStudContext.setKey( "study" );
        imageStudContext.setResource( new ImagingStudy()
                .setSubject(imagingStudy.getSubject())
                .setIdentifier( imagingStudy.getIdentifier() )
                .setId( imagingStudy.getId())
        );
        contextList.add( new Context());

        if ( patient!=null ) {
            Context patientContext = new Context();
            patientContext.setKey("patient");
            patientContext.setResource(new Patient()
                    .setIdentifier(patient.getIdentifier())
                    .setId(patient.getId())
            );
            contextList.add( patientContext );
        }

        return createEvent( contextList, eventType );

    }

    // ===========================================================================

    private static ContextEvent createEvent(List<Context> contextList, FhircastEventType eventType) {
        ContextEventEvent fhirCastWorkflowEventEvent = new ContextEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( eventType.getName() );
        fhirCastWorkflowEventEvent.setContext(contextList);

        return getContextEvent(fhirCastWorkflowEventEvent);
    }

    public static ContextEvent createLogoutEvent(String topic) {
        ContextEventEvent fhirCastWorkflowEventEvent = new ContextEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( "userlogout" );
        fhirCastWorkflowEventEvent.setHub_topic( topic );
        fhirCastWorkflowEventEvent.setContext(new ArrayList<>());

        return getContextEvent(fhirCastWorkflowEventEvent);
    }

    private static ContextEvent getContextEvent(ContextEventEvent fhirCastWorkflowEventEvent) {
        ContextEvent fhirCastWorkflowEvent = new ContextEvent();
        fhirCastWorkflowEvent.setId(UUID.randomUUID().toString());
        fhirCastWorkflowEvent.setTimestamp(new DateTimeType(new Date()).asStringValue());
        fhirCastWorkflowEvent.setEvent(fhirCastWorkflowEventEvent);
        return fhirCastWorkflowEvent;
    }

    public static ContextEvent createSyncErrorEvent(ContextEvent contextEvent,  SendEventResult sendEventResult) {
        ContextEvent fhirCastWorkflowEvent = new ContextEvent();
        fhirCastWorkflowEvent.setId( contextEvent.id );
        fhirCastWorkflowEvent.setTimestamp(new DateTimeType(new Date()).asStringValue());

        ContextEventEvent fhirCastWorkflowEventEvent = new ContextEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( "syncerror" );
        fhirCastWorkflowEventEvent.setHub_topic( contextEvent.getEvent().getHub_topic() );

        Context context = new Context();
        context.setKey("processing");
        sendEventResult.getMessages().forEach( message ->
            context.setResource( new OperationOutcome()
                    .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                            .setSeverity( OperationOutcome.IssueSeverity.WARNING )
                            .setCode( OperationOutcome.IssueType.PROCESSING )
                            .setDiagnostics(message)
                        )
                    )
        );
        fhirCastWorkflowEventEvent.getContext().add(context);

        fhirCastWorkflowEvent.setEvent(fhirCastWorkflowEventEvent);

        return fhirCastWorkflowEvent;
    }

    public static ContextEvent createSyncErrorEvent(String topicId, String id, String message ) {
        ContextEvent fhirCastWorkflowEvent = new ContextEvent();
        fhirCastWorkflowEvent.setId( id );
        fhirCastWorkflowEvent.setTimestamp(new DateTimeType(new Date()).asStringValue());

        ContextEventEvent fhirCastWorkflowEventEvent = new ContextEventEvent();
        fhirCastWorkflowEventEvent.setHub_event( "syncerror" );
        fhirCastWorkflowEventEvent.setHub_topic( topicId );

        Context context = new Context();
        context.setKey("processing");
            context.setResource( new OperationOutcome()
                    .addIssue( new OperationOutcome.OperationOutcomeIssueComponent()
                            .setSeverity( OperationOutcome.IssueSeverity.WARNING )
                            .setCode( OperationOutcome.IssueType.PROCESSING )
                            .setDiagnostics(message)
                    )
            );
        fhirCastWorkflowEventEvent.getContext().add(context);

        fhirCastWorkflowEvent.setEvent(fhirCastWorkflowEventEvent);

        return fhirCastWorkflowEvent;
    }
}
