package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Usuario;

@ApplicationScoped
public class BackgroudDAO  {
	
	@Inject private EntityManager em;
	
	@Transactional
	public Usuario getUsuarioByCPF(String cpf) {
		try {
//busca situacoes para o cache l1
			List<Matricula> lm = this.em.createQuery("""
					select m from Matricula m
					left join fetch m.curso c
					left join fetch m.usuario u
					left join fetch m.listSituacaoMatricula sm
					where u.cpf = :cpf
					""").setParameter("cpf", cpf).getResultList();

			Usuario u = (Usuario) this.em.createQuery("""
					select distinct u from Usuario u
					left join fetch u.listMatricula m
					where u.cpf = :cpf
					""").setParameter("cpf", cpf).getSingleResult();
			return u;
		} catch (NoResultException e) {
			System.out.println("Nenum usuario encontrado.");
			return null;
		}
		
	}
	
	public Matricula getMatriculaByNumero(Integer numero) {
		try {
			return (Matricula)this.em.createQuery("from Matricula m where m.idMatricula = :numero").setParameter("numero", numero).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Nenhuma matricula encontrada.");
			return null;
		}
	}
	
	public TipoVinculo getTipoVinculoByIdTipoVinculo(int vinculo) {
		try {
			return (TipoVinculo)this.em.createQuery("from TipoVinculo tv where tv.idTipoVinculo = :numero").setParameter("numero", vinculo).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Nenhuma matricula encontrada.");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Matricula> listAllMatriculaByCurso(Curso curso){
		Query q = this.em.createQuery("""
				select distinct m from Matricula m
				join fetch m.listSituacaoMatricula sm
				join SituacaoMatricula sm2 on sm2.matricula = m
				where 
				m.curso = :curso and sm2.situacao = 'ATIVA'
				and
				(sm2.matricula.matriulaId,sm2.momento) in(
					select mm.matricula.matriulaId,max(mm.momento)  from SituacaoMatricula as mm
					group by mm.matricula
				)
		""");
		q.setParameter("curso", curso);
//		q.setParameter("situacao", TypeSituacao.ATIVA);
		
		return (List<Matricula> )q.getResultList();
	}
	
}