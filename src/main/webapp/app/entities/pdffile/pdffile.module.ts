import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PdfserverSharedModule } from 'app/shared/shared.module';
import { PdffileComponent } from './pdffile.component';
import { PdffileDetailComponent } from './pdffile-detail.component';
import { PdffileUpdateComponent } from './pdffile-update.component';
import { PdffileDeleteDialogComponent } from './pdffile-delete-dialog.component';
import { pdffileRoute } from './pdffile.route';

@NgModule({
  imports: [PdfserverSharedModule, RouterModule.forChild(pdffileRoute)],
  declarations: [PdffileComponent, PdffileDetailComponent, PdffileUpdateComponent, PdffileDeleteDialogComponent],
  entryComponents: [PdffileDeleteDialogComponent]
})
export class PdfserverPdffileModule {}
