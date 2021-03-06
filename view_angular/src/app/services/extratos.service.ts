import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { IExtrato } from '../interfaces/extrato';
import { IExtratoEspecifico } from '../interfaces/extrato-especifico';

@Injectable({
  providedIn: 'root'
})
export class ExtratosService {

  api = environment.api;
  endpoint = 'transacoes';

  transacoes: IExtrato[] = [];

  mes: string = '';
  ano: string = '';
  dataInicio: string = '';
  dataFinal: string = '';

  constructor(
    private http: HttpClient
  ) { }

  extratoTodoPeriodo(agencia: string, numero: string): Observable<IExtrato[]> {
    return this.http.get<IExtrato[]>(`${this.api}/${this.endpoint}/consultar-extrato/${agencia}/${numero}/`);
  }

  extratoPorMesAno(agencia: string, numero: string, mes: number, ano: number): Observable<IExtrato[]> {
    return this.http.get<IExtrato[]>(`${this.api}/${this.endpoint}/extrato-por-mes-ano/${agencia}/${numero}/${mes}/${ano}/`);
  }

  extratoPeriodoEspecifico(periodo: IExtratoEspecifico): Observable<IExtrato[]> {
    return this.http.post<IExtrato[]>(`${this.api}/${this.endpoint}/por-periodo-especifico`, periodo);
  }

}
