import { Component } from '@angular/core';
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {TopicService} from "./service/topic.service";
import {Patient} from "../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../fhir2angular-r4/src/lib/DomainResource";

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
      {{this.launchSessions.length}}
      <mat-tab-group [selectedIndex]="1+launchSessions.length">
        <mat-tab label="Open new">
          <app-patient-image-selector
            (patientSelected)="patientSelected($event)"
            (imagingStudySelected)="imagingStudySelected($event)">
          </app-patient-image-selector>
        </mat-tab>
        <mat-tab *ngFor="let domainResource of launchSessions" label="{{domainResource.id}}">
          {{domainResource.resourceType}}
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

  constructor( private sofs:SmartOnFhirService, private topicService: TopicService) {
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

      }
    );
  }

  patientSelected( event: Patient) {
    console.log(event);
    let a  = this.launchSessions;
    this.launchSessions = new Array<DomainResource>();
    this.launchSessions.push(event);
    a.forEach( ls => this.launchSessions.push(ls) );
  }

  imagingStudySelected( event: ImagingStudy) {
    this.launchSessions.push(event);
  }
}
