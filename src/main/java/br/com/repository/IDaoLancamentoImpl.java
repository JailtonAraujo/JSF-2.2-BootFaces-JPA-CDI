package br.com.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;

@SuppressWarnings("unchecked")
@Named
public class IDaoLancamentoImpl implements IDaoLancamento, Serializable {

	private static final long serialVersionUID = 8274115355207130815L;

	@Inject
	private EntityManager entityManager;

	@Override
	public List<Lancamento> consultar(Long idUser) {

		List<Lancamento> lancamentos = null;

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		lancamentos = entityManager.createQuery("from Lancamento where usuario.id = " + idUser).getResultList();

		transaction.commit();

		return lancamentos;
	}

	@Override
	public List<Lancamento> consultarLimit10(Long idUser) {

		List<Lancamento> lancamentos = null;

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		lancamentos = entityManager.createQuery("from Lancamento where usuario.id = " + idUser + " order by id desc")
				.setMaxResults(10).getResultList();

		transaction.commit();

		return lancamentos;
	}

	@Override
	public List<Lancamento> consultarLancamentosIntervalo(Long idUser, String dataInicial, String dataFinal) {
		List<Lancamento> lancamentos = null;

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		lancamentos = entityManager.createQuery("from Lancamento where usuario.id = " + idUser + "  and dataLancamento >= '"+dataInicial+"' and dataLancamento <= '"+dataFinal+"' order by id desc")
				.setMaxResults(10).getResultList();

		transaction.commit();

		return lancamentos;
	}

	@Override
	public List<Lancamento> consultarLancamentoData(Long idUser, String dataLancamento) {
		List<Lancamento> lancamentos = null;

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		lancamentos = entityManager.createQuery("from Lancamento where dataLancamento = '"+dataLancamento+"' and usuario.id = "+idUser+"").getResultList();

		transaction.commit();

		return lancamentos;
	}

	

}
