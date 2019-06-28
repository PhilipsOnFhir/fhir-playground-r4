import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MaterialModule} from "./material/material.module";
import { SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import { HttpClientModule} from "@angular/common/http";
import { RouterModule} from "@angular/router";
import { PatientImageSelectorComponent } from './component/patient-image-selector/patient-image-selector.component';
import {TopicService} from "./service/topic.service";
import { LaunchDisplayComponent } from './component/launch-display/launch-display.component';

@NgModule({
  declarations: [
    AppComponent,
    PatientImageSelectorComponent,
    LaunchDisplayComponent,
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [
    SmartOnFhirService, TopicService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
