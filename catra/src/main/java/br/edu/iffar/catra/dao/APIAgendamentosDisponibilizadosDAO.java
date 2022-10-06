package br.edu.iffar.catra.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;

@SuppressWarnings("unchecked")
public class APIAgendamentosDisponibilizadosDAO extends APIDAO<APIAgendamentosDisponibilizados>{
	
	public APIAgendamentosDisponibilizadosDAO(EntityManager em) {
		super();
		super.em = em;
	}

	public APIAgendamentosDisponibilizadosDAO() {
		super();
	}

	public List<APIAgendamentosDisponibilizados> listAllAgeDisponibilizadoByData(LocalDate data, TipoRefeicao tipoRefeicao) {
        this.begin();
		Query q = this.em.createQuery("""
				from APIAgendamentosDisponibilizados ad
				join fetch ad.agendamento a
				join fetch a.refeicao r
				join fetch r.tipoRefeicao tr
				join fetch ad.usuarioDisponibilizou ud
				where ad.sincronizado is null
				and ad.usuarioPegouId is null 
				and tr = :tipoRefeicao
								and a.dtAgendamento = :data
				order by ud.nome asc
		""");
		
		q.setParameter("tipoRefeicao", tipoRefeicao);
		q.setParameter("data", data);
		
		List<APIAgendamentosDisponibilizados> retorno = q.getResultList();
		
		this.commit();
		
		return retorno;
	}
	
	public List<APIAgendamentosDisponibilizados> listNotSyncAgeDisp(){
		this.begin();
		Query q = this.em.createQuery("""
				select ad from APIAgendamentosDisponibilizados ad
				left join fetch ad.agendamento a
				left join fetch a.credito c
				left join fetch c.tipoCredito tc
				left join fetch a.refeicao r
				left join fetch r.tipoRefeicao tr
				left join fetch a.usuario u
				left join fetch ad.usuarioDisponibilizou ud
				where ad.sincronizado is null
				and ad.usuarioPegouId is not null
				""");
		List<APIAgendamentosDisponibilizados> retorno = q.getResultList();
		
		this.commit();
		
		return retorno;		
	}

}
