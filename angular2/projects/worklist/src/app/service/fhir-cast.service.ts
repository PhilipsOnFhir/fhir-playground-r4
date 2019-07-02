import { Injectable } from '@angular/core';
import {Patient} from "../../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../../fhir2angular-r4/src/lib/DomainResource";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {timestamp} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class FhirCastService {
  private topicUrl: string;
  private initialized: boolean = false;
  private topicId: string;

  constructor(  private http: HttpClient ) {
  }

  openPatient(event: Patient) {
    console.log("patient opened");
    // let body = new FhirCastBody();
    // body.id = "myId";
    // body.timestamp = "now";
    // body.hub_topic = this.topicUrl;
    let patientJson = JSON.stringify(event);

    let body =
      "{ \"event\": \n" +
      "\t{ \"hub.topic\": \"FC1561990516010\", \n" +
      "\t  \"hub.event\":\"open-patient-chart\",\n" +
      "\t  \"context\": [\n" +
      "\t\t{ \t\"key\": \"patient\",\n" +
      "\t\t\t\"resource\": \n" +
      "\t\t\t\t{\t\"resourceType\":\"Patient\",\n" +
      "\t\t\t\t\t\"id\":\"WORKLIST-4\"\n" +
      "\t\t\t\t}\n" +
      "\t\t}]\n" +
      "\t}\n" +
      "}";

    const myHeaders = new HttpHeaders().set('Content-Type', 'application/json');
    // let headers = new HttpHeaders();
    // headers.
    console.log( body);
    this.http.post(this.topicUrl,body, {headers: myHeaders, observe: 'response'} ).subscribe(
      next => console.log(next),
      error => console.log(error)
    )
  }

  openStudy(event: ImagingStudy) {
    console.log("imaging opened")
  }

  closePatient(patient: DomainResource) {
    console.log("close patient")
  }
  closeStudy(study: DomainResource) {
    console.log("close study")
  }

  login(topicUrl: string, topicId:string ){
    console.log("login "+topicUrl);
    this.topicUrl = topicUrl;
    this.topicId = topicId;
    this.initialized = true;
  }

  logout() {
    console.log("logout");
  }
}

class FhirCastWorkflowEventEvent {
  hub_topic : string;
  hub_event : string;
  context : string[];
}

class FhirCastBody{
  id : string
  timestamp : string;
  // hub_callback : string;
  // hub_mode: string;
  // hub_topic : string;
  // hub_secret : string;
  // hub_events : string;
  // hub_lease_seconds: string;
  event: FhirCastWorkflowEventEvent;

  toJsonString(): string{
    return "{ " +
      +"\"event\": " + "\""+ "open-patient-chart" + "\""
      " }";
    // return "{ "
      // "\"id\": "+ this.id
      // + " \"timestamp\": \""+this.timestamp
      // + "\", \"hub.callback\": \""+ this.hub_callback
      // + "\", \"hub.mode\": \""+ this.hub_mode
      // + "\", \"hub.topic\": \""+ this.hub_topic
      // + "\"hub.topic\": \""+ this.hub_topic
      // + "\", \"hub.secret\": \""+ this.hub_secret
      // + "\", \"hub.events\": \""+ this.hub_events
      // + "\", \"hub.lease_seconds\": \""+ this.hub_lease_seconds
      // + "\"}";

  }
}
