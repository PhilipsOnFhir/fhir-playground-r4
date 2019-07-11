import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SmartOnFhirService} from "../../fhir-r4/smart-on-fhir.service";
import {Resource,Encounter,ResourceTypeEnum,ImagingStudy,Patient,Reference,EncounterStatusEnum,Practitioner} from "fhir2angular-r4";
import {HumanNameUtil} from "../../fhir-r4/util/humanname-util";
import {Encounter_Participant} from "fhir2angular-r4";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-launch-display',
  templateUrl: './launch-display.component.html',
  styleUrls: ['./launch-display.component.css']
})
export class LaunchDisplayComponent implements OnInit {
  @Input() context:Resource;
  @Input() launch:string;
  @Input() practitioner: Practitioner;
  @Output() closeLaunch = new EventEmitter();

  private encounter: Encounter;
  private imagingStudy: ImagingStudy;
  private patient: Patient;

  private patientName: string;
  private practitionerName: string;
  private title = '-';
  smartApps: any;

  constructor( private sofs: SmartOnFhirService ) {
    this.smartApps = environment.smartApps;
  }

  ngOnInit() {
    this.encounter = new Encounter();
    if ( this.context.resourceType === ResourceTypeEnum.IMAGINGSTUDY ){
      this.imagingStudy = this.context as ImagingStudy;
      if ( this.imagingStudy.subject ){
        this.sofs.getReference(this.imagingStudy.subject ).subscribe( patient =>{
          this.setPatient( patient as Patient );
        })
      }
      this.title = this.imagingStudy.resourceType+" "+ this.imagingStudy.id;
    } else {
      this.setPatient( this.context as Patient );
      this.title = this.patient.resourceType+" "+ this.patient.id;
    }
    // if ( this.patient ) {
    //   this.encounter.subject = new Reference();
    //   this.encounter.subject.reference = this.patient.resourceType + "/" + this.patient.id;
    // }
    this.encounter.status = EncounterStatusEnum.IN_PROGRESS;
    this.encounter.participant = Array<Encounter_Participant>(1);
    this.encounter.participant[0] = new Encounter_Participant();
    this.encounter.participant[0].individual = new Reference();
    this.encounter.participant[0].individual.reference = this.practitioner.resourceType+"/"+this.practitioner.id;

    // if ( this.patient && this.patient.name ){ this.patientName = HumanNameUtil.getPreferredName(this.patient.name[0])}
    if ( this.practitioner && this.practitioner.name ){ this.practitionerName = HumanNameUtil.getPreferredName(this.practitioner.name[0])}
  }

  setPatient( patient: Patient){
    this.patient = patient;
    if (patient ) {
      if (patient.name) {
        this.patientName = HumanNameUtil.getPreferredName(this.patient.name[0])
      }
      this.encounter.subject = new Reference();
      this.encounter.subject.reference = this.patient.resourceType + "/" + this.patient.id;
    }
  }

  close() {
    console.log("close");
    this.closeLaunch.emit(this.context);
  }

  launchApp(app: any) {
    console.log("launch ");
    console.log(app);
    let url=app.url+"/?launch="+this.launch+"&iss="+this.sofs.getUrl();
    window.open(url);
  }
}
