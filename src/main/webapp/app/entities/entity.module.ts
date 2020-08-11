import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pdffile',
        loadChildren: () => import('./pdffile/pdffile.module').then(m => m.PdfserverPdffileModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PdfserverEntityModule {}
