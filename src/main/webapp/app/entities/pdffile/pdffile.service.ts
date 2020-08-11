import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPdffile } from 'app/shared/model/pdffile.model';

type EntityResponseType = HttpResponse<IPdffile>;
type EntityArrayResponseType = HttpResponse<IPdffile[]>;

@Injectable({ providedIn: 'root' })
export class PdffileService {
  public resourceUrl = SERVER_API_URL + 'api/pdffiles';

  constructor(protected http: HttpClient) {}

  create(pdffile: IPdffile): Observable<EntityResponseType> {
    return this.http.post<IPdffile>(this.resourceUrl, pdffile, { observe: 'response' });
  }

  update(pdffile: IPdffile): Observable<EntityResponseType> {
    return this.http.put<IPdffile>(this.resourceUrl, pdffile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPdffile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPdffile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
