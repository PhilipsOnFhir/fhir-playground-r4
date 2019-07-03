import { Component } from '@angular/core';
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {TopicService} from "./service/topic.service";
import {Practitioner} from "../../../fhir2angular-r4/src/lib/Practitioner";
import {HumanName} from "../../../fhir2angular-r4/src/lib/HumanName";
import {FhirCastService} from "./service/fhir-cast.service";
import {HumanNameUtil} from "./fhir-r4/util/humanname-util";

@Component({
  selector: 'app-root',
  template: `
    <mat-toolbar>
      <mat-toolbar-row>
        <span><b>WorkList</b></span>
        <span class="example-fill-remaining-space"></span>
<!--        <span><b>topic: </b>{{topicId}}</span>-->
        <span *ngIf="topicIdSet">
            topic-id: {{topicId}}
        </span>
      </mat-toolbar-row>
      <mat-toolbar-row>
        <span>Practitioner: {{practitionerName}}</span>
        <span class="example-fill-remaining-space"></span>
        <span *ngIf="topicIdSet"><button mat-icon-button (click)="closeCurrentTopic()"><mat-icon>done</mat-icon></button></span>
      </mat-toolbar-row>
    </mat-toolbar>
    
    
    <div *ngIf="!topicIdSet">
      <div *ngIf="topicIds && topicIds.length>0">
        Select topic:
        <mat-form-field>
          <mat-label></mat-label>
          <mat-select [(value)]="selectedTopic" (selectionChange)="selectTopic()">
            <mat-option *ngFor="let tid of topicIds" [value]="tid">
              {{tid}}
            </mat-option>
            <mat-option [value]="newTopicValue"><i>create new</i></mat-option>
          </mat-select>
        </mat-form-field>
      </div>
      <p>{{selectedTopic}}</p>
<!--      <button mat-raised-button (click)="createNewTopic()">start new topic</button>-->
    </div>
    <div *ngIf="topicIdSet && !initialised">
        <mat-progress-spinner
            color="primary"
            mode="indeterminate">
        </mat-progress-spinner>
    </div>
    <div *ngIf="topicIdSet && initialised">
      <app-worklist [practitioner]="practitioner"></app-worklist>
<!--      {{this.launchSessions.length}}-->
      
    </div>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  topicIdSet = false;
  initialised = false;
  topicId = "??";
  // launchSessions = new Array<DomainResource>();
  private practitioner: Practitioner;
  practitionerName: string;
  private topicIds: string[];
  selectedTopic: string;
  newTopicValue="new";

  constructor( private sofs:SmartOnFhirService, private topicService: TopicService, private fhircastService: FhirCastService) {
  }

  ngOnInit(): void {
    this.updateTopicIds();

    this.practitioner = new Practitioner();
    this.practitioner.active = true;
    this.practitioner.name = new Array<HumanName>(1);
    this.practitioner.name[0] = new HumanName();
    this.practitioner.name[0].family="John"
    this.practitioner.name[0].given = new Array<string>(1);
    this.practitioner.name[0].given[0] = "Doe";

    this.practitionerName = HumanNameUtil.getPreferredName(this.practitioner.name[0]);
  }

  private setTopicId( topicId:string){
    this.topicId = topicId;
    this.topicIdSet = true;

    this.sofs.initialize( "http://localhost:9444/api/fhircast/fhir/"+topicId, '' ).subscribe(
      data => console.log(data),
      error => {
      },
      () => {
        console.log('sofs initialisation ready');
        this.initialised = true;
        this.fhircastService.login("http://localhost:9444/api/fhircast/topic/"+topicId+"/websub", this.topicId );
        this.fhircastService.subscribe();
      }
    );
  }

  private updateTopicIds(){
    this.topicService.updateTopidIds().subscribe(
      data => console.log(data),
      error => console.log(error),
      () => {
        console.log('topic initialisation ready');
        this.topicIds = this.topicService.getTopicIds();
        if ( this.topicIds.length==0){
          this.topicService.createTopicId().subscribe(
            next => {
              this.topicIds = this.topicService.getTopicIds();
              this.setTopicId(this.topicIds[0]);
              // this.topicId = this.topicIds[0]
              // this.topicIdSet = true;
            })
        }
      }
    );
  }

  selectTopic() {
    console.log(this.selectedTopic);
    if ( this.selectedTopic===this.newTopicValue ){
      this.topicService.createTopicId().subscribe( topicId => {
        this.setTopicId(topicId);
      });
    } else{
      this.setTopicId(this.selectedTopic);
    }
  }

  closeCurrentTopic() {
    console.log("close topic");
    this.topicService.closeTopic( this.topicId ).subscribe(
      next =>{
        this.fhircastService.logout();
        this.updateTopicIds();
        this.topicIdSet = false;
        this.initialised = false;
        this.selectedTopic = null;
        this.topicId = null;
      },
      error => console.log(error)
    );
  }
}
