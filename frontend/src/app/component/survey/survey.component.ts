import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

type StudentModel = {
  studentID: number;
  fName: string;
  lName: string;
  email: string;
  street: string;
  zipcode: number;
  city: string;
  state: string;
  phone: string;
  fDate: string;
  recommend: string;
  howknow: string;
  comments: string;
  _students: boolean;
  _location: boolean;
  _campus: boolean;
  _atmosphere: boolean;
  _dormrooms: boolean;
  _sports: boolean;
};

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.scss']
})
export class SurveyComponent implements OnInit {
  student: StudentModel = {
    _atmosphere: false,
    _campus: false,
    _dormrooms: false,
    _location: false,
    _sports: false,
    _students: false,
    comments: '',
    howknow: '',
    recommend: '',
    city: '',
    email: '',
    fName: '',
    lName: '',
    phone: '',
    state: '',
    street: '',
    fDate: '',
    studentID: undefined,
    zipcode: undefined
  };

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  submit(): void {
    console.log('----------------submit----------------');
    console.log(this.student);
    const options = {
      method: 'POST',
      body: JSON.stringify(this.student),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    };
    fetch('/FormServlet', options)
      .then(res => res.json())
      .then(res => {
        console.log(res);
        // this.router.navigate(['/acknowledgment', {state: this.student}]).then(r => console.log('navigation successful'));
        this.router.navigate(['/acknowledgment'],
          {state: this.student}).then(r => console.log('navigation successful'));
      })
      .catch(err => {
        console.log(err);
        this.router.navigate(['/acknowledgment']);
      });
  }

}
