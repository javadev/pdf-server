import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPdffile, Pdffile } from 'app/shared/model/pdffile.model';
import { PdffileService } from './pdffile.service';
import { PdffileComponent } from './pdffile.component';
import { PdffileDetailComponent } from './pdffile-detail.component';
import { PdffileUpdateComponent } from './pdffile-update.component';

@Injectable({ providedIn: 'root' })
export class PdffileResolve implements Resolve<IPdffile> {
  constructor(private service: PdffileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPdffile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pdffile: HttpResponse<Pdffile>) => {
          if (pdffile.body) {
            return of(pdffile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pdffile());
  }
}

export const pdffileRoute: Routes = [
  {
    path: '',
    component: PdffileComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Pdffiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PdffileDetailComponent,
    resolve: {
      pdffile: PdffileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pdffiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PdffileUpdateComponent,
    resolve: {
      pdffile: PdffileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pdffiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PdffileUpdateComponent,
    resolve: {
      pdffile: PdffileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pdffiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
