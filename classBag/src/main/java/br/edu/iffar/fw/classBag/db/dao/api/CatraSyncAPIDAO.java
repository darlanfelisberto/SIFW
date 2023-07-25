package br.edu.iffar.fw.classBag.db.dao.api;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
//import br.edu.iffar.fw.classBag.db.model.api.APIServidor;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classShared.db.DAO;
import br.edu.iffar.fw.classShared.db.Model;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;


@SuppressWarnings("unchecked")
@RequestScoped
public class CatraSyncAPIDAO extends DAO<Model<?>> {

	private static final long serialVersionUID = 22021991L;

	public List<APIAgendamento> getListAllAPIUsuario(){
		Query q = this.em.createQuery("from APIUsuario");
		
		return q.getResultList();
	}
	
	public List<APIAgendamento> listFromDateStar(LocalDate dateStart){
		Query q = this.em.createQuery("""
				select a from APIAgendamento a 
				join fetch a.usuario u 
				join fetch a.refeicao r 
				join fetch r.tipoRefeicao tr 
				where  a.dtAgendamento >= :dateStart
				and a.credito is null
				""").setParameter("dateStart", dateStart);
		
		return q.getResultList();
	}
	
	public List<APIAgendamentosDisponibilizados> listAgendamentoDisponibiliadoFromDateStar(Date dateStart){
		Query q = this.em.createQuery("""
				from APIAgendamentosDisponibilizados ad
				join fetch ad.usuarioDisponibilizou ud
				join fetch ad.agendamento a
				join fetch a.refeicao r
				where ad.usuarioPegouId is null 
				and cast(ad.dtDisponibilizado as date) = :data 
		""");
		//
		//join fetch a.refeicao r
		//join fetch r.tipoRefeicao tr
		q.setParameter("data",dateStart);
		
		return q.getResultList();
	}
		
	
	
//	@SuppressWarnings("unchecked")
//	public List<APIServidor> listAllServidor(){
//		Query q = this.em.createQuery("""
//				from APIServidor s
//				inner join fetch s.usuario
//				inner join fetch s.tipoVinculo
//				""");
//		return q.getResultList();
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<APIGrupoRefeicoes> listAllGrupos(){
////		CriteriaBuilder cb = this.em.getCriteriaBuilder();
////		CriteriaQuery<APIGrupoRefeicoes> q = cb.createQuery(APIGrupoRefeicoes.class);
////		q.distinct(true);
////		Root o = q.from(APIGrupoRefeicoes.class);
////		o.fetch("tipoVinculo", JoinType.INNER);
////		o.fetch("listRefeicao", JoinType.LEFT).fetch("tipoRefeicao", JoinType.LEFT);
////		q.select(o);
////		 
////		List<APIGrupoRefeicoes> l = this.em.createQuery(q).getResultList();
////		
////		return l;
//		
//		Query q = this.em.createQuery("""
//				select distinct a from APIGrupoRefeicoes a 
//				left join fetch a.tipoVinculo tv 
//				left join fetch a.listRefeicao r 
//				left join fetch r.tipoRefeicao tr
//				left join fetch a.listMatricula lm
//				left join fetch lm.usuario u
//			""", APIGrupoRefeicoes.class);
//		
//		return q.getResultList();
//	}
	
}
