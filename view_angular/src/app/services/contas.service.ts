import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { IContaTemp } from '../interfaces/conta-temp';
import { ICadastro } from '../interfaces/cadastro';
import { IContaLogada } from '../interfaces/conta-logada';
import { IDepositoSaque } from '../interfaces/deposito-saque';
import { ITransferencia } from '../interfaces/transferencia';
import { IContaEdicao } from '../interfaces/conta-edicao';

@Injectable({
  providedIn: 'root'
})
export class ContasService {

  api = environment.api;
  endpoint = 'contas';

  temp: IContaTemp = {
    nomeCliente: '',
    agencia: '',
    numero: '',
    valor: 0
  }

  encaminhamentoTransacao: boolean = false;

  constructor(
    private http: HttpClient
  ) { }

  buscarTodas(): Observable<ICadastro[]> {
    return this.http.get<ICadastro[]>(`${this.api}/${this.endpoint}/`);
  }

  depositar(deposito: IDepositoSaque) {
    return this.http.put(`${this.api}/${this.endpoint}/deposito`, deposito);
  }

  sacar(saque: IDepositoSaque) {
    return this.http.put(`${this.api}/${this.endpoint}/saque`, saque);
  }

  transferir(transferencia: ITransferencia) {
    return this.http.put(`${this.api}/${this.endpoint}/transferencia`, transferencia);
  }

  cadastrar(conta: ICadastro): Observable<ICadastro> {
    return this.http.post<ICadastro>(`${this.api}/${this.endpoint}/cadastrar`, conta);
  }

  editar(conta: IContaEdicao) {
    return this.http.put(`${this.api}/${this.endpoint}/atualizar`, conta);
  }

  remover(id: number) {
    return this.http.delete(`${this.api}/${this.endpoint}/${id}`);
  }

  buscarPorId(id: number): Observable<ICadastro> {
    return this.http.get<ICadastro>(`${this.api}/${this.endpoint}/buscar-por-id/${id}`);
  }

  buscarPorAgenciaEhNumero(agencia: string, numero: string): Observable<IContaTemp> {
    return this.http.get<IContaTemp>(`${this.api}/${this.endpoint}/informacoes/${agencia}/${numero}/`);
  }

  buscarPorCpf(cpf: string): Observable<IContaTemp> {
    return this.http.get<IContaTemp>(`${this.api}/${this.endpoint}/informacoes/${cpf}/`);
  }

  consultarSaldo(agencia: string, numero: string): Observable<number> {
    return this.http.get<number>(`${this.api}/${this.endpoint}/consultar-saldo/${agencia}/${numero}/`);
  }

  buscarContaLogada(agencia: string, numero: string): Observable<IContaLogada> {
    return this.http.get<IContaLogada>(`${this.api}/${this.endpoint}/conta-logada/${agencia}/${numero}/`);
  }

  cadastro(conta: ICadastro): Observable<ICadastro> {
    return this.http.post<ICadastro>(`${this.api}/${this.endpoint}/cadastro/`, conta);
  }

  salvarInformacoes(cadastro:ICadastro): IContaTemp{
    this.temp.nomeCliente = cadastro.nomeCliente.valueOf();
    this.temp.agencia = cadastro.agencia.valueOf();
    this.temp.numero = cadastro.numero.valueOf();
    return this.temp;
  }

  limparContaTemp() {
    this.temp.nomeCliente = '';
    this.temp.agencia = '';
    this.temp.numero = '';
  }
}
