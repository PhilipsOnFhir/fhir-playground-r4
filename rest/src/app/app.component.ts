import {Component, OnInit} from '@angular/core';
import {FhirCastRestService} from './service/fhir-cast-rest.service';
import {timer} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'com.github.philipsonfhir.fhircast.app';
  private context: string;
  private status: string;
  topicUrl:string;
  private timer;

  constructor( private fhirCastRestService: FhirCastRestService) {

  }

  ngOnInit(): void {
    this.fhirCastRestService.getTopicUrl();
    timer(1);
    this.timer = timer( 0, 1000);
    this.timer.subscribe( t=> {
      console.log(t);
      this.fhirCastRestService.updateContext().subscribe( upd => {
          console.log(upd);
          this.context = this.fhirCastRestService.getContext();
          this.status = this.fhirCastRestService.getStatus();
        },
        error => {
          this.context = this.fhirCastRestService.getContext();
          this.status = this.fhirCastRestService.getStatus();
        }
      );
    });

  }

}
