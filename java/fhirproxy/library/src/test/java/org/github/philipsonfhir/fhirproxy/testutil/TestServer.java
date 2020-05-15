package org.github.philipsonfhir.fhirproxy.testutil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.FhirServer;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.testutil.support.CqlLibraryBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestServer {
//    private static String defaultUrl = "http://localhost:9404/hapi-fhir-jpaserver/fhir/";
    private static String defaultUrl = "http://localhost:9404/fhir";
    private final IGenericClient ourClient;
    private final IFhirServer fhirServer;
    private FhirContext ourCtx = FhirContext.forR4();

    public TestServer(  ){
        this(defaultUrl);
    }

    public TestServer( String serverUrl ){
        this.fhirServer = new FhirServer(serverUrl);
        // Set how long to try and establish the initial TCP connection (in ms)
        ourCtx.getRestfulClientFactory().setConnectTimeout(30 * 1000);

        // Set how long to block for individual read/write operations (in ms)
        ourCtx.getRestfulClientFactory().setSocketTimeout(30 * 1000);

        ourClient = ourCtx.newRestfulGenericClient(serverUrl);

        // required classes
        org.slf4j.impl.StaticLoggerBinder a;
    }

    public void storeResource(Resource resource) throws IOException {
        IParser jsonParser = ourCtx.newJsonParser();
        jsonParser.setPrettyPrint(true);
        File exampleDir = new File("example");
        if ( !exampleDir.exists() ){
            exampleDir.mkdir();
        }

        File rscFile = new File("example/"+resource.getIdElement().getIdPart()+".json");
        FileWriter writer = new FileWriter( rscFile );
        writer.write( jsonParser.encodeResourceToString(resource ));
        writer.close();
    }

    public void putResource(Resource resource) {
        putResource( resource, resource.getId() );
    }

    public void putResource(Resource resource, String id) {
        if (resource instanceof Bundle) {
            ourClient.transaction().withBundle((Bundle) resource).execute();
        }
        else {
            ourClient.update().resource(resource).withId(id).execute();
        }
    }
    public void uploadAllIn( String dir ) throws IOException {
//        String dir = "clinical-guidelines-r4/anc/resources/";
        String finalDir = ( dir.endsWith("/")?dir:dir+"/");
        IParser xmlParser = FhirContext.forR4().newXmlParser();
        IParser jsonParser = FhirContext.forR4().newJsonParser();
        List<String> files = IOUtils.readLines(this.getClass().getClassLoader()
                .getResourceAsStream(dir), StandardCharsets.UTF_8);

        System.out.println("READ-start--------------------------------------");

        List<Resource> resourceList = new ArrayList<>();

        files.stream()
                .filter( filename-> filename.endsWith(".xml"))
                .forEach( filename -> {
                    System.out.println(filename);
                    IBaseResource res = null;
                    try {
                        res = xmlParser.parseResource(Resources.getResource(finalDir + filename).openStream());
                        resourceList.add((Resource) res);
//                        if ( res instanceof Bundle && ((Bundle)res).getType()== Bundle.BundleType.TRANSACTION ){
//                            testServer.performTransaction((Bundle) res);
//                        }
//                        testServer.putStoreResource((Resource) res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        files.stream()
                .filter( filename-> filename.endsWith(".json"))
                .forEach( filename -> {
                    System.out.println(filename);
                    IBaseResource res = null;
                    try {
                        res = jsonParser.parseResource(Resources.getResource(finalDir + filename).openStream());
                        resourceList.add((Resource) res);
//                        if ( res instanceof Bundle && ((Bundle)res).getType()== Bundle.BundleType.TRANSACTION ){
//                            testServer.performTransaction((Bundle) res);
//                        }
//                        testServer.putStoreResource((Resource) res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        files.stream()
                .filter( filename-> filename.endsWith(".cql"))
                .forEach( filename -> {
                    System.out.println(filename);

                    try {
                        InputStream stream = Resources.getResource(finalDir + filename).openStream();
                        String cql = IOUtils.toString( stream, "UTF-8");
                        IBaseResource res = new CqlLibraryBuilder( cql ).build();

                        resourceList.add((Resource) res);
//                        if ( res instanceof Bundle && ((Bundle)res).getType()== Bundle.BundleType.TRANSACTION ){
//                            testServer.performTransaction((Bundle) res);
//                        }
//                        testServer.putStoreResource((Resource) res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        resourceList.forEach( resource -> {
            try {
                System.out.println(resource.getResourceType()+ " - " + resource.getId());
                putResource( resource );
            } catch ( Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("READ ALL--------------------------------------------------------------");
    }

    private CarePlan applyPlanDefinition(String planDefinitionId, String subjectId) {
        Parameters outParams = ourClient
                .operation()
                .onInstance(new IdDt("PlanDefinition", planDefinitionId ))
                .named("$apply")
                .withParameters( new Parameters()
                        .addParameter( new Parameters.ParametersParameterComponent()
                                .setName("patient")
                                .setValue(new StringType("Patient/"+subjectId))
                        )
                )
                .useHttpGet()
                .execute();

        List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

        Assert.assertTrue(!response.isEmpty());

        Resource resource = response.get(0).getResource();

        Assert.assertTrue(resource instanceof CarePlan);

        CarePlan carePlan = (CarePlan) resource;

        assertNotNull( carePlan );
        assertTrue( carePlan.hasActivity() );

        return carePlan;
    }



    public Resource transform(String structuredMapId, QuestionnaireResponse questionnaireResponse) {
        String xml = ourCtx.newXmlParser().encodeResourceToString(questionnaireResponse);
        Parameters outParams = ourClient
                .operation()
                .onInstance(new IdDt("StructureMap", structuredMapId ))
                .named("$transform")
//                .withParameters( new Parameters()
//                        .addParameter( new Parameters.ParametersParameterComponent()
//                                .setName("content")
//                                .setValue( new StringType("dummy"))
////                                .setValue(new StringType(xml) )
//                        )
//                )
                .withNoParameters(Parameters.class)
                .execute();

            List<Parameters.ParametersParameterComponent> response = outParams.getParameter();

            Assert.assertTrue(!response.isEmpty());

            Resource resource = response.get(0).getResource();


            return resource;
        }

    public void putStoreResource(Resource resource) throws IOException {
        storeResource(resource);
        putResource(resource);
    }

    public void putStoreResource(List<Resource> allRequiredResources) throws IOException {
        for (Resource resource : allRequiredResources) {
            putStoreResource(resource);
        }
    }

    public IFhirServer getFhirServer() {
        return this.fhirServer;
    }

    public void performTransaction( Bundle res) {
        this.ourClient.transaction().withBundle(res);
    }

}
