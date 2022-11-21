package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Turma;

@RequestScoped
@SuppressWarnings("unchecked")
public class TurmaDAO extends DAO<Turma> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<Turma> listAllTurma(int ano) {
		return this.em.createQuery("""
				select t from Turma t
				left join fetch t.curso c
				where t.ano = :ano
				order by t.numero
				""").setParameter("ano", ano).getResultList();
	}

	public List<Integer> listAnosDisponiveis() {
		return this.em.createQuery("""
				select distinct t.ano from Turma t
				order by t.ano desc
				""").getResultList();
	}


	public Turma findTurma(Turma turma) {
		return (Turma) this.em.createQuery("""
				select t from Turma t
				left join fetch t.curso c
				where t = :turma
				""")
				.setParameter("turma", turma)
				.getSingleResult();
	}

	public List<Matricula> listAllMatricula(Turma turma) {

		// OBS: "and m is not null" é uma pequena POG pq eu nao quero mapear a turma na
		// matricula

		return (List<Matricula>) this.em.createQuery(
				"""
						select m from Turma t
						left join t.listMatriculaTurma m
						left join fetch m.usuario u
						where t = :turma
								and m is not null
						order by u.nome asc
				""")
				.setParameter("turma", turma)
				.getResultList();
	}

}