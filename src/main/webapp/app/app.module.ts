import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PdfserverSharedModule } from 'app/shared/shared.module';
import { PdfserverCoreModule } from 'app/core/core.module';
import { PdfserverAppRoutingModule } from './app-routing.module';
import { PdfserverHomeModule } from './home/home.module';
import { PdfserverEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PdfserverSharedModule,
    PdfserverCoreModule,
    PdfserverHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PdfserverEntityModule,
    PdfserverAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class PdfserverAppModule {}
