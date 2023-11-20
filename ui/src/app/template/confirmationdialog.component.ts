import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  template: `
    <h1 mat-dialog-title>Подтвердите удаление</h1>
    <div mat-dialog-content>
      Вы уверены, что хотите удалить этот элемент?
    </div>
    <div mat-dialog-actions>
      <button mat-button [mat-dialog-close]="false">Отмена</button>
      <button mat-button [mat-dialog-close]="true">Удалить</button>
    </div>
  `,
})
export class ConfirmationDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
}
