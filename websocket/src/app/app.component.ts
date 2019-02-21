import {Component} from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'com.github.philipsonfhir.fhircast.app';
  private stompClient: any;
  connected = false;
  destOpen = '/app/fhircast/demo/open-patient-chart' ;
  destClose = '/app/fhircast/demo/close-patient-chart' ;
  destSwitch = '/app/fhircast/demo/switch-patient-chart' ;
  receiveOpen = '/hub/fhircast/demo/open-patient-chart' ;
  receiveClose = '/hub/fhircast/demo/close-patient-chart' ;
  receiveSwitch = '/hub/fhircast/demo/switch-patient-chart' ;
  events = [];

  connect() {
    const socket = new SockJS('http://localhost:9080/fhircast/websocket');
    this.stompClient = Stomp.Stomp.over( socket);

    const _this = this;
    console.log('connect');
    this.stompClient.connect({}, function (frame) {
      _this.connected = true;
      console.log('Connected: ' + frame);

      console.log('subscribe to ' + _this.receiveOpen);
      _this.stompClient.subscribe(_this.receiveOpen, function (hello) {
        console.log('Open event received');
        console.log( hello);
        console.log( JSON.parse(hello.body) );
        _this.events.unshift( JSON.parse(hello.body));

      });
      console.log('subscribe to ' + _this.receiveSwitch);
      _this.stompClient.subscribe(_this.receiveSwitch, function (hello) {
        console.log('Switch event received');
        console.log( hello);
        console.log( JSON.parse(hello.body) );
        _this.events.unshift( JSON.parse(hello.body));

      });
      console.log('subscribe to ' + _this.receiveClose);
      _this.stompClient.subscribe(_this.receiveClose, function (hello) {
        console.log('Close event received');
        console.log( hello);
        console.log( JSON.parse(hello.body) );
        _this.events.unshift( JSON.parse(hello.body));

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
        'hub.event': 'open-patient-chart',
        'context': [{'key': 'patient', 'resource': { 'resourceType': 'Patient', 'id': '9999'}  } ]
      };
    console.log(message);
    this.stompClient.send( this.destOpen , {}, JSON.stringify(message ));
  }

  updatePatient2() {
    const message = {
      'hub.topic': 'demo',
      'hub.event': 'open-patient-chart',
      'context': [{'key': 'patient', 'resource': { 'resourceType': 'Patient', 'id': '88888'}  } ]
    };
    console.log(message);
    this.stompClient.send( this.destOpen , {}, JSON.stringify(message ));
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    this.connected = false;
    console.log('Disconnected!');
  }

}
