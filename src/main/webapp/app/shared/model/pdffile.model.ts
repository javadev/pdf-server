import { IUser } from 'app/core/user/user.model';

export interface IPdffile {
  id?: number;
  name?: string;
  dataContentType?: string;
  data?: any;
  user?: IUser;
}

export class Pdffile implements IPdffile {
  constructor(public id?: number, public name?: string, public dataContentType?: string, public data?: any, public user?: IUser) {}
}
