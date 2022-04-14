package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named(value = "cadBean")
public class cadBean implements Serializable{

	private static final long serialVersionUID = 1L;

	
	public void salvar() {
		System.out.println("Chamou!");
	}
}
