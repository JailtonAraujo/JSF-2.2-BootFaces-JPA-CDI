package br.com.projetojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;
import br.com.repository.IDaoLancamentoImpl;

@ViewScoped
@ManagedBean(name = "lanceamentoBean")
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<Lancamento>();
	private IDaoLancamento daoLancamento = new IDaoLancamentoImpl();
	
	private List<Lancamento> listaDeLancamento = new ArrayList<Lancamento>();
	
	
	public String salvar() {
		
		lancamento.setUsuario(getUserLogado());
		 lancamento = daoGeneric.merge(lancamento);
		carregarLancamentos();
		
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
		
		return "";
	}
	
	public Pessoa getUserLogado () {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuarioLogado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return usuarioLogado;
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
