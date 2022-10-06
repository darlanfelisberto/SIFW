//package br.edu.iffar.fw.classBag.db.dao;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//import javax.enterprise.context.RequestScoped;
//import javax.persistence.NoResultException;
//import javax.persistence.Query;
//import javax.transaction.RollbackException;
//import javax.transaction.Transactional;
//
////import org.keycloak.KeycloakSecurityContext;
//
//import br.edu.iffar.fw.classBag.db.DAO;
//import br.edu.iffar.fw.classBag.db.model.AgendamentosDisponibilizados;
//import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
//
//@SuppressWarnings("unchecked")
//@RequestScoped
//public class AgendamentosDisponilizadosDAO extends DAO<AgendamentosDisponibilizados> {
//		
//	private static final long serialVersionUID = 22021991L;
//	
//	/**
//	 * metodo salva o agendamento e a disonibilidade em uma transação
//	 * 
//	 * obs: da pra usera UserTransaction tbm , mas assim fica mais limpo o bean
//	 * 
//	 * @param ageDisp objeto pronto para salvar
//	 * @throws RollbackException 
//	 */
//	@Transactional
//	public void salvaAgendamentoDisponibilizado(AgendamentosDisponibilizados ageDisp) throws RollbackException {
//		this.em.merge(ageDisp.getAgendamento());
//		this.em.merge(ageDisp);
//	}
//	
//	public AgendamentosDisponibilizados findById(UUID id) {
//		return (AgendamentosDisponibilizados) 
//				this.em.createQuery("""
//					from AgendamentosDisponibilizados ad
//					join fetch ad.agendamento a
//					join fetch a.refeicao r
//					join fetch r.tipoRefeicao tr
//					left join fetch a.usuario u
//					join fetch ad.usuarioDisponibilizou ud
//					where ad.agendamentoDisponibilizadoId = :ageDispId
//				""")
//				.setParameter("ageDispId", id)
//				.getSingleResult();	
//	}
//
//	public List<AgendamentosDisponibilizados> listTodosAgendamentosDisponibilizados(TipoRefeicao tipoRefeicao, LocalDate today) {
//		Query q =  this.em.createQuery("""
//				from AgendamentosDisponibilizados ad
//				join fetch ad.agendamento a
//				join fetch a.refeicao r
//				join fetch r.tipoRefeicao tr
//				join fetch ad.usuarioDisponibilizou ud
//				where ad.usuarioPedouId is null 
//				and tr = :tipoRefeicao 
//				and :dateToday = a.dtAgendamento
//				order by ad.dtDisponibilizado asc
//				""");
//		q.setParameter("tipoRefeicao", tipoRefeicao);
//		q.setParameter("dateToday", today);
//		
//		return q.getResultList();
//	}
//
//	public AgendamentosDisponibilizados findAgendamentoDisponibilizado(TipoRefeicao tipoRefeicao, LocalDate today) {
//		try {
//			Query q = this.em.createQuery("""
//					from AgendamentosDisponibilizados ad
//					join fetch ad.agendamento a
//					join fetch a.refeicao r
//					join fetch r.tipoRefeicao tr
//					join fetch ad.usuarioDisponibilizou ud
//					where ad.usuarioPedouId is null
//					and tr = :tipoRefeicao
//					and :dateToday = a.dtAgendamento
//					order by ad.dtDisponibilizado asc
//					""");
//			q.setParameter("tipoRefeicao", tipoRefeicao)
//					.setParameter("dateToday", today)
//					.setMaxResults(1);
//
//			return (AgendamentosDisponibilizados) q.getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
//}