import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {environment} from "../../../environments/environment";
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {Bundle} from "../../../../../fhir2angular-r4/src/lib/Bundle";
import {Patient} from "../../../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../../../fhir2angular-r4/src/lib/DomainResource";


@Component({
  selector: 'app-patient-image-selector',
  template: `
    <mat-tab-group>
      <mat-tab label="Patient">
        <table *ngIf="patients">
          <tr *ngFor="let entry of patients.entry">
            <td *ngIf="entry.resource">
              <button mat-button (click)="selectPatient(entry.resource)"> {{entry.resource.id}}</button>
            </td>
          </tr>
        </table>
      </mat-tab>
      <mat-tab label="ImageStudy">
        <table *ngIf="imagingstudies">
          <tr *ngFor="let entry of imagingstudies.entry">
            <td *ngIf="entry.resource">
              <button mat-button (click)="selectImageStudy(entry.resource)"> {{entry.resource.id}}</button>
            </td>
          </tr>
        </table>
      </mat-tab>
    </mat-tab-group>
  `,
  styleUrls: ['./patient-image-selector.component.css']
})
export class PatientImageSelectorComponent implements OnInit {
  private patients: Bundle;
  private imagingstudies: Bundle;

  @Output() patientSelected = new EventEmitter<Patient>();
  @Output() imagingStudySelected = new EventEmitter<ImagingStudy>();

  constructor( private sofs: SmartOnFhirService) { }

  ngOnInit() {
    this.sofs.searchResources( Patient.def, "" ).subscribe(
      bundle =>{
        // console.log(bundle);
        this.patients = bundle;
      }
    )
    this.sofs.searchResources( ImagingStudy.def, "" ).subscribe(
      bundle =>{
        // console.log(bundle);
        this.imagingstudies = bundle;
      }
    )
  }


  selectImageStudy(imagingStudy: DomainResource) {
    console.log("select imagingstudy "+imagingStudy.id);
    this.imagingStudySelected.emit(imagingStudy as ImagingStudy);
  }

  selectPatient(patient: DomainResource) {
    console.log("select patient "+patient.id);
    this.patientSelected.emit(patient as Patient);
    // const baseUrl = environment.topicViewerUrl;
    //
    // // TODO create launch context/topic
    // const launch = "sjaksalksjlak";
    // const iss = this.sofs.getUrl();
    // let launchUrl = baseUrl+"?launch="+launch+"&iss="+iss;
    // console.log(launchUrl);
    // window.open(launchUrl);
  }
}
