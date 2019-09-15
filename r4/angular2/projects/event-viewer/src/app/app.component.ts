import {Component, OnInit} from '@angular/core';
import {ActivatedRoute } from "@angular/router";
import {Location} from "@angular/common";
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "./service/fhir-cast.service";
import {SmartMessagingService} from "./fhir-r4/smart-messaging.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'event-viewer';
  private initialised: boolean = false;
  private events: any[] = new Array(0);

  // constructor( private router: Router, private _AcLocation: Location ){
  constructor(private route: ActivatedRoute, private sofs: SmartOnFhirService, private fhircast: FhirCastService, private sm: SmartMessagingService ){
  }

  ngOnInit(): void {
    this.sofs.initialize( environment.client.id, environment.client.secret, "fhircast" ).subscribe(
      next=>{ console.log(next) },
      err => { console.log(err) },
       () => {
        this.initialised = this.sofs.isInitialized();
        let topicId = this.sofs.getToken()["cast-hub"];
        this.fhircast.login( "http://localhost:9401/api/fhircast/websub/"+topicId, topicId);
         this.fhircast.subscribe().subscribe(next => {
           console.log(next);
           this.events.unshift(next);
         })
      }
    );

    // App needs to know EHR's origin.
// Add a smart_messaging_origin launch context parameter alongside the access_token
// to tell the app what the EHR's origin will be
    console.log("--------------send-----------------");
    console.log(window);
    window.opener.postMessage({
      "authentication": {// maybe }
        "messageId": "lksjdlkdsajhfdahs;lkjfdsajfh",
        "messageType": "scratchpad.create",
        "payload": "dsadsajkdjsa"
      }}, "*" );
    window.parent.postMessage({
      "authentication": {// maybe }
        "messageId": "lksjdlkdsajhfdahs;lkjfdsajfh",
        "messageType": "scratchpad.create",
        "payload": "dsadsajkdjsa"
      }}, "*" );
  }
}
