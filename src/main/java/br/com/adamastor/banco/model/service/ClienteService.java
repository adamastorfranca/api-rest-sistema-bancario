package br.com.adamastor.banco.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adamastor.banco.model.dto.ClienteDTO;
import br.com.adamastor.banco.model.entity.Cliente;
import br.com.adamastor.banco.model.repository.ClienteRepository;
import br.com.adamastor.banco.model.util.CpfUtil;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public ClienteDTO buscarClientePorCpf(String cpf) {
		boolean cpfValido = cpf != null && CpfUtil.validaCPF(cpf);

		if (!cpfValido) {
			return null;
		}

		Cliente c = clienteRepository.findByCpf(cpf);

		if (c == null) {
			return null;
		}
		
		ClienteDTO dto = new ClienteDTO(c);
		
		return dto;
	}
	
	  public List<ClienteDTO> buscarClientesPorNome(String nome){
		  List<Cliente> clientes = clienteRepository.findByNomeContains(nome);

		  if (clientes == null || clientes.isEmpty()) {
			  return null;
		  }

		  return ClienteDTO.converter(clientes);
	  }
}
