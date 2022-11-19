package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Curso;

@RequestScoped
@SuppressWarnings("unchecked")
public class CursosDAO extends DAO<Curso> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<Curso> listAllCursos() {
		return this.em.createQuery("""
				from Curso c 
				join fetch c.tipoVinculo tv 
				order by c.nome asc
				""").getResultList();
	}


	public List<Curso> listAllCursosComMatriculas() {
		return this.em.createQuery("""
				select distinct c from Matricula m
				inner join m.curso c
				order by c.nome asc
				""").getResultList();
	}

}