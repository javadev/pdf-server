import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPdffile, Pdffile } from 'app/shared/model/pdffile.model';
import { PdffileService } from './pdffile.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-pdffile-update',
  templateUrl: './pdffile-update.component.html'
})
export class PdffileUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(240)]],
    data: [],
    dataContentType: [],
    user: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected pdffileService: PdffileService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pdffile }) => {
      this.updateForm(pdffile);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(pdffile: IPdffile): void {
    this.editForm.patchValue({
      id: pdffile.id,
      name: pdffile.name,
      data: pdffile.data,
      dataContentType: pdffile.dataContentType,
      user: pdffile.user
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('pdfserverApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pdffile = this.createFromForm();
    if (pdffile.id !== undefined) {
      this.subscribeToSaveResponse(this.pdffileService.update(pdffile));
    } else {
      this.subscribeToSaveResponse(this.pdffileService.create(pdffile));
    }
  }

  private createFromForm(): IPdffile {
    return {
      ...new Pdffile(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dataContentType: this.editForm.get(['dataContentType'])!.value,
      data: this.editForm.get(['data'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPdffile>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
