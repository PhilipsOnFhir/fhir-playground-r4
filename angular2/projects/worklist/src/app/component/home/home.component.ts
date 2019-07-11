import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HumanName,Practitioner} from "fhir2angular-r4";
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {TopicService} from "../../service/topic.service";
import {FhirCastService} from "../../service/fhir-cast.service";
import {HumanNameUtil} from "../../fhir-r4/util/humanname-util";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  @Output() logOut = new EventEmitter();
  topicIdSet = false;
  initialised = false;
  topicId = "??";
  // launchSessions = new Array<DomainResource>();
  private practitioner: Practitioner;
  practitionerName: string;
  private topicIds: string[];
  selectedTopic: string;
  newTopicValue="new";
  private loggedout = false;

  constructor( private sofs:SmartOnFhirService, private topicService: TopicService, private fhircast: FhirCastService) {

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

    let topicUrl = "http://localhost:9444/api/fhircast/websub/"+topicId;
    let fhirUrl  = "http://localhost:9444/api/fhircast/fhir/"+topicId;

    // this.sofs.initialize( "http://localhost:9444/api/fhircast/fhir/"+topicId, '' ).subscribe(
    this.sofs.initialize( fhirUrl, '' ).subscribe(
      data => console.log(data),
      error => {
      },
      () => {
        console.log('sofs initialisation ready');
        this.initialised = true;
        // this.fhircast.login("http://localhost:9444/api/fhircast/websub/"+topicId, this.topicId );
        this.fhircast.login(topicUrl, this.topicId );
        this.fhircast.subscribe().subscribe( fce => {
            console.log(fce);
            switch ( fce.hub_event ) {
              case "user-logout":
                console.log(fce);
                this.closeCurrentTopic();
                break;
            }
          }
          , err => console.log(err)
        );
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
    this.fhircast.logout().subscribe( nxt => {
      console.log("Event send");
      this.topicService.closeTopic( this.topicId ).subscribe(
        next =>{
          // this.updateTopicIds();
          this.topicIdSet = false;
          this.initialised = false;
          this.selectedTopic = null;
          this.topicId = null;
          this.loggedout = true;
          this.logOut.emit(true);
        },
        error => console.log(error)
      );
    });
  }

  logout() {
    this.logOut.emit(true);
  }
}
