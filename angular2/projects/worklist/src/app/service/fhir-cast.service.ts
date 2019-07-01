import { Injectable } from '@angular/core';
import {Patient} from "../../../../fhir2angular-r4/src/lib/Patient";
import {ImagingStudy} from "../../../../fhir2angular-r4/src/lib/ImagingStudy";
import {DomainResource} from "../../../../fhir2angular-r4/src/lib/DomainResource";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FhirCastService {

  constructor(  private http: HttpClient ) {
  }

  openPatient(event: Patient) {
    console.log("patient opened")
  }

  openStudy(event: ImagingStudy) {
    console.log("imaging opened")
  }

  closePatient(patient: DomainResource) {
    console.log("close patient")
  }
  closeStudy(study: DomainResource) {
    console.log("close study")
  }

  login() {
    console.log("login");
  }

  logout() {
    console.log("logout");
  }
}
