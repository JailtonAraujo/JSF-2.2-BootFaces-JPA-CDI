package br.com.projetojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> listaDePessoas = new ArrayList<Pessoa>();
	
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	public String salvar() {
		
		pessoa = daoGeneric.merge(pessoa);
		carregarListaDePessoas();
		mostrarMsg("Salvo com Sucesso!");
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String deletar () {
		daoGeneric.deletePorID(pessoa);
		carregarListaDePessoas();
		mostrarMsg("Usuario Excluido com sucesso!");
		return "";
	}
	
	@PostConstruct
	public void carregarListaDePessoas() {
		listaDePessoas = daoGeneric.getListEntity(Pessoa.class);
	}
	
	public String logar() {
		
		Pessoa usuarioLogado = iDaoPessoa. consultaLoginEspecifico(pessoa.getLogin(), pessoa.getSenha());
		
		if(usuarioLogado != null) {// Achou usuario
			
			//Adicionar o usuario na sessão usuarioLogado
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", usuarioLogado);
			
			return "primeira.jsf";
		}
		
		return "index.jsf";
	}
	
	public String logout() {
		
		FacesContext context = FacesContext.getCurrentInstance(); 
        context.getExternalContext().getSessionMap().remove("usuarioLogado");
        
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance()
        					  .getExternalContext().getSession(true);
        		    session.invalidate(); 
		
		return "index.jsf";
	}
	
	public boolean retricaoAcesso(String perfil) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuarioLogado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return usuarioLogado.getPerfilUser().equalsIgnoreCase(perfil);
	}
	
	public void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+pessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			
			InputStream stream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while ((cep = reader.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Pessoa gson = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			
			pessoa.setLocalidade(gson.getLocalidade());
			pessoa.setComplemento(gson.getComplemento());
			pessoa.setUf(gson.getUf());
			pessoa.setLogradouro(gson.getLogradouro());
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao buscar Cep!");
		}
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
