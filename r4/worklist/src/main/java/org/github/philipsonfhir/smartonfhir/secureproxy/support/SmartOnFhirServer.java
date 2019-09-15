package org.github.philipsonfhir.smartonfhir.secureproxy.support;

import org.github.philipsonfhir.smartonfhir.basic.controller.service.FhirServer;
import org.hl7.fhir.r4.model.*;

public class SmartOnFhirServer extends FhirServer {

    private final String tokenEndpoint;
    private final String authEndpoint;


    public SmartOnFhirServer(String fhirServerUrl, String authEndpoint, String tokenEndpoint ) {
        super(fhirServerUrl);
        this.tokenEndpoint = tokenEndpoint;
        this.authEndpoint = authEndpoint;
    }



    @Override
    public CapabilityStatement getCapabilityStatement() {
        CapabilityStatement capabilityStatement = super.getCapabilityStatement();

        CapabilityStatement.CapabilityStatementRestSecurityComponent capabilityStatementRestSecurityComponent
                = new CapabilityStatement.CapabilityStatementRestSecurityComponent();
        capabilityStatementRestSecurityComponent.
                addService( new CodeableConcept().addCoding(
                        new Coding().
                                setSystem("http://hl7.org/fhir/restful-security-service")
                                .setCode("SMART-on-FHIR")
                        )
                ).addExtension()
                    .setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                    .addExtension( new Extension()
                            .setUrl("token").setValue(new UriType(tokenEndpoint))
                    )
                    .addExtension( new Extension()
                            .setUrl("authorize").setValue(new UriType(authEndpoint))
                    )
        ;

        capabilityStatement.getRest().forEach( rest ->{
            rest.setSecurity(capabilityStatementRestSecurityComponent);
        });

        return capabilityStatement;
    }
}
