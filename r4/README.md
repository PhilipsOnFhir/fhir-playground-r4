## Port used

4401 - Worklist
4302 - EventViewer
9301 - Worklist server
9311 - FHIR Server

## start demo
### start hapi server
cd hapi-r4  
mvn jetty:run
## start worklist
cd worklist
run org\github\philipsonfhir\worklist\WorklistServer.java  
*further detail required*
## start worklist application
cd angular2
ng serve worklist --port 4401
