# FHIR-Playground-R4

## Scope
This project holds a reference/test implementation of a FHIRcast server. This code is used in HL7 FHIR Connecthaton to test the FHIRcast specification
(https://fhircast.hl7.org/specification/Feb2020Ballot/index.html).

The project contains a FHIRcast service, a Topic service and a commandline websocket and
webhook client.

The Topic service is used to create and remove FHIRcast topics. The FHIRcast service holds
the FHIRcast end-points as specified in the specification.

## Compilation
Check-out the code.
Compile the code by running `mvn install`.

## Execution
The examples/local directory contains a project that will run a local instance of the
project. The FHIR data is retrieved from the public HAPI FHIR server ( http://hapi.fhir.org/baseR4).
The server can be run by calling   
 * `java.exe -jar target/local-fhircast-4.1-SNAPSHOT.jar org.github.philipsonfhir.smartsuite.examples.local.MyPlainFhirCastServer`  

from the directory:
 * examples/local/local-fhircast


