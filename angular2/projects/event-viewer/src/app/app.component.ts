import {Component, OnInit} from '@angular/core';
import {ActivatedRoute } from "@angular/router";
import {Location} from "@angular/common";
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "./service/fhir-cast.service";

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
  constructor(private route: ActivatedRoute, private sofs: SmartOnFhirService, private fhircast: FhirCastService ){
  }

  ngOnInit(): void {
    this.sofs.initialize( environment.client.id, environment.client.secret, "fhircast" ).subscribe(
      next=>{ console.log(next) },
      err => { console.log(err) },
       () => {
        this.initialised = this.sofs.isInitialized();
        let topicId = this.sofs.getToken()["cast-hub"];
        this.fhircast.login( "http://localhost:9444/api/fhircast/websub/"+topicId, topicId);
         this.fhircast.subscribe().subscribe(next => {
           console.log(next);
           this.events.unshift(next);
         })
      }
    );


  }
}
