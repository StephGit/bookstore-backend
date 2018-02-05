import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {CatalogComponent} from './catalog/catalog.component';
import {AuthGuardService} from './shared/services/auth-guard.service';


const routes: Routes = [
  {path: 'catalog', component: CatalogComponent, canActivate: [AuthGuardService]},
  // {path: '', component: MainComponent, canActivate: [AuthGuardService]},
  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
