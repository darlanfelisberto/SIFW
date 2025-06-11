package br.edu.iffar.fw.classBag.db.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@RequestScoped
public class CreditosDAO extends DAO<Credito> {

	private static final long serialVersionUID = 22021991L;

	@SuppressWarnings("unchecked")
	public List<Credito> listAllMoves() {
		return this.em.createQuery("from Credito c order by c.dtCredito desc").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Credito> listAllByUserName(String userName){
		String hql = "select c from Credito c "
				+ "join c.usuario u "
				+ "where u.userName = :userName "
				+ "order by c.dtCredito desc";
		Query q = this.em.createQuery(hql);
		q.setParameter("userName", userName);
		return q.getResultList();
	}
	
//	public Float getSaldo(String userName) {
//		Query q =  this.em.createNativeQuery(""
//				+ "select sum(saldo) as saldo from (\n"
//				+ "	select sum(c.valor) as saldo from creditos c "
//				+ " inner join usuarios u2 on c.usuario_id = u2.usuario_id "
//				+ " where c.tipo_credito = :entrada and u2.username = :username"
//				+ "	union \n"
//				+ "	select sum(c.valor) *(-1) as saldo from creditos c "
//				+ " inner join usuarios u2 on c.usuario_id = u2.usuario_id "
//				+ " where c.tipo_credito in :saida and u2.username = :username"
//				+ " )as c");
//		
////		q =  this.em.createQuery("select c from Credito c where c.tipoCredito in :saida ");
//		String [] t = {TypeCredito.SAIDA.name(),TypeCredito.TRANS.name()};
//		q.setParameter("entrada", TypeCredito.ENTRADA.name());
//		q.setParameter("saida", Arrays.asList(t));
//		q.setParameter("username", userName);
//		Number saldo = (Number) q.getSingleResult();
//		if(saldo == null) {
//			return 0f;
//		}
//		return saldo.floatValue();
//	}
	
	@SuppressWarnings("unchecked")
	public List<Credito> getCreditosByMesAno(LocalDate mesAno, Usuario user){
		Query q = this.em.createQuery("""
				select c from Credito c
				inner join fetch c.usuario u
				inner join fetch c.tipoCredito tc
				left join fetch c.agendamento a
				left join fetch a.refeicao r
				left join fetch r.tipoRefeicao tr
				left join fetch c.parent p
				left join fetch p.agendamento pa
				left join fetch p.usuario uup
				where c.usuario = :user and c.dtCredito between :inicio and :fim order by c.dtCredito desc""");
		
		LocalDateTime inicio = LocalDateTime.of(mesAno.getYear(), mesAno.getMonth(), 1, 00, 00);
		LocalDateTime fim = LocalDateTime.of(mesAno.getYear(), mesAno.getMonth(), mesAno.lengthOfMonth(),23,59);
		q.setParameter("inicio", inicio);
		q.setParameter("user", user);
		q.setParameter("fim", fim);
		
		return q.getResultList();
	}
	
	public Credito existeSaidaCreditoParaHoje(Usuario user, TipoRefeicao tipoRefeicao){
		try {
			Query q = this.em.createQuery("""
					select c from Credito c
					left join c.usuario u
					left join c.tipoCredito tc
					left join c.agendamento a
					left join a.refeicao r
					left join r.tipoRefeicao tr
					where u =:user
					and c.dtCredito between :dtInicio and :dtFim 
					and tc.tipoCreditoId = 'SAIDA'
					and tr = :tipoRefeicao
				""");
			
			q.setParameter("user", user);
			q.setParameter("tipoRefeicao", tipoRefeicao);
			
			LocalDate today = LocalDate.now();
			q.setParameter("dtInicio", today.atTime(0, 0));
			q.setParameter("dtFim", today.atTime(23, 59));
			
			Object oo = q.getSingleResult();
			return (Credito) oo;
		} catch (NoResultException e) {
			return null;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<Credito> findCreditosByAgendmento(Agendamento a){
		Query q = this.em.createQuery("""
				from Credito c
				inner join c.agendamento a
				where a = :agendamento
		""");
		q.setParameter("agendamento", a);
		
		return q.getResultList();
	}

	public Saldo findSaldo(Usuario u) {
		try {
			return (Saldo)this.em.createNativeQuery("""
					select c.usuario_id, sum(c.valor) as saldo from creditos c
					inner join tipo_credito tc ON c.tipo_credito_id = tc.tipo_credito_id
					where c.usuario_id = :user
					group by c.usuario_id
			""" ,Saldo.class).setParameter("user", u.getMMId()).getSingleResult();
		} catch (NoResultException e) {
			return new Saldo();
		}
	}
}