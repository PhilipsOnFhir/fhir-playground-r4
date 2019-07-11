import {Component, Input, OnInit} from '@angular/core';
import {DomainResource} from "fhir2angular-r4";
import {Patient} from "fhir2angular-r4";
import {ImagingStudy} from "fhir2angular-r4";
import {Resource} from "fhir2angular-r4";
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "../../service/fhir-cast.service";
import {Practitioner} from "fhir2angular-r4";
import {TopicService} from "../../service/topic.service";

class LaunchSession {
  domainResource: DomainResource;
  launchId:string;
}

@Component({
  selector: 'app-worklist',
  templateUrl: './worklist.component.html',
  styleUrls: ['./worklist.component.css']
})
export class WorklistComponent implements OnInit {
  @Input() practitioner: Practitioner;
  @Input() topicId: string;
  launchSessions = new Array<LaunchSession>();
  selectedLaunchIndex: number = 0;

  constructor( private sofs: SmartOnFhirService, private fhircast: FhirCastService, private topicService: TopicService) { }

  ngOnInit() {
    this.fhircast.subscribe().subscribe( fce => {
        console.log(fce);
        switch ( fce.hub_event ) {
          case "open-patient-chart": {
            let p = fce.context[0].resource as Patient;
            this.patientSelected(p);
            break;
          }
          case "close-patient-chart":{
            let p = fce.context[0].resource as Patient;
            this.launchedClosed(p);
            break;
          }
          case "open-imaging-study": {
            let s = fce.context[0].resource as ImagingStudy;
            this.imagingStudySelected(s);
            break;
          }
          case "close-imaging-study":{
            let s = fce.context[0].resource as ImagingStudy;
            this.launchedClosed(s);
            break;
          }
        }
      }
      , err => console.log(err)
    );
  }

  patientSelected( patient: Patient) {
    // console.log(patient);
    this.locateAndChangeFocus(patient);
    this.fhircast.openPatient(patient);
  }

  private locateAndChangeFocus( domainResource: DomainResource ) {
    let found = false;
    let i = 0;
    while (i < this.launchSessions.length && !found) {
      let ls = this.launchSessions[i];
      if (ls.domainResource.resourceType == domainResource.resourceType && ls.domainResource.id === domainResource.id) {
        found = true;
      } else {
        i++;
      }
    }

    if (!found) {
      console.log("new resource "+domainResource.id);
      this.sofs.getResource( domainResource.resourceType+"/"+domainResource.id ).subscribe( dr=> {
        let ls = new LaunchSession();
        ls.domainResource=dr;
        // console.log(ls);

        this.topicService.openLaunch(this.topicId, domainResource).subscribe(
          nxt => {
            console.log("launch received "+nxt);
            ls.launchId=nxt;
            this.launchSessions[i]=ls;
            console.log( this.launchSessions);
            this.selectedLaunchIndex = this.launchSessions.length + 1;
          },
          err => console.log(err)
        );
      });

      // let ls = new LaunchSession();
      // ls.domainResource =domainResource;
      // ls.launchId=launch;
      // this.launchSessions.push(ls);
      // console.log(ls);
      // this.selectedLaunchIndex = this.launchSessions.length + 1;
    } else {
      this.selectedLaunchIndex = i;
    }
  }

  imagingStudySelected( study: ImagingStudy) {
    this.locateAndChangeFocus( study );
    this.fhircast.openStudy(study);
  }

  launchedClosed(contextResource: Resource) {
    // console.log("close launch "+contextResource);
    let tmp  = this.launchSessions;
    this.launchSessions = new Array<LaunchSession>();
    tmp.forEach( ls => {
      if ( ls.domainResource.resourceType===contextResource.resourceType && ls.domainResource.id===contextResource.id ){
        // console.log("close pannel "+ls.id);
        if ( ls.domainResource.resourceType===Patient.def ){
          this.fhircast.closePatient( ls.domainResource );
        } else{
          this.fhircast.closeStudy( ls.domainResource );
        }
      }else{
        this.launchSessions.push(ls)
      }
    } );
    this.selectedLaunchIndex=0;

  }

  selectedIndexChange(index: number) {
    console.log("tab focus on:" + index);
    let ls = this.launchSessions[index-1];
    if ( ls && ls.domainResource.resourceType===Patient.def ) {
      this.fhircast.openPatient(ls.domainResource as Patient);
    } else if ( ls && ls.domainResource.resourceType===ImagingStudy.def ){
      this.fhircast.openStudy(ls.domainResource as ImagingStudy);
    }
  }
}
