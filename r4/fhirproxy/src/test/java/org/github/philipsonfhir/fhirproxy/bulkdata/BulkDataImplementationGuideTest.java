package org.github.philipsonfhir.fhirproxy.bulkdata;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirRequest;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.github.philipsonfhir.fhirproxy.testutil.TestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class BulkDataImplementationGuideTest {

    private String fhirServerUrl = "http://localhost:9404/hapi-fhir-jpaserver/fhir/";
    private FhirContext ourCtx = FhirContext.forR4();
    private boolean resourcesCreated = false;
    private FhirServer fhirServer;


    @Test
    public void basicTest(){
        BulkDataImplementationGuide bulkDataImplementationGuide = new BulkDataImplementationGuide();

        assertEquals( 4, bulkDataImplementationGuide.getOperations().size() );
        assertTrue( bulkDataImplementationGuide.getOperations().stream()
                .anyMatch( fhirOperation -> fhirOperation instanceof ExportFhirOperation ));
        assertTrue( bulkDataImplementationGuide.getOperations().stream()
                .anyMatch( fhirOperation -> fhirOperation instanceof GroupInstanceExportFhirOperation));
        assertTrue( bulkDataImplementationGuide.getOperations().stream()
                .anyMatch( fhirOperation -> fhirOperation instanceof PatientExportFhirOperation ));
        assertTrue( bulkDataImplementationGuide.getOperations().stream()
                .anyMatch( fhirOperation -> fhirOperation instanceof PatientInstanceExportFhirOperation ));

        CapabilityStatement capabilityStatement = new CapabilityStatement();
        bulkDataImplementationGuide.updateCapabilityStatement(capabilityStatement);
        assertEquals(1, capabilityStatement.getInstantiates().size() );
        assertEquals("http://www.hl7.org/fhir/bulk-data/CapabilityStatement-bulk-data.html",
                capabilityStatement.getInstantiates().get(0).getValue() );
    }

    @Test
    public void testExport() throws Exception {
        createResources();
        ExportFhirOperation exportFhirOperation = new ExportFhirOperation();
        FhirServer fhirServer = new FhirServer( fhirServerUrl );
        FhirRequest fhirRequest = new FhirRequest("GET", "http://localhost","http://localhost/$export","since=2015-02-07T13:28:17.239+02:00");
        FhirCall fhirCall = exportFhirOperation.createFhirCall(fhirServer, fhirRequest);
        fhirCall.execute();
        assertTrue( fhirCall.getResource() instanceof Bundle );
        Bundle bundle = (Bundle) fhirCall.getResource();
        System.out.println( "Number of resources "+bundle.getEntry().size() );
    }

    @Test
    public void callPatientExport() throws FhirProxyException {
        PatientExportFhirOperation exportFhirOperation = new PatientExportFhirOperation();
        FhirServer fhirServer = new FhirServer( fhirServerUrl );
        FhirRequest fhirRequest = new FhirRequest("GET", "http://localhost","http://localhost/Patient/$export","");
        FhirCall fhirCall = exportFhirOperation.createFhirCall(fhirServer, fhirRequest);
        fhirCall.execute();
        assertTrue( fhirCall.getResource() instanceof Bundle );
        Bundle bundle = (Bundle) fhirCall.getResource();
        System.out.println( "Number of resources "+bundle.getEntry().size() );
    }

    @Test
    public void callPatientInstanceExport() throws Exception {
        createResources();
        PatientInstanceExportFhirOperation exportFhirOperation = new PatientInstanceExportFhirOperation();
        FhirServer fhirServer = new FhirServer( fhirServerUrl );
        FhirRequest fhirRequest = new FhirRequest("GET", "http://localhost","http://localhost/Patient/BD-Pa1/$export","");
        FhirCall fhirCall = exportFhirOperation.createFhirCall(fhirServer, fhirRequest);
        fhirCall.execute();
        assertTrue( fhirCall.getResource() instanceof Bundle );
        Bundle bundle = (Bundle) fhirCall.getResource();
        System.out.println( "Number of resources "+bundle.getEntry().size() );
    }

    @Test
    public void callGroupInstanceExport() throws Exception {
        createResources();
        GroupInstanceExportFhirOperation exportFhirOperation = new GroupInstanceExportFhirOperation();
        FhirServer fhirServer = new FhirServer( fhirServerUrl );
        FhirRequest fhirRequest = new FhirRequest("GET", "http://localhost","http://localhost/Group/Group-BD-Grp1/$export","");
        FhirCall fhirCall = exportFhirOperation.createFhirCall(fhirServer, fhirRequest);
        fhirCall.execute();
        assertTrue( fhirCall.getResource() instanceof Bundle );
        Bundle bundle = (Bundle) fhirCall.getResource();
        System.out.println( "Number of resources "+bundle.getEntry().size() );
    }

    @Test
    public void testServerRunning(){
        assertNotNull(  ourCtx.newRestfulGenericClient(fhirServerUrl).capabilities().ofType(CapabilityStatement.class).execute() );
    }

    private void createResources() throws Exception {
        if ( !resourcesCreated ) {
            fhirServer = new FhirServer( fhirServerUrl );
            List<Resource> resources = new ArrayList<>();
            {
                Practitioner practitioner = TestDataBuilder.createPractitioner("BD-Pr1", "1949-01-23" );
                Patient patient = TestDataBuilder.createPatient( "BD-Pa1", "1986-01-15", practitioner );
                Encounter encounter = TestDataBuilder.createEncounter( "BD-Enc-1", patient );
                Procedure procedure =  TestDataBuilder.createProcedure( "BD-Pr1", patient, practitioner, "2018-01-23" );
                resources.add( practitioner );
                resources.add( patient );
                resources.add( encounter );
                resources.add( procedure );
            }
            {
                Practitioner practitioner = TestDataBuilder.createPractitioner("BD-Pr2", "1949-02-23" );
                Patient patient = TestDataBuilder.createPatient( "BD-Pa2", "1986-02-15", practitioner );
                Encounter encounter = TestDataBuilder.createEncounter( "BD-Enc-2", patient );
                Procedure procedure =  TestDataBuilder.createProcedure( "BD-Pr2", patient, practitioner, "2018-01-23" );
                resources.add( practitioner );
                resources.add( patient );
                resources.add( encounter );
                resources.add( procedure );
            }
            {
                Practitioner practitioner = TestDataBuilder.createPractitioner("BD-Pr3", "1949-02-23" );
                Patient patient1 = TestDataBuilder.createPatient( "BD-Pa3", "1986-03-15", practitioner );
                Patient patient2 = TestDataBuilder.createPatient( "BD-Pa4", "1986-04-15", practitioner );
                Patient patient3 = TestDataBuilder.createPatient( "BD-Pa5", "1986-05-15", practitioner );
                Encounter encounter = TestDataBuilder.createEncounter( "BD-Enc-3", patient1 );
                Procedure procedure =  TestDataBuilder.createProcedure( "BD-Pr3", patient1, practitioner, "2018-01-23" );

                Group group = (Group) new Group()
                        .setActive( true )
                        .setType( Group.GroupType.PERSON )
                        .setName( "My bulk data test group" )
                        .addMember( new Group.GroupMemberComponent().setEntity( new Reference( patient1 ) ) )
                        .addMember( new Group.GroupMemberComponent().setEntity( new Reference( patient2 ) ) )
                        .addMember( new Group.GroupMemberComponent().setEntity( new Reference( patient3 ) ) )
                        .addMember( new Group.GroupMemberComponent().setEntity( new Reference( practitioner ) ) )
                        .setId( "Group-BD-Grp1" );
                resources.add( practitioner );
                resources.add( patient1 );
                resources.add( patient2 );
                resources.add( patient3 );
                resources.add( encounter );
                resources.add( procedure );
                resources.add( group );
            }

            for ( IBaseResource baseResource : resources ) {
                fhirServer.doPut( baseResource );
            }

            resourcesCreated = true;
//            allPatientData = retrieveAllPatientEverything( null );
//            assertNotNull( allPatientData );
//            assertTrue( allPatientData.getEntry().size() > 1 );
//
//            allPatientDataPa2 = retrieveAllPatientEverything( patientId );
//            assertNotNull( allPatientDataPa2 );
//            assertTrue( allPatientDataPa2.getEntry().size() > 1 );
        }
    }
}
