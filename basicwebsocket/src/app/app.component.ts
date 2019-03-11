import {Component, OnInit} from '@angular/core';
import {WebsocketService} from "./websocket.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'app';
  events = [];

  constructor(private chatService: WebsocketService) {

  }
  ngOnInit(): void {
    this.chatService.connect("ws://localhost:9080/fhircast/demo/websocket").subscribe( (message)=> {
      console.log("Response from websocket: " + message.data);
      console.log( JSON.stringify(message.data) );
      this.events.unshift( message.data );
    });
  }
}
