import {Component, OnInit} from '@angular/core';

type StudentModel = {
  studentID: number;
  fName: string;
  lName: string;
};


@Component({
  selector: 'app-list-surveys',
  templateUrl: './list-surveys.component.html',
  styleUrls: ['./list-surveys.component.scss']
})


export class ListSurveysComponent implements OnInit {

  students: StudentModel[] = null;

  // heroes: StudentModel[] = [{fName: 'Goo', lName: 'Ravioli'}, {fName: 'Goo', lName: 'Ravioli'}];

  constructor() {
    this.getStudents().then(res => this.students = res);
  }

  async getStudents(): Promise<any> {
    try {
      const res = await fetch('/ListAllStudents');
      const data = await res.json();
      console.log(data);
      return data;
    } catch (err) {
      console.log('err: ' + err);
      return null;
    }
  }

  ngOnInit(): void {
  }

}
