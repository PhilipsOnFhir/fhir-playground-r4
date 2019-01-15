import {Injectable, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FhirCastRestService {
  private topicUrl: string ;
  private status: string;
  private context: string;

  constructor(private activatedRoute: ActivatedRoute, private http: HttpClient) {
    this.status = 'UNINITIALIZED';
    // Note: Below 'queryParams' can be replaced with 'params' depending on your requirements
    this.activatedRoute.queryParams.subscribe(params => {
      console.log(params);
      this.topicUrl = params['hub'];
      console.log(this.topicUrl);
    });

  }

  updateContext(): Observable<string> {
    return new Observable<string>(observer => {
        this.http.get<string>(this.topicUrl + '/context').subscribe(
          context => {
            console.log('context retrieved');
            console.log(context);
            this.context = context;
            this.status = 'UPDATED';
            observer.next(context);
            observer.complete();
          }, err => {
            console.log(err);
            this.status = 'ERROR';
            observer.error(err);
          });
      });
  }

  getStatus() {
    return this.status;
  }

  getContext() {
    return this.context;
  }

  getTopicUrl() {
    return this.topicUrl;
  }
}
