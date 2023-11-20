import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { CdkTableDataSourceInput } from '@angular/cdk/table';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})


export class ProductComponent{
  
    dataSource: Product[] = [];
    currentProducts: Product[] = [];
    displayedColumns: string[] = ['prGroup', 'wbDesign', 'size',  'tpStream', 'countOTK', 'countStock', 'countRoute'];
    

    startDate: Date;
    endDate: Date;
    tableState: number = 0; // 0 - wait data, 1 - ok, 2 - empty

    uniquePrGroup = new Set<any>;
    uniqueTpStream= new Set<any>;
    currentPrGroup = "не выбрано";
    currentTpStream = "не выбрано";

    constructor(
      private http: HttpClient, private router: Router) {
      this.endDate = new Date();
      const oneWeekAgo = new Date();
      oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);
      this.startDate = oneWeekAgo;
      this.getProduct();
    }

  

    getProduct(){
      this.tableState = 0;
      this.dataSource = [];
      this.currentPrGroup = "не выбрано";
      this.currentTpStream = "не выбрано";
      this.http.get('http://192.168.208.235:9081/stat/all?dateFrom=' + this.startDate.setHours(0,0,0,0) + '&dateTo=' + this.endDate.setHours(0,0,0,0))
      .subscribe((data: any) => {
        this.dataSource = data;
        this.sortTable();
        this.tableState = 1;
        this.currentProducts = this.dataSource;
        this.dataSource.forEach(v =>{
          this.uniquePrGroup.add('не выбрано');
          if(v.prGroup !== null)
            this.uniquePrGroup.add(v.prGroup);

          this.uniqueTpStream.add('не выбрано');
          this.uniqueTpStream.add(v.tpStream);
      })
      })
    }

    onDateRangeSelected(event: any) {
      if (event.value) {
        this.startDate = event.value.start;
        this.endDate = event.value.end;
        console.log(this.startDate, ' ', this.endDate);
      }
    }

    getCountOTK() :number{
      let count:number = 0;
      this.dataSource.forEach(data => {
        count += data.countOtk;
      })
      return count;
    }

    getCountStock() : number{
      let count:number = 0;
      this.dataSource.forEach(data => {
        count += data.countStock;
      })
      return count;
    }

    getCountRoute():number{
      let count:number = 0;
      this.dataSource.forEach(data => {
        count += data.countRoute;
      })
      return count;
    }

    sortProduct(tpGr: string, tpSt: string){
      this.currentPrGroup = tpGr;
      this.currentTpStream = tpSt;
      this.filterByPrGroup();
      if(this.dataSource.length == 0) this.tableState = 2;
    }

    filterByPrGroup() {
      this.dataSource = this.currentProducts.filter((product: Product) => {
        return (product.prGroup === this.currentPrGroup || this.currentPrGroup === "не выбрано") && 
        (product.tpStream === this.currentTpStream || this.currentTpStream === "не выбрано");
      });
    }

    sortTable(){
      this.dataSource.sort((a, b) => {
        if(a.wbDesign !== b.wbDesign){
          return a.wbDesign.localeCompare(b.wbDesign)
        }
        else {
          return a.size.localeCompare(b.size);
        }
      });
      //this.dataSource.sort((a:Product, b:Product) => );
    }

    homeRedirect(){
      this.router.navigate(['/']).then(r => {});
    }
}

export interface Product {
    countOtk: number;
    countStock: number;
    countRoute: number;
    size: string;
    wbDesign: string;
    prGroup: string;
    tpStream: string;
}
