package br.com.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;

@Named
public class IDaoLancamentoImpl implements IDaoLancamento, Serializable{

	private static final long serialVersionUID = 8274115355207130815L;
	
	@Inject
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consultar(Long idUser) {
		
		List<Lancamento> lancamentos = null;
		
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		lancamentos = entityManager.createQuery("from Lancamento where usuario.id = "+idUser).getResultList();
		
		transaction.commit();
		
		return lancamentos;
	}

}
