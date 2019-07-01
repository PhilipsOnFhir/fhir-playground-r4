import {Component, Input, OnInit} from '@angular/core';
import {DomainResource} from "../../../../../fhir2angular-r4/src/lib/DomainResource";
import {Patient} from "../../../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../../../fhir2angular-r4/src/lib/ImagingStudy";
import {Resource} from "../../../../../fhir2angular-r4/src/lib/Resource";
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "../../service/fhir-cast.service";
import {Practitioner} from "../../../../../fhir2angular-r4/src/lib/Practitioner";

@Component({
  selector: 'app-worklist',
  templateUrl: './worklist.component.html',
  styleUrls: ['./worklist.component.css']
})
export class WorklistComponent implements OnInit {
  @Input() practitioner: Practitioner;
  launchSessions = new Array<DomainResource>();

  constructor( private sofs: SmartOnFhirService, private fhircast: FhirCastService) { }

  ngOnInit() {
  }

  patientSelected( event: Patient) {
    // console.log(event);
    // let tmp  = this.launchSessions;
    // this.launchSessions = new Array<DomainResource>();
    // tmp.forEach( ls => this.launchSessions.push(ls) );
    this.launchSessions.push(event);
    this.fhircast.openPatient(event);
  }

  imagingStudySelected( event: ImagingStudy) {
    this.launchSessions.push(event);
    this.fhircast.openStudy(event);
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
  }
}
