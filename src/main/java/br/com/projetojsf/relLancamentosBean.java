package br.com.projetojsf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;

@ViewScoped
@Named(value = "relLancamentosBean")
public class relLancamentosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Lancamento> listLancamentos = new ArrayList<Lancamento>();
	private List<Lancamento> listLancamentosModal = new ArrayList<Lancamento>();

	@Inject
	private IDaoLancamento iDaoLancamento;

	@Inject
	private DaoGeneric<Lancamento> daoGeneric;

	private Lancamento lancamento = new Lancamento();
	
	public void buscarLancamento() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		listLancamentos = iDaoLancamento.consultarLancamentosIntervalo(getUsuarioLogado().getId(), format.format(lancamento.getDataLanInicial()), format.format(lancamento.getDataLanFinal()));
	}
	
	public void buscarModal() {
		listLancamentosModal = iDaoLancamento.consultar(getUsuarioLogado().getId());
	}
	

	public List<Lancamento> getListLancamentos() {
		return listLancamentos;
	}

	public void setListLancamentos(List<Lancamento> listLancamentos) {
		this.listLancamentos = listLancamentos;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
	public Pessoa getUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();

		Pessoa usuario = (Pessoa) context.getExternalContext().getSessionMap().get("usuarioLogado");
		
		return usuario;
	}

}
