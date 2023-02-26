package br.edu.iffar.fw.classBag.db.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;

@SuppressWarnings("unchecked")
@RequestScoped
public class AgendamentosDAO extends DAO<Agendamento> {

	private static final long serialVersionUID = 22021991L;

	public Agendamento findById(UUID id) {
		try {
			Query q = this.em.createQuery("""
					select a from Agendamento a
					left join fetch a.credito c
					where a.agendamentoId = :agendamentoId
					""");
			q.setParameter("agendamentoId", id);
			
			return (Agendamento) q.getSingleResult();	
		} catch (NoResultException  e) {
		}
		return null;
	}

		
	public List<Object[]> getAgendamentoEmDataPeriodo(LocalDate dtInicial,LocalDate dtFim,TipoRefeicao tf) {

		Query q = this.em.createNativeQuery("""
				select dia,	coalesce(
						(select count(a.agendamento_id)  from agendamentos a
						left join refeicao r on r.refeicao_id =a.refeicao_id 
						where (a.dt_agendamento = foo.dia) and r.tipo_refeicao_id = :tipoRefeicao group by r.tipo_refeicao_id)
					,0) as total
				from(
				 	select generate_series(cast(:dtInicial as date) , cast(:dtFim as date)  , '1 day') as dia
				 ) as foo """)
		.setParameter("dtInicial", dtInicial)
		.setParameter("dtFim", dtFim)
		.setParameter("tipoRefeicao", tf.getMMId());

		return  q.getResultList();
	}
	
	public boolean existeSobreposicaoDeRefeicao(Agendamento age,Usuario usuario) {
		Query q = this.em.createQuery("""
				select 1 from Agendamento a 
				join a.refeicao r
				left join a.matricula m
				left join a.servidor s
				where (s.usuario = :usuario or m.usuario = :usuario) 
				and a != :agendamento
				and r.tipoRefeicao = :tipo
				and a.dtAgendamento = :dtAgendamento
		""");

		q.setParameter("dtAgendamento", age.getDtAgendamento())
		.setParameter("tipo", age.getRefeicao().getTipoRefeicao())
		.setParameter("usuario", usuario)
		.setParameter("agendamento", age);

		return !q.getResultList().isEmpty();
	}
	
	public Agendamento findByUserAndDateAndTipoRefeicao(Usuario user, LocalDate date, TipoRefeicao tipoRefeicao) {
    	Query q = this.em.createQuery("""
				select a from Agendamento a  
				inner join fetch a.refeicao r 
				inner join fetch r.tipoRefeicao tr 
				left join fetch a.credito c
				left join fetch a.matricula m
				left join fetch a.servidor s
				left join s.usuario us
				left join m.usuario um
				where (us = :user or um = :user) 
				and tr = :tipoRefeicao  
				and :dateToday = a.dtAgendamento 
		""")
		.setParameter("dateToday",date)
		.setParameter("user", user)
		.setParameter("tipoRefeicao", tipoRefeicao);
    	try {
        	 return (Agendamento) q.getSingleResult();			
		} catch (NoResultException e) {
			System.out.println(e.getMessage());
		}
    	return null;
    }
	
	public List<Agendamento> findAgendamentoEmPeriodo(Model vinculo,LocalDateTime inicio,LocalDateTime fim) {
    	Query q = this.em.createQuery("""
    		select a from Agendamento a
    		left join fetch a.credito lc 
			left join fetch a.matricula m
			left join fetch a.servidor s
			inner join fetch a.refeicao r 
			inner join fetch r.tipoRefeicao tr 
			inner join fetch r.grupoRefeicoes gr
			where (m.matriculaId = :id or s.servidorId = :id)
			and (
					(a.dtAgendamento between :inicio and :final)
			)
			order by tr.tipoRefeicaoId desc,a.dtAgendamento asc

    	""");
    	
		q.setParameter("inicio", inicio.toLocalDate());
		q.setParameter("final", fim.toLocalDate());

    	q.setParameter("id", vinculo.getMMId());
    	
    	return q.getResultList();
    }

	public List<Agendamento> listAgendamentosTransferiveis(Usuario u, LocalDate data) {
		List<Agendamento> tranferifevis = new ArrayList<Agendamento>();
		List<Agendamento> lista = this.em.createQuery("""
					select a from Agendamento a
					join fetch a.refeicao r
					join fetch r.tipoRefeicao tr
					left join fetch a.matricula m
					left join fetch a.servidor s
					where a.credito is null
					and a.dtAgendamento between :dtIni and :dtFim
					and (m.usuario = :usuario or s.usuario = :usuario)
					and a not in(
						select age from AlteracoesAgendamentos aa
						join aa.agendamento age
						left join age.matricula m
						left join age.servidor s
						left join aa.usuarioDestino ud
						left join aa.usuarioOrigem uo
						where (ud = m.usuario or uo = m.usuario or ud = s.usuario or uo = s.usuario)
						and aa.aceito is null
					)
					""")
				.setParameter("usuario", u)
				.setParameter("dtIni", data)
				.setParameter("dtFim", data.plusDays(1))
				.getResultList();

		lista.forEach(a -> {
			if(a.isAindaPodeTranferir()) {
				tranferifevis.add(a);
			}
		});

		return tranferifevis;
	}
}