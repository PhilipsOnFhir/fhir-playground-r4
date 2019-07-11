import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {observable, Observable} from "rxjs";
import {ConnectorService} from "./connector.service";
import {Patient} from "../../../../fhir2angular-r4/src/lib/Patient";
import {Resource} from "../../../../fhir2angular-r4/src/lib/Resource";

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private topicIds: string[];
  private baseUrl = "http://localhost:9444/api/fhircast/topic/";

  constructor(  private http: HttpClient, private httpSec: ConnectorService ) {
  }

  updateTopidIds(): Observable<any> {
    return new Observable<any>(
      observable => {

        this.httpSec.get<string[]>( this.baseUrl  ).subscribe(
          next => { console.log( next);
              this.topicIds = next;
              observable.next();
              observable.complete();
          },
          error=> { console.log( error);         observable.error('Invalid URL');
          }
        );
      }
    );

  }

  createTopicId(): Observable<string> {
    return new Observable<string>(
      observable => {
        this.httpSec.post<any>( this.baseUrl, "", {observe: 'response' as 'body'} ).subscribe(
          next => {
              let topicId = (next as HttpResponse<any>).body.topicID;
              this.updateTopidIds().subscribe(
                next => {
                  observable.next(topicId);
                  observable.complete()
                },
                error => {}
              )
          },
        error=> { observable.error(error) }
        );

      }
    )
  }


  getTopicIds():string[] {
    return this.topicIds;
  }

  closeTopic( topicId:string ): Observable<string> {
    console.log("close topic "+topicId);
    console.log(this.baseUrl+topicId);
    return this.httpSec.delete( this.baseUrl+topicId);
  }

  openLaunch(topicId:string,  patient: Resource) :Observable<string> {
    console.log("topic open launch for "+patient);
      return this.httpSec.post(this.baseUrl+topicId, patient, {responseType: 'text' as 'json' } );
  }
}
