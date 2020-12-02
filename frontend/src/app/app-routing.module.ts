import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SurveyComponent} from './component/survey/survey.component';
import {HomeComponent} from './component/home/home.component';
import {ListSurveysComponent} from './component/list-surveys/list-surveys.component';
import {AcknowledgmentComponent} from './component/acknowledgment/acknowledgment.component';
import {StudentComponent} from './component/student/student.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'survey', component: SurveyComponent},
  {path: 'listStudents', component: ListSurveysComponent},
  {path: 'students/:id', component: StudentComponent},
  {path: 'acknowledgment', component: AcknowledgmentComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
