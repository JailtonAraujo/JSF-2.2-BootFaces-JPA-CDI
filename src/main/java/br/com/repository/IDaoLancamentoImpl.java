package br.com.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUtil;

public class IDaoLancamentoImpl implements IDaoLancamento{

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consultar(Long idUser) {
		
		List<Lancamento> lancamentos = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		lancamentos = entityManager.createQuery("from Lancamento where usuario.id = "+idUser).getResultList();
		
		transaction.commit();
		entityManager.close();
		
		return lancamentos;
	}

}
