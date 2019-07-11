import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MaterialModule} from "./material/material.module";
import { SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import { HttpClientModule} from "@angular/common/http";
import { RouterModule, Routes} from "@angular/router";
import { PatientImageSelectorComponent } from './component/patient-image-selector/patient-image-selector.component';
import { TopicService} from "./service/topic.service";
import { LaunchDisplayComponent } from './component/launch-display/launch-display.component';
import { WorklistComponent } from './component/worklist/worklist.component';
import { HomeComponent} from "./component/home/home.component";
import { ConnectorService} from "./service/connector.service";

const appRoutes: Routes = [
  { path: 'launch', component: AppComponent },
  { path: '', component: AppComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    PatientImageSelectorComponent,
    LaunchDisplayComponent,
    WorklistComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    SmartOnFhirService, TopicService, ConnectorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
