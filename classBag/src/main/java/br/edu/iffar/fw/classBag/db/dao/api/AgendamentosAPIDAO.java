package br.edu.iffar.fw.classBag.db.dao.api;

import java.time.LocalDate;
import java.util.List;

import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.com.feliva.sharedClass.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;

@RequestScoped
public class AgendamentosAPIDAO extends DAO<APIAgendamento> {

	private static final long serialVersionUID = 22021991L;


	@SuppressWarnings("unchecked")
	public List<APIAgendamento> getListAllAPIUsuario(){
		Query q = this.em.createQuery("from APIUsuario");
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<APIAgendamento> listFromDateStar(LocalDate dateStart){
		Query q = this.em.createQuery("select a from APIAgendamento a "
				+ "join fetch a.usuario u "
				+ "join fetch a.refeicao r "
				+ "join fetch r.tipoRefeicao tr "
				+ "where  :dateStart = a.dtAgendamento");
		q.setParameter("dateStart", dateStart);
		
		return q.getResultList();
	}
	
}
