import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-answer-dialog',
  templateUrl: './answer-dialog.component.html',
  styleUrls: ['./answer-dialog.component.css']
})
export class AnswerDialogComponent implements OnInit {
  images: any[];
  currentImageIndex: number = 0;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.images = data.images;
    this.currentImageIndex = data.index;
  }

  ngOnInit() {
    // Убедитесь, что currentImageIndex находится в пределах массива изображений
    this.currentImageIndex = Math.max(0, Math.min(this.currentImageIndex, this.images.length - 1));
  }

  showNextImage() {
    this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
  }

  showPreviousImage() {
    this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
  }
}
