package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Usuario;

@RequestScoped
@SuppressWarnings("unchecked")
public class MatriculaDAO extends DAO<Matricula> {

	private static final long serialVersionUID = 22021991L;

	
	public List<Matricula> listMatriculaByLikeNome(String nome){
		Query q = this.em.createQuery("""
			from Matricula m 
				join fetch m.usuario u
					where unaccent(lower(u.nome)) like unaccent(:nome)
			order by u.nome asc"""
		);
		q.setParameter("nome", "%" + nome.toLowerCase() + "%");
		
		return q.getResultList();
	}
	
	public List<Matricula> listMatriculaNotHabByLikeNome(String nome) {

		// @formatter:off
		Query q = this.em.createQuery("""
			from Matricula m 
			join fetch m.usuario u
			where
			unaccent(lower(u.nome)) like unaccent(:nome)
			and  m not in(
				select hu.matricula from HabitanteUnidade hu
				where hu.dtSaida is null or hu.dtSaida > current_date
			)
			order by u.nome asc
		""");
		q.setParameter("nome", "%"+nome.toLowerCase() +"%");
		
		return q.getResultList();
	}
	
	public Matricula findByIdMatricula(Integer idMatricula) {
		try {
			return (Matricula) this.em.createQuery("from Matricula m where m.idMatricula = :idMatricula").setParameter("idMatricula", idMatricula).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * O @transaction cria uma transação para a execução do metodo e dentro dele é executada duas consultas.
	 * A primeira cria um cache e a segunda cria retorna o objeto
	 * 
	 * @param u
	 * @return
	 */
	@Transactional
	public List<Matricula> listAllMatriculaByUsuario(Usuario u){
		
		this.em.createQuery("""
				select m from Matricula m
				left join fetch m.listSituacaoMatricula sm
				where m.usuario = :usuario
				order by m.idMatricula, sm.situacao
			"""
					).setParameter("usuario", u).getResultList();
//		cria cache
		return this.em.createQuery("""
				select m from Matricula m
				left join fetch m.curso c
				left join fetch c.tipoVinculo tv
				where m.usuario = :usuario
				""").setParameter("usuario", u).getResultList();
				
	}

}