package br.com.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override/*RETORNA OBJETO INTEIRO*/
	public Object getAsObject(FacesContext context, UIComponent component, String id_estado) {

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Estados estados = (Estados) entityManager.find(Estados.class, Long.parseLong(id_estado));
		
		transaction.commit();
		entityManager.close();
		
		return estados;
	}

	@Override/*RETORNA O CODIGO EM STRING*/
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		
		if(estado == null) {
			return null;
		}
		if(estado instanceof Estados) {
			return ((Estados)estado).getId().toString();
		}else {
			return estado.toString();
		}
		
	}

}
