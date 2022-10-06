package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.enun.TypeCredito;

@RequestScoped
public class TipoCreditoDAO extends DAO<TipoCredito> {
	
	private static final long serialVersionUID = 22021991L;

	@SuppressWarnings("unchecked")
	public List<TipoCredito> listAll(){
		return this.em.createQuery("from TipoCredito tr order by tr.descricao asc").getResultList();
	}
	
	public TipoCredito getTipoCreditoByTypeCredito(TypeCredito typeCredito) {
		return (TipoCredito) this.em.createQuery("from TipoCredito tc where tc.tipoCreditoId = :tipo").setParameter("tipo", typeCredito).getSingleResult();
	}
}