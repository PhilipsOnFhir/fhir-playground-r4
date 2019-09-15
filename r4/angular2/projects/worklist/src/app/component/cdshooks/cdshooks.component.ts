import {Component, Input, OnInit} from '@angular/core';
import {ConnectorService} from "../../service/connector.service";

@Component({
  selector: 'app-cdshooks',
  templateUrl: './cdshooks.component.html',
  styleUrls: ['./cdshooks.component.css']
})
export class CdshooksComponent implements OnInit {
  @Input() launch:string;

  constructor( private secHttp: ConnectorService) { }

  ngOnInit() {
    let url = "http://localhost:9401/api/fhircast/launch/"+this.launch+"/cdshooks";
    this.secHttp.get(url).subscribe( res => {console.log(res)});
  }

}
