import {Component} from '@angular/core';
import {SmartOnFhirService} from "./fhir-r4/smart-on-fhir.service";
import {TopicService} from "./service/topic.service";
import {FhirCastService} from "./service/fhir-cast.service";
import {HumanNameUtil} from "./fhir-r4/util/humanname-util";
import {HumanName,Practitioner} from "fhir2angular-r4";
import {ConnectorService} from "./service/connector.service";

@Component({
  selector: 'app-root',
  template: `
    <div class="container" >
      <button *ngIf="!isLoggedIn" class="btn btn-primary" (click)="login()" type="submit">Login</button>
      <div *ngIf="isLoggedIn" class="content">
        <span>Welcome !!</span>
        <a class="btn btn-default pull-right"(click)="logout()" href="#">Logout</a>
        <br/>
        <app-home></app-home>
      </div>
    </div>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  isLoggedIn: boolean = false;

  constructor( private httpSec: ConnectorService) {
  }

  ngOnInit(): void {
    // this.isLoggedIn = this.httpSce.isLoggedIn();
    this.isLoggedIn = this.httpSec.checkCredentials();
    let i = window.location.href.indexOf('code');
    if(!this.isLoggedIn && i != -1){
      this.httpSec.retrieveToken(window.location.href.substring(i + 5));
    }
  }



  login() {
    this.httpSec.login();
  }

  logout() {
    this.httpSec.logout();
  }
}
