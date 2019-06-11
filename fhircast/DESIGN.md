# FHIR-CAST WEBSOCKET update
This implementation implemnents some proposed changes of the FHIR cast 
Split the specification into(http://fhircast.org/) specification.

## Specification format
We propose to split the specifiaction into three layers:
 1. channel protocol
 2. subscribe/unsubscribe and event type
 3. Event definitions
 
## Channel protocols
Channel protocols are the the different protocols that fhir-cast can use to connect to clients.   
This implementation of fhir-cast implements three different
 1. WebSub (exisiting spec)
 2. Websockets (see below)
 3. pull based context (exisiting spec)

In order to separate the channels on different end-points:
 * WebSub: \<baseurl>/\<topic>/websub  
 * WebSocket: \<baseurl>/websocket
 * Context: \<baseurl>/\<topic>/context

GET on \<baseurl>/\<topic> will give you a list of the supported protocols.

# Websocket
The websocket part uses STOMP 1.2.   
Some functionality is not required for FHIR CAST. Clients and server SHALL implement the required functionality and SHOULD implement the other.

## STOMP subset
We will be using STOMP 1.2 over WebSocket. Not all functionality from STOM is required.
The table below indicates the functionality that MAY be unsupported. It is RECOMMENDED that 
the client and server implement the other functionality required by STOMP although it will 
not be used by this specification.

The following table shows the support requirements for the different STOMP messages.
| Message | Source | Status | Description |  
| --- | --- | --- | --- |   
| CONNECT | Client	REQUIRED	Connect to websocket
| SEND | Client	REQUIRED	
| SUBSCRIBE | Client | REQUIRED | Subscribe to channel
| UNSUBSCRIBE | Client  | REQUIRED | Unsubscribe from channel
| BEGIN | Client | OPTIONAL | Start transaction
| COMMIT | Client | OPTIONAL | Commit transaction
| ABORT | Client | OPTIONAL | Abort transaction
| ACK | Client | REQUIRED | Acknowledge
| NACK | Client | REQUIRED | Not Acknowledge
| DISCONNECT | Client | REQUIRED | Disconnect websocket
| MESSAGE | Server | REQUIRED | Send message to client
| RECEIPT | Server | REQUIRED | Indicate receipt content
| ERROR	Server | REQUIRED | Indicate error occurred

## Destinations
Clients send messages to destinations, a destination is a directory like name on the server.  
All FhirCast destinations are targetting the hube will be prefixed with /hub/.  
All FhirCast destinations are targetting the spplications will be prefixed with /app/.  

### Client-->Server 
Client-->Server  communication SHALL use the following pattern:
	/hub/fhircast/\<topic>/
where
	\<clientid>: is a unique id of the client, e.g. the client-id used in OAUth

### Server ->Client
Server ->Client SHALL use the following destinations:
	/app/fhircast/<topic>/<event>
where
	\<clientid>: 	is a unique id of the client, e.g. the client-id used in Oauth
	\<event>	is the event name/id the client sends a message to

Implementations can distinguish on OAuth role, adding client-id makes it easier to filter who receives what.

## Use cases
### Client Subscribe/ Unsubscribe
All clients can register for events on the websocket endpoint.

**SUBSCRIBE /hub/fhircast/\<topic>/\<event>**  
	subscribe app for event \<event> on topic \<topic>
		
**UNSUBSCRIBE /hub/fhircast/\<topic>/\<event>**   
	Unsubscribe for the event on the topic.
	
**Deny subscription**   
	TBD
	
### Sending events
#### Server sends event   
**MESSAGE /app/fhircast/topic>/<event> Event**  
The Event is formatted as a WebSub event. E.g. 
THe event is formated as is displayed below:
```json
{
event": {
    "hub.topic": "https://hub.example.com/7jaa86kgdudewiaq0wtu",
    "hub.event": "close-patient-chart",
    "context": [
      {
        "key": "patient",
        "resource": {
          "resourceType": "Patient",
          "id": "798E4MyMcpCWHab9",
          "identifier": [
            {
              "system": "urn:oid:1.2.840.114350",
              "value": "1345687"
            },
            {
              "system": "urn:oid:1.2.840.114350.1.13.861.1.7.5.737384.27000",
              "value": "7539"
            }
          ]
        }
      }
    ]
}
```

#### Clients updates context
**MESSAGE /app/fhircast/\<topic>/\<event>**  
Send the event to the event endpoint. The implementation SHALL refuse messages send to the wrong endpoint.
