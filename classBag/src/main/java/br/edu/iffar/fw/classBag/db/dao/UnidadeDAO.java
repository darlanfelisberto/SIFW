package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Unidade;

@RequestScoped
public class UnidadeDAO extends DAO<Unidade> {

	private static final long serialVersionUID = 1L;
	@Inject
	EntityManager ma;

	@Transactional
	public int removeTodosNodes(Unidade u) throws RollbackException {// @formatter:off
		String sql = """
				with  recursive  uni as(
					select u.* from moradia_estudantil.unidade u
					where u.unidade_id = :unidadeId and u.parent_id is not null 
					union all
					select u2.* from moradia_estudantil.unidade u2
				join uni on uni.unidade_id = u2.parent_id
				)
				select * from uni
				""";
		Query q = this.em.createNativeQuery(sql,Unidade.class).setParameter("unidadeId", u.getMMId());
		
		List<Unidade> l = q.getResultList();
		
//		String ids = "('"+l.stream().map(Object::toString).collect(Collectors.joining("','"))+"')";
//
//		int retorno = this.em.createNativeQuery("delete from moradia_estudantil.unidade where unidade_id in " + ids).executeUpdate();

		
		// é so para gerar os logs coloquei assim
		for (Unidade uni : l) {
			this.em.remove(uni);
		}
		
		return l.size();
	}
	
	public List<Unidade> listAllUnidade(){
		return null;
	}
	
	/**
	 * a consulta abaixo usa o cache do hibernate por isso necessita da anotação @transactional
	 * 
	 * @return
	 */	
	
	@Transactional
	public Unidade getRootNodeUnidade() {// @formatter:off
		
		Query q = this.em.createQuery("""
				from Unidade u
				left join fetch u.tipoUnidade ti
				left join fetch u.listItemUnidade iu
				order by u.desc desc nulls first
		""");
		List<Unidade> l = q.getResultList();
		

		q = this.em.createQuery("""
				from Unidade u
				left join fetch u.listHabitanteUnidade hu
				left join fetch hu.matricula m
				left join fetch m.usuario us
				order by u.desc desc nulls first
		""");
		l = q.getResultList();
				
		q = this.em.createQuery("""
				select u from Unidade u
				left join fetch u.listUnidadeChildren uc
				order by u.desc desc nulls first
		""");
		l = q.getResultList();
		
		Unidade u = (Unidade) this.em.createQuery("select u from Unidade u where u.parrent is null order by u.desc desc").getSingleResult();
				
		return u;
	}
}
