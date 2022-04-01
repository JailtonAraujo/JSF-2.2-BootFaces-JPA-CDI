package br.com.repository;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarLogin (String login, String senha);
}
