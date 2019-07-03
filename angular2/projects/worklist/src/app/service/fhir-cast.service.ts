import { Injectable } from '@angular/core';
import {Patient} from "../../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../../fhir2angular-r4/src/lib/DomainResource";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {timestamp} from "rxjs/operators";
import {Resource} from "../../../../fhir2angular-r4/src/lib/Resource";

@Injectable({
  providedIn: 'root'
})
export class FhirCastService {
  private topicUrl: string;
  private initialized: boolean = false;
  private topicId: string;

  constructor(  private http: HttpClient ) {
  }

  subscribe(){
    console.log("Subscribe to fhicast events");
    let subscriptionRequest =
      "{\n" +
      // "\t\"hub.callback\":null,\n" +
      "\t\"hub.mode\":\"subscribe\",\n" +
      "\t\"hub.topic\":\""+this.topicId+"\",\n" +
      "\t\"hub.secret\":\"randomSecret\",\n" +
      "\t\"hub.events\":\"open-patient-chart,close-patient-chart\",\n" +
      // "\t\"hub.lease_seconds\":null,\n" +
      "\t\"hub.channel.type\":\"websocket\"\n" +
      "}\n";
    console.log(subscriptionRequest);
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/json');
    this.http.post(this.topicUrl, subscriptionRequest, {headers: myHeaders, observe: 'response'} ).subscribe(
      next => console.log(next),
      error => console.log(error)
    )
  }

  openPatient(patient: Patient) {
    console.log("patient opened");

    let body : string = this.getEventString( this.topicId, "open-patient-chart", patient);
    console.log(body);
    this.sendEvent( body );
  }

  openStudy(study: ImagingStudy) {
    console.log("imaging opened")
    let body = this.getEventString( this.topicId, "open-imaging-study", study);
    this.sendEvent( body );
  }


  closePatient(patient: DomainResource) {
    console.log("close patient")
    let body = this.getEventString( this.topicId, "close-patient-chart", patient);
    this.sendEvent( body );
  }
  closeStudy(study: DomainResource) {
    console.log("close study")
    let body = this.getEventString( this.topicId, "close-imaging-study", study);
    this.sendEvent( body );
  }

  login(topicUrl: string, topicId:string ){
    console.log("login "+topicUrl);
    this.topicUrl = topicUrl;
    this.topicId = topicId;
    this.initialized = true;
  }

  logout() {
    console.log("logout");
    let body = this.getEventString( this.topicId, "user-logout",null );
    this.sendEvent( body );
  }

  private sendEvent(body: string) {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/json');
    console.log( body);

    this.http.post(this.topicUrl, body, {headers: myHeaders, observe: 'response'} ).subscribe(
      next => console.log(next),
      error => console.log(error)
    )
  }

  private getEventString( topicId: string, eventType: string, resource: Resource): string {
    if ( resource ) {
      return(
        "{ \"event\": \n" +
        "\t{ \"hub.topic\": \"" + topicId + "\", \n" +
        "\t  \"hub.event\":\"" + eventType + "\",\n" +
        "\t  \"context\": [\n" +
        "\t\t{ \t\"key\": \"patient\",\n" +
        "\t\t\t\"resource\": \n" +
        "\t\t\t\t{\t\"resourceType\":\"" + resource.resourceType + "\",\n" +
        "\t\t\t\t\t\"id\":\"" + resource.id + "\"\n" +
        "\t\t\t\t}\n" +
        "\t\t}]\n" +
        "\t}\n" +
        "}"
      );
    } else {
      return(
        "{ \"event\": \n" +
        "\t{ \"hub.topic\": \"" + topicId + "\", \n" +
        "\t  \"hub.event\":\"" + eventType + "\"\n" +
        "\t}\n" +
        "}"
      );
    }
    return "-";
  }
}
