import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ClientesService } from 'src/app/services/clientes.service';
import { Cliente } from "src/app/models/cliente.model";

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css']
})
export class CadastroComponent implements OnInit {

@Output() aoCadastrar = new EventEmitter<any>();

  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  dataNascimento: string;
  senha: string;
  senhaConfirmar: string;

  clientes: Cliente[];

  constructor(private clientesService: ClientesService) { }

  ngOnInit(): void {
  }

  cadastrar(){
    const cadastroEmitir: Cliente = {nome: this.nome, cpf: this.cpf, email: this.email, telefone: this.telefone, dataNascimento: this.dataNascimento, senha: this.senha, senhaConfirmar: this.senhaConfirmar};
    this.clientesService.cadastrar(cadastroEmitir).subscribe(
      (resultado) => {
        console.log(resultado);
      },
      (error) => console.error(error)
    );
  }

}