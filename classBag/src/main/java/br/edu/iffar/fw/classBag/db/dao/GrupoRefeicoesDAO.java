package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.edu.iffar.fw.classBag.db.model.GrupoRefeicoes;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@RequestScoped
@SuppressWarnings("unchecked")
public class GrupoRefeicoesDAO extends DAO<GrupoRefeicoes> {

	private static final long serialVersionUID = 22021991L;

	public List<GrupoRefeicoes> listAll(){
		return this.em.createQuery("""
				select gr from GrupoRefeicoes gr 
				left join fetch gr.tipoVinculo tv
				order by gr.descricao
				""").getResultList();
	}

		public List<GrupoRefeicoes> listGroupsManualLinkVinculoByDesc(String descGrupo, boolean all){
		String hql = "select gr from GrupoRefeicoes gr where gr.tipoVinculo is null "
				+ (all?"":" and lower(gr.descricao) like lower(:descGrupo)")
				+ " order by gr.descricao asc";
		Query q = this.em.createQuery(hql);
		
		if(!all) {
			q.setParameter("descGrupo", "%" + descGrupo + "%");
		}
		
		return q.getResultList();
	}
	
	public List<GrupoRefeicoes> listGroupsByDesc(String descGrupo, boolean all){
		String hql = "select gr from GrupoRefeicoes gr "
				+ (all?"":" where lower(gr.descricao) like lower(:descGrupo)")
				+ " order by gr.descricao asc";
		Query q = this.em.createQuery(hql);
		
		if(!all) {
			q.setParameter("descGrupo", "%" + descGrupo + "%");
		}
		
		return q.getResultList();
	}
	
	public boolean grupoRefeicaoEmUso(GrupoRefeicoes gr) {
		Query q = this.em.createQuery("""
				select 1 from Agendamento a
				join a.refeicao r
				where r.grupoRefeicoes = :grupo
		""");
		q.setParameter("grupo", gr);
		return !q.getResultList().isEmpty();
	}
	
	public GrupoRefeicoes findGrupoRefeicoesMatriculaEager(GrupoRefeicoes grupoRefeicoes){
		Query q = this.em.createQuery("""
			select distinct gr from GrupoRefeicoes gr
			left join fetch gr.tipoVinculo tv
			left join fetch gr.listMatricula m
			left join fetch m.usuario u
			where gr = :grupo
			order by u.nome asc
		""");
		q.setParameter("grupo", grupoRefeicoes);
		try {
			return (GrupoRefeicoes) q.getSingleResult();
		} catch (NoResultException  e) {
			// TODO: handle exception
		}
		
		return null;
	}

	public boolean isVinculadaAGrupo(Matricula mat) {
		if(mat.isNovo()) {
			return false;
		}
		List<?> vinculado = this.em.createQuery("""
				select 1 from GrupoRefeicoes gr
				left join gr.listMatricula m
				where m = :mat
				""").setParameter("mat", mat).getResultList();
		if(vinculado == null || vinculado.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

}