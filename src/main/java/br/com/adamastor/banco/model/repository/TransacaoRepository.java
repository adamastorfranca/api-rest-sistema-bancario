package br.com.adamastor.banco.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.adamastor.banco.model.entity.ContaBancaria;
import br.com.adamastor.banco.model.entity.Transacao;

public interface TransacaoRepository extends CrudRepository<Transacao, Long>{
	
	@Query("SELECT t FROM Transacao t WHERE t.origem = :conta OR t.destino = :conta")
	List<Transacao> buscarTransacoesPorConta(ContaBancaria conta);
	
	@Query(value = "SELECT * FROM transacoes AS t WHERE t.fk_conta_origem_id = :id OR t.fk_conta_destino_id = :id", nativeQuery = true)
	List<Transacao> buscarTransacoesPorId(Long id);
	
	@Query("SELECT t FROM Transacao t WHERE "
			+ "t.origem = :conta OR "
			+ "t.destino = :conta AND "
			+ "t.dataHora >= :inicioPeriodo AND "
			+ "t.dataHora <= :finalPeriodo")
	List<Transacao> buscarTransacoesPorPeriodo(ContaBancaria conta, LocalDateTime inicioPeriodo, LocalDateTime finalPeriodo);

}