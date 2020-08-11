import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PdfserverTestModule } from '../../../test.module';
import { PdffileUpdateComponent } from 'app/entities/pdffile/pdffile-update.component';
import { PdffileService } from 'app/entities/pdffile/pdffile.service';
import { Pdffile } from 'app/shared/model/pdffile.model';

describe('Component Tests', () => {
  describe('Pdffile Management Update Component', () => {
    let comp: PdffileUpdateComponent;
    let fixture: ComponentFixture<PdffileUpdateComponent>;
    let service: PdffileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PdfserverTestModule],
        declarations: [PdffileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PdffileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PdffileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PdffileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pdffile(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pdffile();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
