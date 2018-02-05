import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {AppRoutingModule} from './app.routing';


import { AppComponent } from './app.component';
import { CustomerComponent } from './customer/customer.component';
import { CatalogComponent } from './catalog/catalog.component';
import {ApiService, CatalogService, CustomerService, JwtService, OrderService, UserService} from './shared/services';
import { AuthComponent } from './auth/auth.component';


@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    CatalogComponent,
    AuthComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [
    ApiService,
    CatalogService,
    CustomerService,
    JwtService,
    OrderService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
