import { Component } from '@angular/core';
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {TopicService} from "./service/topic.service";
import {Patient} from "../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../fhir2angular-r4/src/lib/DomainResource";
import {Practitioner} from "../../../fhir2angular-r4/src/lib/Practitioner";
import {HumanName} from "../../../fhir2angular-r4/src/lib/HumanName";
import {Resource} from "../../../fhir2angular-r4/src/lib/Resource";
import {FhirCastService} from "./service/fhir-cast.service";

@Component({
  selector: 'app-root',
  template: `
    <mat-toolbar>
      <span><b>WorkList</b></span>
      <span class="example-fill-remaining-space"></span>
      <span><b>topic: </b>{{topicId}}</span>
    </mat-toolbar>
    <div *ngIf="!initialised">
        <mat-progress-spinner
            color="primary"
            mode="indeterminate">
        </mat-progress-spinner>
    </div>
    <div *ngIf="initialised">
<!--      {{this.launchSessions.length}}-->
      <mat-tab-group [selectedIndex]="1+launchSessions.length">
        <mat-tab label="Open new">
          <app-patient-image-selector
            (patientSelected)="patientSelected($event)"
            (imagingStudySelected)="imagingStudySelected($event)">
          </app-patient-image-selector>
        </mat-tab>
        <mat-tab *ngFor="let domainResource of launchSessions" label="{{domainResource.resourceType}} {{domainResource.id}}">
          <app-launch-display [context]="domainResource" [practitioner]="practitioner" (closeLaunch)="launchedClosed($event)"></app-launch-display>
        </mat-tab>
      </mat-tab-group>
    </div>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  initialised = false;
  topicId = "??";
  launchSessions = new Array<DomainResource>();
  private practitioner: Practitioner;

  constructor( private sofs:SmartOnFhirService, private topicService: TopicService, private fhircastService: FhirCastService) {
  }

  ngOnInit(): void {
    this.topicId = this.topicService.getTopicId();
    this.sofs.initialize( environment.fhirUrl, '' ).subscribe(
      data => console.log(data),
      error => {
      },
      () => {
        console.log('initialisation ready');
        this.initialised = true;
        this.fhircastService.login();
      }
    );
    this.practitioner = new Practitioner();
    this.practitioner.active = true;
    this.practitioner.name = new Array<HumanName>(1);
    this.practitioner.name[0] = new HumanName();
    this.practitioner.name[0].family="John"
    this.practitioner.name[0].given = new Array<string>(1);
    this.practitioner.name[0].given[0] = "Doe";

  }

  patientSelected( event: Patient) {
    // console.log(event);
    // let tmp  = this.launchSessions;
    // this.launchSessions = new Array<DomainResource>();
    // tmp.forEach( ls => this.launchSessions.push(ls) );
    this.launchSessions.push(event);
    this.fhircastService.openPatient(event);
  }

  imagingStudySelected( event: ImagingStudy) {
    this.launchSessions.push(event);
    this.fhircastService.openStudy(event);
  }

  launchedClosed(contextResource: Resource) {
    // console.log("close launch "+contextResource);
    let tmp  = this.launchSessions;
    this.launchSessions = new Array<DomainResource>();
    tmp.forEach( ls => {
      if ( ls.resourceType===contextResource.resourceType && ls.id===contextResource.id ){
        // console.log("close pannel "+ls.id);
        if ( ls.resourceType===Patient.def ){
          this.fhircastService.closePatient( ls );
        } else{
          this.fhircastService.closeStudy( ls );
        }
      }else{
        this.launchSessions.push(ls)
      }
    } );
  }
}
