import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {observable, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private topicId: string;
  private topicIds: string[];

  constructor(  private http: HttpClient ) {
  }

  updateTopidIds(): Observable<any> {
    return new Observable<any>(
      observable => {

        this.http.get<string[]>( "http://localhost:9444/api/fhircast/topic"  ).subscribe(
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
        this.http.post<any>( "http://localhost:9444/api/fhircast/topic", "", {observe: 'response' as 'body'} ).subscribe(
          next => {
              let topicId = next.body.topicID;
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
  getTopicId(): string{
    return this.topicId;
  }

  getTopicIds():string[] {
    return this.topicIds;
  }

}
