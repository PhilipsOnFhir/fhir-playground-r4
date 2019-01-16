import { Component } from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {$} from 'protractor';
import {disconnect} from 'cluster';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  private stompClient: any;
  connected = false;

  connect() {
    const socket = new SockJS('http://localhost:9080//fhicast/websocket');
    this.stompClient = Stomp.Stomp.over( socket);

    const _this = this;
    console.log('connect');
    this.stompClient.connect({}, function (frame) {
      _this.connected = true;
      console.log('Connected: ' + frame);

      console.log('subscribe');
      _this.stompClient.subscribe('/server/demo/patient-open', function (hello) {
        console.log('subscribed');
        console.log( hello);

      });
    });
  }

  updatePatient1() {
    const message = { 'patient' : '1234' };
    this.stompClient.send( '/server/demo/patient' , {}, JSON.stringify(message ));
  }

  updatePatient2() {
    const message = { 'patient' : '4321' };
    this.stompClient.send( '/server/demo/patient' , {}, JSON.stringify(message ));
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    this.connected = false;
    console.log('Disconnected!');
  }

}
