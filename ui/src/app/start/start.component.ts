import { Component} from "@angular/core";
import {Router, NavigationEnd} from "@angular/router";



@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})


export class StartComponent {

  isShowMenu = true;
  
  

  constructor(private router: Router) {
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.updatePageFlags(event.url);
      }
    });
  }
  name = "АЛ";

  feedbacks() {
    //this.router.navigate(['/']).then(r => {});
    this.router.navigate(['/list']).then(r => {});
  }

  answerTemplates(){
    this.router.navigate(['/templates']).then(r => {});
  }

  productNavigate(){
    this.router.navigate(['/product']).then(r => {});
  }

  homeRedirect(){
    this.router.navigate(['/']).then(r => {});
  }

  private updatePageFlags(url: string) {
    if(url === "/"){
      this.isShowMenu = true;
      //this.isFeedbacksPage = this.isAnswerTemplatesPage = this.isProductNavigatePage = true;
    }
    else {
      this.isShowMenu = false;
      //this.isFeedbacksPage = this.isAnswerTemplatesPage = this.isProductNavigatePage = false;
    }
    
  }
}
