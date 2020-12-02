import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  title = 'SWE 645 HW3';
  submitForm = async () => {
    const res = await fetch('http://localhost:8080/swe645_hw3_war/confirmation');
    // const data = await res.json();
    const data = await res.text();
    console.log('submitForm', data);
  }
  dockerSend = async () => {
    const res = await fetch('http://localhost:8080/swe645_hw3-1.0/confirmation');
    // const data = await res.json();
    const data = await res.text();
    console.log('dockerSend', data);
  }
  dockerSendHost = async () => {
    const res = await fetch('http://swe645-backend:8080/swe645_hw3-1.0/confirmation');
    const data = await res.text();
    console.log('dockerSendHost', data);
  }
  sweBackend = async () => {
    const res = await fetch('swe645-backend:8080/swe645_hw3-1.0/confirmation');
    const data = await res.text();
    console.log('sweBackend', data);
  }
  sayHello = () => {
    console.log('Hello world');
  }
}
