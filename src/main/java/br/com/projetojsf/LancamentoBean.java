package br.com.projetojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;

@javax.faces.view.ViewScoped
@Named(value = "lanceamentoBean")
public class LancamentoBean implements Serializable{

	private static final long serialVersionUID = -2356585645424425571L;

	private Lancamento lancamento = new Lancamento();
	
	@Inject
	private DaoGeneric<Lancamento> daoGeneric;
	
	@Inject
	private IDaoLancamento daoLancamento;
	
	private List<Lancamento> listaDeLancamento = new ArrayList<Lancamento>();
	
	
	public String salvar() {
		
		lancamento.setUsuario(getUserLogado());
		 lancamento = daoGeneric.merge(lancamento);
		carregarLancamentos();
		gerarMsg("Lançamento salvo com sucesso!");
		
		return "";
	}
	
	@PostConstruct
	public void carregarLancamentos() {
		
		listaDeLancamento = daoLancamento.consultar(getUserLogado().getId());
	}
	
	public String novo() {
		
		lancamento = new Lancamento();
		
		return "";
	}
	
	public String deletar() {
		
		daoGeneric.deletePorID(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		gerarMsg("Excluido com sucesso!");
		
		return "";
	}
	
	public Pessoa getUserLogado () {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuarioLogado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return usuarioLogado;
	}
	
	public void gerarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Lancamento> getListaDeLancamento() {
		return listaDeLancamento;
	}

	public void setListaDeLancamento(List<Lancamento> listaDeLancamento) {
		this.listaDeLancamento = listaDeLancamento;
	}

}
