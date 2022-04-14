package br.com.projetojsf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;

@ViewScoped
@Named(value = "relUsuariosBean")
public class relUsuarios implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Pessoa> listaUsuario = new ArrayList<Pessoa>();
	private Date dataInicial;
	private Date dataFinal;
	
	
	@Inject
	private IDaoPessoa daoPessoa;
	
	@Inject
	private DaoGeneric<Pessoa> daoGeneric;
	
	public void GerarRelatorio() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		if((dataInicial != null && !dataInicial.toString().trim().isEmpty()) && (dataFinal != null && !dataFinal.toString().trim().isEmpty())) {
			listaUsuario = daoPessoa.consultarUsuarioIntervaloData(format.format(dataInicial), null);
		}
		else if( (dataInicial == null || dataInicial.toString().trim().isEmpty()) && (dataFinal != null && !dataFinal.toString().trim().isEmpty()) ) {
			listaUsuario = daoPessoa.consultarUsuarioIntervaloData(null, format.format(dataFinal));
		}
		else if ( (dataInicial != null && !dataInicial.toString().trim().isEmpty()) && (dataFinal == null || dataFinal.toString().trim().isEmpty())) {
			listaUsuario = daoPessoa.consultarUsuarioIntervaloData(format.format(dataInicial), null);
		}
		else if ( (dataInicial == null || dataInicial.toString().trim().isEmpty()) && (dataFinal == null || dataFinal.toString().trim().isEmpty())) {
			listaUsuario = daoGeneric.getListEntity(Pessoa.class);
		}
		
	}

	public List<Pessoa> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<Pessoa> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	

}
