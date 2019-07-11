import {Component, Input, OnInit} from '@angular/core';
import {DomainResource} from "fhir2angular-r4";
import {Patient} from "fhir2angular-r4";
import {ImagingStudy} from "fhir2angular-r4";
import {Resource} from "fhir2angular-r4";
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "../../service/fhir-cast.service";
import {Practitioner} from "fhir2angular-r4";

@Component({
  selector: 'app-worklist',
  templateUrl: './worklist.component.html',
  styleUrls: ['./worklist.component.css']
})
export class WorklistComponent implements OnInit {
  @Input() practitioner: Practitioner;
  launchSessions = new Array<DomainResource>();
  selectedLaunchIndex: number = 0;

  constructor( private sofs: SmartOnFhirService, private fhircast: FhirCastService) { }

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

  private locateAndChangeFocus( domainResource: DomainResource) {
    let found = false;
    let i = 0;
    while (i < this.launchSessions.length && !found) {
      let ls = this.launchSessions[i];
      if (ls.resourceType == domainResource.resourceType && ls.id === domainResource.id) {
        found = true;
      } else {
        i++;
      }
    }

    if (!found) {
      this.sofs.getResource( domainResource.resourceType+"/"+domainResource.id ).subscribe( dr=> {
        this.launchSessions[i]=dr;
      });
      this.launchSessions.push(domainResource);
      this.selectedLaunchIndex = this.launchSessions.length + 1;
    } else {
      this.selectedLaunchIndex = i;
    }
  }

  imagingStudySelected( study: ImagingStudy) {
    this.locateAndChangeFocus(study);
    this.fhircast.openStudy(study);
  }

  launchedClosed(contextResource: Resource) {
    // console.log("close launch "+contextResource);
    let tmp  = this.launchSessions;
    this.launchSessions = new Array<DomainResource>();
    tmp.forEach( ls => {
      if ( ls.resourceType===contextResource.resourceType && ls.id===contextResource.id ){
        // console.log("close pannel "+ls.id);
        if ( ls.resourceType===Patient.def ){
          this.fhircast.closePatient( ls );
        } else{
          this.fhircast.closeStudy( ls );
        }
      }else{
        this.launchSessions.push(ls)
      }
    } );
    this.selectedLaunchIndex=0;

  }

  selectedIndexChange(index: number) {
    console.log("tab focus on:" + index);
    let resource = this.launchSessions[index-1];
    if ( resource && resource.resourceType===Patient.def ) {
      this.fhircast.openPatient(resource as Patient);
    } else if ( resource && resource.resourceType===ImagingStudy.def ){
      this.fhircast.openStudy(resource as ImagingStudy);
    }
  }
}
