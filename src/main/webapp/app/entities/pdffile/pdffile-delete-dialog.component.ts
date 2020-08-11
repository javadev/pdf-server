import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPdffile } from 'app/shared/model/pdffile.model';
import { PdffileService } from './pdffile.service';

@Component({
  templateUrl: './pdffile-delete-dialog.component.html'
})
export class PdffileDeleteDialogComponent {
  pdffile?: IPdffile;

  constructor(protected pdffileService: PdffileService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pdffileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pdffileListModification');
      this.activeModal.close();
    });
  }
}
