package br.com.projetojsf;

import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import br.com.dao.DaoGeneric;
import br.com.entidades.Endereco;
import br.com.entidades.Pessoa;

public class testJPA {

	@Test
	public void TesteDependecia() {
		DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
		Endereco endereco = new Endereco();
		
		Pessoa pessoa = new Pessoa();
		pessoa.setIdade(44);
		pessoa.setNome("jailton");
		
		endereco.setCep("48800000");
		endereco.setComplemento("gfdgfd");
		endereco.setLocalidade("fdgfdg");
		endereco.setLogradouro("fdgfdg");
		endereco.setUf("g");
		endereco.setPessoa(pessoa);		
		pessoa.setEndereco(endereco);
		
		daoGeneric.salvar(pessoa);
		
	}
}
