import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PdfserverTestModule } from '../../../test.module';
import { PdffileDetailComponent } from 'app/entities/pdffile/pdffile-detail.component';
import { Pdffile } from 'app/shared/model/pdffile.model';

describe('Component Tests', () => {
  describe('Pdffile Management Detail Component', () => {
    let comp: PdffileDetailComponent;
    let fixture: ComponentFixture<PdffileDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ pdffile: new Pdffile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PdfserverTestModule],
        declarations: [PdffileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PdffileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PdffileDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load pdffile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pdffile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
