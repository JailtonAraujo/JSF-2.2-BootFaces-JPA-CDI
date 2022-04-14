package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;

@ViewScoped
@Named(value = "cadVisitanteBean")
public class cadVisitanteBean implements Serializable{

	private Pessoa pessoa = new Pessoa();
	
	@Inject
	private DaoGeneric<Pessoa> daoGeneric;
	
	public String salvar() {
		pessoa.setPerfilUser("VISITANTE");
		daoGeneric.merge(pessoa);
		mostrarMsg("Visitante Cadastrado com Sucesso!");
		return "";
	}
	
	public void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
}
