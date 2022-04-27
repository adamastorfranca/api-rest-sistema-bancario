package br.com.adamastor.banco.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.adamastor.banco.model.dto.ContaBancariaDTO;
import br.com.adamastor.banco.model.dto.ContaBancariaLogadaDTO;
import br.com.adamastor.banco.model.dto.DepositoDTO;
import br.com.adamastor.banco.model.entity.ContaBancaria;
import br.com.adamastor.banco.model.form.CadastroContaForm;
import br.com.adamastor.banco.model.service.ContaBancariaService;

@RestController
@RequestMapping("rest/contas")
public class ContaBancariaRest {

	@Autowired
	private ContaBancariaService contaBancariaService;
	
	@PutMapping(value = "/deposito", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Void> depositar(@RequestBody DepositoDTO dto){
		contaBancariaService.depositar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/cadastrar", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ContaBancariaDTO> cadastrar(@RequestBody CadastroContaForm form) {
		ContaBancariaDTO dto = contaBancariaService.cadastrar(form);
		if(dto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}	
	
	@GetMapping(value = "/informacoes/{agencia}/{numeroConta}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ContaBancariaDTO> consultarConta(@PathVariable  String agencia, @PathVariable  String numeroConta){
		ContaBancariaDTO dto = contaBancariaService.consultarContaDTO(agencia, numeroConta);
		if (dto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@GetMapping(value = "/conta-logada/{agencia}/{numeroConta}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ContaBancariaLogadaDTO> consultarContaLogada(@PathVariable  String agencia, @PathVariable  String numeroConta){
		ContaBancariaLogadaDTO conta = contaBancariaService.consultarContaLogadaDTO(agencia, numeroConta);
		if (conta == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(conta, HttpStatus.OK);
	}
	
	@GetMapping(value = "/informacoes/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ContaBancariaDTO> consultarContaPorCpf(@PathVariable  String cpf){
		ContaBancariaDTO dto = contaBancariaService.consultarContaPorCpfDTO(cpf);
		if (dto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
//	@DeleteMapping(value = "/deletar/{agencia}/{numeroConta}")
//	public ResponseEntity<Void> deletar(@PathVariable String agencia, @PathVariable  String numeroConta){
//		boolean deletou = contaBancariaService.deletar(agencia, numeroConta);
//		if (!deletou) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@GetMapping(value = "/consultar-saldo/{agencia}/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<Double> consultarSaldo(@PathVariable  String agencia, @PathVariable  String numeroConta){
//		double saldo = contaBancariaService.consultarSaldo(agencia, numeroConta);
//
//		return new ResponseEntity<>(saldo, HttpStatus.OK);
//	}
//	
//	@GetMapping(value = "/consultar-contas-por-cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<List<ConsultaContaBancariaDTO>> consultarContasPorCpf(@PathVariable String cpf){
//		List<ConsultaContaBancariaDTO> contas = contaBancariaService.obterContasPorCpf(cpf);
//		if (contas == null || contas.isEmpty()) {
//			return new ResponseEntity<>(contas, HttpStatus.NO_CONTENT);
//		}
//		return new ResponseEntity<>(contas, HttpStatus.OK);
//	}
//	

//	
//	@PutMapping(value = "/saque", produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<Void> sacar(@RequestBody SaqueDTO dto){
//		contaBancariaService.sacar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@PutMapping(value = "/transferencia", produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<Void> transferir(@RequestBody TransferenciaBancariaDTO dto){
//		contaBancariaService.transferir(dto);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody ResponseEntity<List<ContaBancariaDTO>> buscarTodasContas() {
//		List<ContaBancariaDTO> dto = contaBancariaService.buscarTodasContas();
//		
//		if (dto == null) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
//		
//		return new ResponseEntity<>(dto, HttpStatus.OK);
//	}
}
