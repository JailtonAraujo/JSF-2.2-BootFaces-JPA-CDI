package br.com.repository;

import java.util.Date;
import java.util.List;

import br.com.entidades.Lancamento;

public interface IDaoLancamento {

	List<Lancamento> consultar (Long idUser);
	
	List<Lancamento> consultarLimit10 (Long idUser);
	
	List<Lancamento> consultarLancamentosIntervalo (Long idUser, String dataInicial, String dataFinal);
	List<Lancamento> consultarLancamentoData (Long idUser, String dataLancamento);
}
