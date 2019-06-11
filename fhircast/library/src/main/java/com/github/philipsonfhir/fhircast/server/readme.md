https://healthservices.atlassian.net/wiki/spaces/TAC/pages/21463048/HSPC+SMART+Java+Client+Collaboration

Aproach taken:    
   * expose FHIR cast hub: <base-url>/fhircast

# <base-url>/fhircast
## GET : return list of sessions
  
## POST <base-url>/fhircast: create new session
Request:
  * patient - optional - string
  * encounter - optional - string
  * study -iptional - string
  
Response:
  * session-id

# <base-url>/fhircast/<session-id>
## GET : return session info - TODO  
## DELETE: remove session
OK  

# Launch
<app-url>?fhircast-session:xxxx

 



            