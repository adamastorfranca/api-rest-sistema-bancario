import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { BemVindoComponent } from './bem-vindo/bem-vindo.component';
import { CadastroComponent } from './cadastro/cadastro.component';
import { DepositoComponent } from './deposito/deposito.component';
import { HomeComponent } from './home.component';
import { InicialComponent } from './inicial/inicial.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {
    path:'', component: HomeComponent,
    children: [
      { path: '', component: InicialComponent },
      { path: 'login', component: LoginComponent },
      { path: 'cadastro', component: CadastroComponent },
      { path: 'deposito', component: DepositoComponent },
      { path: 'bem-vindo', component: BemVindoComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
