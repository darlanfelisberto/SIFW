package br.edu.iffar.catra.dao;

import java.time.LocalDate;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APICredito;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;

public class APICreditoDAO extends APIDAO<APICredito> {

//    @SuppressWarnings("unchecked")
//	public List<APICredito> listAllNotSync() {
//        List<APICredito> l = Factory.current.createQuery("""
//				select c from APICredito c 
//				join fetch c.usuario u
//				join fetch c.tipoCredito tc
//				join fetch c.agendamento a
//				join fetch a.usuario au
//				join fetch a.refeicao r
//				join fetch r.tipoRefeicao tr
//				join fetch c.usuario uc
//				left join APIAgendamentosDisponibilizados ad on ad.agendamento = a
//				where 
//				ad is null and c.sincronizado is null
//        """).getResultList();
//
//        return l;
//    }
    

	public APICredito existeSaidaCreditoParaHoje(APIUsuario user, TipoRefeicao tipoRefeicao){
		try {
			this.begin();
			Query q = this.em.createQuery("""
					select c from APICredito c
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
			this.commit();
			
			return (APICredito) oo;
		} catch (NoResultException e) {
			return null;
		}
	}

}
