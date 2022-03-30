package br.com.projetojsf;

import javax.persistence.Persistence;

public class testJPA {

	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("projeto-jsf");
	}
}
