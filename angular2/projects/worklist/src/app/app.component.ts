import { Component } from '@angular/core';
import {environment} from "../environments/environment";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";

@Component({
  selector: 'app-root',
  template: `
    <!--The content below is only a placeholder and can be replaced.-->
    <div *ngIf="!initialised">
        <mat-progress-spinner
            color="primary"
            mode="indeterminate">
        </mat-progress-spinner>
    </div>
    <app-patient-image-selector *ngIf="initialised"></app-patient-image-selector>
    
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'worklist';
  initialised = false;

  constructor( private sofs:SmartOnFhirService) {
  }

  ngOnInit(): void {
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

}
