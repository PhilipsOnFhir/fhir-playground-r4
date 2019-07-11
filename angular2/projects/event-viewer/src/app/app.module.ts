import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { RouterModule, Routes} from "@angular/router";
import { MaterialModule} from "./material/material.module";
import { HttpClientModule} from "@angular/common/http";
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {FhirCastService} from "./service/fhir-cast.service";

const appRoutes: Routes = [
  { path: 'launch', component: AppComponent },
  { path: '', component: AppComponent}
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    RouterModule.forRoot(appRoutes ),
    HttpClientModule
  ],
  providers: [ SmartOnFhirService, FhirCastService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
