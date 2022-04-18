package br.com.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.query.Query;

import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;

@SuppressWarnings("rawtypes")
@Named
public class IDaoPessoaImpl implements IDaoPessoa, Serializable {

	private static final long serialVersionUID = -2416250826231427L;
	
	@Inject
	private EntityManager entityManager;
	
	@Override
	public Pessoa consultarLogin(String login, String senha) {

		Pessoa pessoa = null;

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		pessoa = (Pessoa) entityManager
				.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "' ")
				.getSingleResult();

		entityTransaction.commit();

		return pessoa;
	}

	@Override
	public Pessoa consultaLoginEspecifico(String login, String senha) {
	
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Pessoa pessoa = new Pessoa();

		Query query = (Query) entityManager.createNativeQuery(
				"select p.id, p.nome, p.perfilUser from Pessoa p where p.login = ? and p.senha = ?;");
		query.setParameter(1, login);
		query.setParameter(2, senha);

		try {
		Object[] pessoaOb = (Object[]) query.getSingleResult();
		
		pessoa.setId(((BigInteger) pessoaOb[0]).longValue());
		pessoa.setNome((String) pessoaOb[1]);
		pessoa.setPerfilUser((String) pessoaOb[2]);
		}catch (javax.persistence.NoResultException e) {/*Tratamento se não encontrar usuario com login e senha*/
		}
		entityTransaction.commit();

		return pessoa;
	}

	@Override
	public List<SelectItem> listarEstados() {
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		List<Estados> listaDeEstados = entityManager.createQuery("from Estados").getResultList();

		for (Estados estado : listaDeEstados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}

		entityTransaction.commit();

		return selectItems;
	}

	@Override
	public List<SelectItem> listarCidades(String estado_id) {
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		List<Cidades> cidades = new ArrayList<Cidades>();

		cidades = entityManager.createQuery("from Cidades where estados.id = " + estado_id).getResultList();

		for (Cidades cidade : cidades) {
			selectItems.add(new SelectItem(cidade, cidade.getNome()));
		}

		entityTransaction.commit();

		return selectItems;
	}

	@Override
	public List<Pessoa> consultarUsuarioModal(String nome) {
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.com.entidades.Pessoa(p.id, p.nome)"
				+ "from Pessoa p where p.nome like '").append(nome).append("%'");
		
		pessoas = entityManager.createQuery(sql.toString(), Pessoa.class).setMaxResults(10).getResultList();
		
		return pessoas;
	}

	@Override
	public List<Pessoa> consultarUsuarioIntervaloData(String dataInicial, String dataFinal) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		List<Pessoa> usuarios = new ArrayList<Pessoa>();
		
		StringBuilder sql = new StringBuilder();
		
		if( (dataInicial == null || dataInicial.trim().isEmpty()) && (dataFinal != null && !dataFinal.trim().isEmpty())) {
			sql.append("select new br.com.entidades.Pessoa(p.id, p.nome, p.cpf, p.idade, p.perfilUser, p.endereco.logradouro) "
					+ "from Pessoa p where p.dataNascimento <= '").append(dataFinal).append("'");
		}
		else if( (dataInicial != null && !dataInicial.trim().isEmpty()) && (dataFinal == null || dataFinal.trim().isEmpty())) {
			sql.append("select new br.com.entidades.Pessoa(p.id, p.nome, p.cpf, p.idade, p.perfilUser, p.endereco.logradouro)"
					+ "from Pessoa p where p.dataNascimento >= '").append(dataInicial).append("'");
		}
		else if ( (dataInicial != null && !dataInicial.trim().isEmpty()) && (dataFinal != null && !dataFinal.trim().isEmpty()) ) {
			sql.append("select new br.com.entidades.Pessoa(p.id, p.nome, p.cpf, p.idade, p.perfilUser, p.endereco.logradouro)"
					+ "from Pessoa p where p.dataNascimento >= '").append(dataInicial).append("'")
			.append(" and p.dataNascimento <= '").append(dataFinal).append("'");
		}
		else if ( (dataInicial == null || dataInicial.trim().isEmpty()) && (dataFinal == null || dataFinal.trim().isEmpty()) ) {
			sql.append("select new br.com.entidades.Pessoa(p.id, p.nome, p.cpf, p.idade, p.perfilUser, p.endereco.logradouro)"
					+ "from Pessoa p right join p.endereco");
		}
		
		usuarios = entityManager.createQuery(sql.toString(), Pessoa.class).getResultList();
		
		return usuarios;
	}

}
