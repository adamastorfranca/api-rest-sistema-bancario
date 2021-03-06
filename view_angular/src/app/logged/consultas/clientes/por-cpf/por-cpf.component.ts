import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AutenticacaoService } from 'src/app/services/autenticacao.service';

import { ContasService } from 'src/app/services/contas.service';

@Component({
  selector: 'app-por-cpf',
  templateUrl: './por-cpf.component.html',
  styleUrls: ['./por-cpf.component.css']
})
export class PorCpfComponent implements OnInit {

  formConsultaCliente: FormGroup = new FormGroup({
    cpf: new FormControl('', [Validators.required]),
  })

  cpfLogado: string = '';

  constructor(
    private contaService: ContasService,
    private authService: AutenticacaoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cpfLogado = this.authService.contaConectada.cpf;
  }

  consulta(): void {
    if (this.formConsultaCliente.valid){
      const cpf: string = this.formConsultaCliente.get('cpf')?.value;
      this.contaService.buscarPorCpf(cpf).subscribe((result) => {
        this.contaService.temp.agencia = result.agencia;
        this.contaService.temp.numero = result.numero;
        this.contaService.temp.nomeCliente = result.nomeCliente;
        this.router.navigate(['user/informacoes-conta']);
      },
      (error) => {
        alert('CPF informado não está cadastrado');
        console.error(error);
        this.formConsultaCliente.reset();
      });
    }
  }
}
