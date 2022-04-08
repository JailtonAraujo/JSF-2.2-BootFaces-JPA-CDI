package br.com.converters;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;


import br.com.entidades.Cidades;

@FacesConverter(forClass = Cidades.class, value = "cidadeConverter")
public class CidadeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	

	@Override/*RETORNA OBJETO INTEIRO*/
	public Object getAsObject(FacesContext context, UIComponent component, String id_cidade) {

		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		
		
		Cidades cidade = (Cidades) entityManager.find(Cidades.class, Long.parseLong(id_cidade));
		
		return cidade;
	}

	@Override/*RETORNA O CODIGO EM STRING*/
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {
		
		if(cidade == null) {
			return null;
		}
		if(cidade instanceof Cidades) {
			return ((Cidades)cidade).getId().toString();
		}else {
			return cidade.toString();
		}
		
	}

}
