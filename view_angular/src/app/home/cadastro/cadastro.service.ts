import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IConta } from '../../interfaces/conta';

@Injectable({
  providedIn: 'root'
})
export class CadastroService {

  api = environment.api;
  endpoint = 'contas';

  constructor(private http: HttpClient) { }

  cadastrar(conta: IConta): Observable<IConta> {
    return this.http.post<IConta>(`${this.api}/${this.endpoint}/cadastrar`, conta);
  }

}