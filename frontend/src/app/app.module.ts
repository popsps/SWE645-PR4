import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from '@angular/common/http';
import { SurveyComponent } from './component/survey/survey.component';
import { NavigationComponent } from './component/navigation/navigation.component';
import { HomeComponent } from './component/home/home.component';
import { ListSurveysComponent } from './component/list-surveys/list-surveys.component';
import { AcknowledgmentComponent } from './component/acknowledgment/acknowledgment.component';
import {FormsModule} from '@angular/forms';
import { StudentComponent } from './component/student/student.component';

@NgModule({
  declarations: [
    AppComponent,
    SurveyComponent,
    NavigationComponent,
    HomeComponent,
    ListSurveysComponent,
    AcknowledgmentComponent,
    StudentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
