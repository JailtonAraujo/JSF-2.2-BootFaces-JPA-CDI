package br.com.projetojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> listaDePessoas = new ArrayList<Pessoa>();
	
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	
	public String salvar() {
		
		pessoa = daoGeneric.merge(pessoa);
		carregarListaDePessoas();
		
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String deletar () {
		daoGeneric.deletePorID(pessoa);
		carregarListaDePessoas();
		
		return "";
	}
	
	@PostConstruct
	public void carregarListaDePessoas() {
		listaDePessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaDePessoas() {
		return listaDePessoas;
	}

	public void setListaDePessoas(List<Pessoa> listaDePessoas) {
		this.listaDePessoas = listaDePessoas;
	}

	
	

}
