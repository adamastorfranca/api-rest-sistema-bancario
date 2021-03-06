package br.com.adamastor.banco.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.adamastor.banco.model.dto.ContaBancariaDTO;
import br.com.adamastor.banco.model.dto.ContaBancariaEdicaoDTO;
import br.com.adamastor.banco.model.dto.ContaBancariaLogadaDTO;
import br.com.adamastor.banco.model.dto.TransferenciaBancariaDTO;
import br.com.adamastor.banco.model.entity.Cliente;
import br.com.adamastor.banco.model.entity.ContaBancaria;
import br.com.adamastor.banco.model.entity.TipoTransacao;
import br.com.adamastor.banco.model.exception.AplicacaoException;
import br.com.adamastor.banco.model.exception.ExceptionValidacoes;
import br.com.adamastor.banco.model.form.AtualizacaoContaForm;
import br.com.adamastor.banco.model.form.CadastroContaForm;
import br.com.adamastor.banco.model.repository.ClienteRepository;
import br.com.adamastor.banco.model.repository.ContaBancariaRepository;

@Service
public class ContaBancariaService {

	@Autowired
	private ContaBancariaRepository contaBancariaRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private AutorizacaoService autorizacaoService;
	@Autowired
	private TransacaoService transacaoService;

	@Transactional
	public ContaBancaria criarConta(Cliente cliente, CadastroContaForm form) {
		ContaBancaria novaConta = new ContaBancaria();
		novaConta.setAgencia("0001");
		novaConta.adicionarAutorizacao(autorizacaoService.buscar("CLIENTE"));
		novaConta.setCliente(cliente);
		String senhaHash = new BCryptPasswordEncoder().encode(form.getSenha());
		novaConta.setSenha(senhaHash);
		Optional<ContaBancaria> resultado = contaBancariaRepository.findById(1L);
		Integer numeroUltima = 0;
		Integer digitoUltima = 0;
		if (!resultado.isPresent()) {
			novaConta.setNumeroConta("0001-1");
			contaBancariaRepository.save(novaConta);
		} else {
			Long ultimoId = contaBancariaRepository.buscarUltimo();
			Optional<ContaBancaria> resultado2 = contaBancariaRepository.findById(ultimoId);
			if (resultado2.isPresent()) {
				String[] contaDigito = resultado2.get().getNumeroConta().split("-");
				numeroUltima = Integer.parseInt(contaDigito[0]);
				digitoUltima = Integer.parseInt(contaDigito[1]);
				if (digitoUltima < 9) {
					digitoUltima += 1;
				} else {
					numeroUltima += 1;
					digitoUltima = 1;
				}
				novaConta.setNumeroConta(String.valueOf(String.format("%04d-%d", numeroUltima, digitoUltima)));
				novaConta.setLogin(novaConta.getAgencia() + novaConta.getNumeroConta());
				contaBancariaRepository.save(novaConta);
			}
		}
		return novaConta;
	}
	
	public List<ContaBancariaDTO> buscarTodasContas() {
		return ContaBancariaDTO.converter(contaBancariaRepository.findAll());
	}

	@Transactional(rollbackFor = Exception.class)
	public ContaBancariaDTO cadastrar(CadastroContaForm form) {
		if (!form.getSenha().equals(form.getSenhaConfirmar())) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_SENHAS_NAO_CORRESPONDEM);
		}
		Cliente c = clienteService.cadastrar(form.getNome(), form.getCpf(), form.getEmail(), form.getTelefone(),
				form.getDataNascimento());
		ContaBancaria conta = criarConta(c, form);
		return new ContaBancariaDTO(conta);
	}

	public ContaBancaria consultarConta(String agencia, String numero) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByAgenciaAndNumeroConta(agencia, numero);

		if (!resultado.isPresent()) {
			return null;
		}

		return resultado.get();
	}

	@Transactional(rollbackFor = Exception.class)
	public void depositar(String agencia, String numeroConta, double valor) {
		ContaBancaria conta = consultarConta(agencia, numeroConta);

		conta.setSaldo(conta.getSaldo() + valor);

		contaBancariaRepository.save(conta);
		transacaoService.salvar(TipoTransacao.DEPOSITO, valor, null, conta);
	}

	@Transactional(rollbackFor = Exception.class)
	public void sacar(String agencia, String numeroConta, double valor) {
		ContaBancaria conta = consultarConta(agencia, numeroConta);

		if (conta.getSaldo() < valor) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_SALDO_INSUFICIENTE);
		}

		conta.setSaldo(conta.getSaldo() - valor);

		contaBancariaRepository.save(conta);
		transacaoService.salvar(TipoTransacao.SAQUE, valor, conta, null);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public void transferir(TransferenciaBancariaDTO dto) {
		ContaBancaria contaOrigem = consultarConta(dto.getAgenciaOrigem(), dto.getNumeroContaOrigem());
		ContaBancaria contaDestino = consultarConta(dto.getAgenciaDestino(), dto.getNumeroContaDestino());
		
		if (contaOrigem.getSaldo() < dto.getValor()) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_SALDO_INSUFICIENTE); 
		}
		
		contaOrigem.setSaldo(contaOrigem.getSaldo() - dto.getValor());
		contaDestino.setSaldo(contaDestino.getSaldo() + dto.getValor());
		
		contaBancariaRepository.save(contaOrigem);
		contaBancariaRepository.save(contaDestino);
		transacaoService.salvar(TipoTransacao.TRANSFERENCIA, dto.getValor(), contaOrigem, contaDestino);
	}

	public ContaBancariaLogadaDTO consultarContaLogadaDTO(String agencia, String numero) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByAgenciaAndNumeroConta(agencia, numero);

		if (!resultado.isPresent()) {
			return null;
		}

		return new ContaBancariaLogadaDTO(resultado.get());
	}

	public ContaBancariaDTO consultarContaDTO(String agencia, String numero) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByAgenciaAndNumeroConta(agencia, numero);

		if (!resultado.isPresent()) {
			return null;
		}

		return new ContaBancariaDTO(resultado.get());
	}

	public ContaBancariaDTO consultarContaPorCpfDTO(String cpf) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByClienteCpf(cpf);

		if (!resultado.isPresent()) {
			return null;
		}

		return new ContaBancariaDTO(resultado.get());
	}

	public Double consultarSaldo(String agencia, String numero) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByAgenciaAndNumeroConta(agencia, numero);

		if (!resultado.isPresent()) {
			return null;
		}

		return resultado.get().getSaldo();
	}
	
	public ContaBancaria buscarPorId(Long id) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findById(id);
		if(resultado.isPresent()) {
			return resultado.get();
		}
		return null;
	}

	
	@Transactional(rollbackFor = Exception.class)
	public Boolean deletarPorId(Long id) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findById(id);
		if(resultado.isPresent()) {
			ContaBancaria c = resultado.get();
			Cliente cl = c.getCliente();
			cl.setTemConta(false);
			clienteRepository.save(cl);
			contaBancariaRepository.delete(resultado.get());
			return true;
		}
		return false;
	}
	
	public ContaBancariaEdicaoDTO buscarPorIdDTO(Long id) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findById(id);
		if(resultado.isPresent()) {
			return new ContaBancariaEdicaoDTO(resultado.get());
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public ContaBancariaDTO abrirConta(CadastroContaForm form) {
		if (!form.getSenha().equals(form.getSenhaConfirmar())) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_SENHAS_NAO_CORRESPONDEM);
		}
		Optional<Cliente> resultado = clienteRepository.findByCpf(form.getCpf());
		if(resultado.isPresent()) {
			Cliente c = resultado.get();
			c.setTemConta(true);
			clienteRepository.save(c);
			ContaBancaria conta = criarConta(c, form);
			return new ContaBancariaDTO(conta);
		}
		return null;
	}

	public boolean deletar(String agencia, String numero) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findByAgenciaAndNumeroConta(agencia, numero);
		if (resultado.isPresent()) {
			ContaBancaria conta = resultado.get();
			contaBancariaRepository.delete(conta);
			return true;
		}
		return false;	
	}
	
	public Boolean atualizar(AtualizacaoContaForm form) {
		Optional<ContaBancaria> resultado = contaBancariaRepository.findById(form.getId());
		if(resultado.isPresent()) {
			ContaBancaria conta = resultado.get();
			conta.setAgencia(form.getAgencia());
			conta.setNumeroConta(form.getNumero());
			String senhaHash = new BCryptPasswordEncoder().encode(form.getSenha());
			conta.setSenha(senhaHash);
			conta.setLogin(form.getAgencia()+form.getNumero());
			contaBancariaRepository.save(conta);
		}
				
		return null;
	}
}
