package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarLogin (String login, String senha);
	
	Pessoa  consultaLoginEspecifico (String login, String senha);
	
	List<SelectItem> listarEstados();
	List<SelectItem> listarCidades(String estado_id);
}
