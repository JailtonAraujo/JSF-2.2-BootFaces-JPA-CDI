package br.com.repository;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.query.Query;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

@SuppressWarnings("rawtypes")
public class IDaoPessoaImpl implements IDaoPessoa {

	@Override
	public Pessoa consultarLogin(String login, String senha) {
		
		Pessoa pessoa = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		
		pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '"+login+"' and p.senha = '"+senha+"' ").getSingleResult();
		
		entityTransaction.commit();
		entityManager.close();
		
		return pessoa;
	}

	@Override
	public Pessoa consultaLoginEspecifico (String login, String senha) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		
		Pessoa pessoa = new Pessoa();
		
		Query query = (Query) entityManager.createNativeQuery("select p.id, p.nome, p.perfilUser from Pessoa p where p.login = ? and p.senha = ?;");
		query.setParameter(1, login);
		query.setParameter(2, senha);
		
		Object [] pessoaOb = (Object[]) query.getSingleResult();
		
		pessoa.setId(((BigInteger) pessoaOb[0]).longValue());
		pessoa.setNome((String) pessoaOb[1]);
		pessoa.setPerfilUser((String) pessoaOb[2]);
		
		entityTransaction.commit();
		entityManager.close();
		
		 return pessoa;
	}

}
