# fhir-cast
Java implementation of the FHIR Cast protocol

The repository contains 4 different elements:
* hub-server: back-end server  
* hub-app: web-sub client application 
* res: Angular 6 rest based client
* websocket: Angular6 websocket client

## Composition
The repository is divided into three parts:
* hub : maven repository with hub-server and hub-app
  * hub-server application: hub\src\main\java\com\github\philipsonfhir\fhircast\server\FhirCastServerApplication.java
  * hub-app application: hub\src\main\java\com\github\philipsonfhir\fhircast\app\FhirCastWebsubClient.java
* rest
  * Angular 6 application that addresses the context endpoint
* websocket
  * Angular 6 application that implements the websocket interface
 
 



