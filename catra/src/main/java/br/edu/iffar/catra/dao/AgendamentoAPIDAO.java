package br.edu.iffar.catra.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;

@SuppressWarnings("unchecked")
public class AgendamentoAPIDAO extends APIDAO<APIAgendamento>{
	
	public APIAgendamento find(APIAgendamento agendamento) {
    	this.begin();
    	APIAgendamento t = this.em.find(APIAgendamento.class, agendamento);
    	this.commit();
    	return t;
    }
	
	public APIAgendamento findByUserAndDateAndTipoRefeicao(APIUsuario user,LocalDate date,TipoRefeicao tipoRefeicao) {
    	this.begin();
		Query q = this.em.createQuery("""
							from APIAgendamento a
							inner join fetch a.usuario u
							inner join fetch a.refeicao r
							inner join fetch r.tipoRefeicao tr
							where u.username = :username
							and tr = :tipoRefeicao
							and :dateToday = a.dtAgendamento
				""");
    	
		q.setParameter("dateToday", LocalDate.now());
    	q.setParameter("username", user.getUsername());
    	q.setParameter("tipoRefeicao", tipoRefeicao);
    	APIAgendamento a = null;
    	try {
        	 a = (APIAgendamento) q.getSingleResult();			
		} catch (NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		}

    	this.commit();
    	return a;
    }
	
	public List<APIAgendamento> listAllAgendamentoNotSync() {
		List<APIAgendamento> l = null;
		this.begin();
		
        l = this.em.createQuery("""
				select a from APIAgendamento a
				left join APIAgendamentosDisponibilizados ad on ad.agendamento = a and ad is null
				join fetch a.refeicao r
				join fetch r.tipoRefeicao tr
				join fetch a.usuario au
				join fetch a.credito c
				join fetch c.usuario u
				join fetch c.tipoCredito tc
				where 
				c.sincronizado is null  
        """).getResultList();
		
		this.commit();
		
        return l;
    }

	/**
	 * metodo precisa estar dentro de uma transação
	 * 
	 * @param list
	 * @param data
	 * @return
	 */
	public List<APIAgendamento> listAllAgendamentoToDelete(List<APIAgendamento> list, LocalDate data) {

		return this.em.createQuery("""
				select a from APIAgendamento a
				left join APIAgendamentosDisponibilizados ad on ad.agendamento = a
				where a.credito is null
				and ad is null
				and a.dtAgendamento >= :data
				and a not in :list
				""")
				.setParameter("list", list)
				.setParameter("data", data)
				.getResultList();
	}

	/**
	 * metodo precisa estar dentro de uma transação
	 * 
	 * @param list
	 * @param data
	 * @return
	 */
	public List<APIAgendamentosDisponibilizados> listAllAgendamentoDisponibilizadoToDelete(List<APIAgendamento> list, LocalDate data) {

		return this.em.createQuery("""
				select ad from APIAgendamentosDisponibilizados ad
				join fetch ad.agendamento a
				where a.credito is null
				and a.dtAgendamento >= :data
				and a not in :list


				   """).setParameter("list", list).setParameter("data", data).getResultList();

	}

	/**
	 * metodo deve estar detro de transação
	 * 
	 * @param ids
	 * @param data
	 * @return
	 */
	public int delete(List<UUID> ids, LocalDate data) {
		int retorno = this.em.createNativeQuery(
				"""
						delete	from agendamentos a
						where a.credito_id is null and
						a.agendamento_id not in :lista
						and a.dt_agendamento >= :data
				""")
		.setParameter("lista", ids)
		.setParameter("data", data)
		.executeUpdate();

		return retorno;
	}
}
