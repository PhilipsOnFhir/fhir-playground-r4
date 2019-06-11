# fhir-cast
Java implementation of the FHIR Cast protocol

The repository contains 4 different elements:
* library: FHIRCast library containing the code for both server and clients.
* hub: back-end server - instantiates a server
* websub: console application that communicates with server using web-sub. 
* rest: Angular 6 rest based client
* websocket: Angular6 websocket client

## Compile
The project can be compiled by running mvn install in the root directory. This will compile and 
build the library, hub and websub modules.

## Execute
Running the code requires that mvn install has been run.
For each of the servers a shell script has been added.
* launchHub.sh - launches the fhir-cast hub server
* launchRestServer.sh - compiles and launches the angular server for the rest client
* launchWebsocketServer.sh - compiles and launches the angular server for the websocket client.
* openWebSubConsole.sh - opens a WebSub console application client.

## handling demo
### websub
The WebSub client implements a simple console interface:
* subscribe - subscribes to patient events
* unsubscribe - unsubscribes from patient events
* open <xxxx> - opens patient xxxx

### websocket
The websocket client implements a simple interface that is explained in the webpage.

## known issues
Currently the websocket events are not received by the other clients.




