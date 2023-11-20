import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "./confirmationdialog.component";
import { Router } from '@angular/router';
@Component({
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.css'],
})
export class TemplateComponent {
  constructor(private http: HttpClient, private dialog: MatDialog, private router: Router) {
    this.getAllTemplates()
  }
  templates: any[] = [];
  templatesData: TempComponent[] = [];
  root = "http://192.168.208.235:9092/templates";
  newTemplateText: string = "";
  newAnswerText: string = "";

  getAllTemplates(){
    this.http.get(this.root + '/all').subscribe(
      (v: any) => {
        this.templates = v;
        this.templates.forEach((template: any) => {
          this.templatesData.push({
            id: template.id,
            template: template.template,
            answer: template.answer
          });
        });
      })
  }

  saveEditTemplate(index: number){
    this.templates[index].template = this.templatesData[index].template; // Обновляем значение шаблона
    this.templates[index].answer = this.templatesData[index].answer;


    const postData = {
      id: this.templates[index].id,
      template: this.templates[index].template,
      answer: this.templates[index].answer
    };

    this.http.post('http://192.168.208.235:9092/templates/update?id=' + this.templates[index].id, postData).subscribe(
      (response) => {
        console.log('Ok!' + JSON.stringify(response));
      },
      (error) => {
        console.log('error! ' + JSON.stringify(error));
      }
    )
  }

  deleteTemplate(index: number) {
    this.http.post(this.root + '/delete', {id: this.templatesData[index].id}).subscribe(
      (response) => {
        console.log('Ok!' + JSON.stringify(response));
      },
      (error) => {
        console.log('error! ' + JSON.stringify(error));
      }
    )
    this.templatesData.splice(index, 1); // Удаляем элемент из массива
  }

  saveTemplate() {
    this.http.post(this.root + '/add', {template: this.newTemplateText, answer: this.newAnswerText}).subscribe(
      (response) => {
        console.log('Ok!' + JSON.stringify(response));
        this.templates = [];
        this.templatesData = [];
        this.getAllTemplates();
        this.newAnswerText = this.newTemplateText = ' ';
      },
      (error) => {
        console.log('error! ' + JSON.stringify(error));
      }
    );
  }

  openDeleteConfirmationDialog(index: number) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { index },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.deleteTemplate(index);
      }
    });
  }

  homeRedirect(){
    this.router.navigate(['/']).then(r => {});
  }
}
export class TempComponent{
  id: any;
  template: any;
  answer: any;
}


