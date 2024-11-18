package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.com.feliva.sharedClass.db.NoContext;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.com.feliva.sharedClass.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;

@RequestScoped
@SuppressWarnings("unchecked")
public class CursosDAO extends DAO<Curso> implements NoContext<CursosDAO> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<Curso> listAllCursos() {
		return this.em.createQuery("""
				select c from Curso c 
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

	public Curso findByCod(Integer cod) {
		return (Curso) this.em.createQuery("select c from Curso c where c.idCurso = :idCurso")
				.setParameter("idCurso",cod)
				.getSingleResult();
	}

	@Override
	public CursosDAO noContext(EntityManager em) {
		this.em = em;
		return this;
	}
}