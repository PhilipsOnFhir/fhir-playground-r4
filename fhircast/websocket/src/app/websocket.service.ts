import {Injectable} from "@angular/core";
import * as Rx from 'rxjs';
import {Observable, Observer, Subject} from 'rxjs';

@Injectable()
export class WebsocketService {
  public messages: Subject<MessageEvent>;
  private subject: Rx.Subject<MessageEvent> ;
  public ws: any;

  constructor() {
  }

  public connect(url: string): Subject<any> {
    console.log(url);
    if ( !this.subject ){
      this.subject =this.create(url);
    }
    return this.subject;
  }

  private create(url: string): Subject<MessageEvent> {
    this.ws = new WebSocket(url);
    const observable = Observable.create(
      (obs: Observer<any>) => {
        this.ws.onmessage = obs.next.bind(obs);
        this.ws.onerror = obs.error.bind(obs);
        this.ws.onclose = obs.complete.bind(obs);
        return this.ws.close.bind(this.ws);
      });//.share();

    const observer = {
      next: (data: Object) => {
        if (this.ws.readyState === WebSocket.OPEN) {
          this.ws.send(data);
        }
      }
    };
    console.log(this.ws);
    return Rx.Subject.create(observer, observable);
  }

  public close() {
    if (this.ws) {
      this.ws.close();
      this.subject = null;
    }
  }
}

// import { Injectable } from "@angular/core";
// import * as socketIo from 'socket.io-client';
// import {Observable, Observer} from "rxjs";
//
// @Injectable()
// export class WebsocketService {
//
//   socket;
//   observer: Observer<any>;
//   constructor() {
//
//   }
//
//   public connect(url: string): Observable<any> {
//     console.log(url);
//     this.socket = socketIo("http://localhost:8080/fhircast/demo");
//
//     this.socket.on('data', (res) => {
//       this.observer.next(res.data);
//     });
//
//     return new Observable<any>(obs => {
//       this.observer = obs;
//     })
//   }
// }
//
