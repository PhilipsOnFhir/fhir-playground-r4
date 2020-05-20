package org.github.philipsonfhir.fhirproxy.testutil.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.github.philipsonfhir.fhirproxy.testutil.TestServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UploadResources {
    static TestServer testServer = new TestServer();

    public static void fromDirectory(String dir ) throws IOException {

        IParser xmlParser = FhirContext.forR4().newXmlParser();
        IParser jsonParser = FhirContext.forR4().newJsonParser();
        List<String> files = IOUtils.readLines(UploadResources.class.getClassLoader()
                .getResourceAsStream(dir), StandardCharsets.UTF_8);

        System.out.println("READ-start--------------------------------------");

        List<Resource> resourceList = new ArrayList<>();
        files.stream()
                .filter( filename-> filename.endsWith(".xml"))
                .forEach( filename -> {
                    System.out.println(filename);
                    IBaseResource res = null;
                    try {
                        res = xmlParser.parseResource(Resources.getResource(dir + filename).openStream());
                        resourceList.add((Resource) res);
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
                        res = jsonParser.parseResource(Resources.getResource(dir + filename).openStream());
                        resourceList.add((Resource) res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        files.stream()
                .filter( filename-> filename.endsWith(".cql"))
                .forEach( filename -> {
                    System.out.println(filename);

                    try {
                        InputStream stream = Resources.getResource(dir + filename).openStream();
                        IOUtils.toString( stream, "UTF-8");
//                        IBaseResource res = CqlLibrary ( cql );
//
//                        resourceList.add((Resource) res);
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
                testServer.putResource( resource );
            } catch ( Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("READ ALL--------------------------------------------------------------");
    }
}
