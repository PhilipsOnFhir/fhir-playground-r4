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
  destination = '/app/fhircast/demo/open-patient-chart' ;

  connect() {
    const socket = new SockJS('http://localhost:9080//fhircast/websocket');
    this.stompClient = Stomp.Stomp.over( socket);

    const _this = this;
    console.log('connect');
    this.stompClient.connect({}, function (frame) {
      _this.connected = true;
      console.log('Connected: ' + frame);

      console.log('subscribe to ' + _this.destination);
      _this.stompClient.subscribe(_this.destination, function (hello) {
        console.log('subscribed');
        console.log( hello);

      });
    });
  }

  updatePatient1() {
    // {"timestamp":"Wed Jan 16 18:02:27 EST 2019","id":"f27c0d34-ee84-4ce6-a27f-2ac4bf8279e7","event":{"context":[{"key":"patient","resource":"{\"resourceType\":\"Patient\",\"id\":\"6788\"}"}],"hub.topic":"demo","hub.event":"SWITCH_PATIENT_CHART"}}
    // {"context":[{"key":"patient","resource":"{\"resourceType\":\"Patient\",\"id\":\"6788\"}"}],"hub.topic":"demo","hub.event":"SWITCH_PATIENT_CHART"}
    // {"context":[{"key":"patient","resource":"{\"resourceType\":\"Patient\",\"id\":\"1111111\"}"}],"hub.topic":"demo","hub.event":"switch-patient-chart"}
    // const message = { 'hub.topic' : 'demo', 'hub.event': 'open-patient-chart' };
    const message = {
        'hub.topic': 'demo',
        'hub.event': 'switch-patient-chart',
        'context': [{'key': 'patient', 'resource': { 'resourceType': 'Patient', 'id': '9999'}  } ]
      };
    this.stompClient.send( this.destination , {}, JSON.stringify(message ));
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
