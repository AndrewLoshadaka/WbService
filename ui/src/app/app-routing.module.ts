import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {FeedbacksComponent} from "./feedbacks/feedbacks.component";
import {TemplateComponent} from "./template/template.component";
import {ProductComponent} from "./product/product.component";

const routes: Routes = [
  
  {path: 'list', component: FeedbacksComponent},
  {path: 'templates', component: TemplateComponent},
  {path: 'product', component: ProductComponent},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
