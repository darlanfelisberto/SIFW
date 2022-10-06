package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;

@RequestScoped
public class TipoRefeicaoDAO extends DAO<TipoRefeicao> {
	
	private static final long serialVersionUID = 22021991L;

	@SuppressWarnings("unchecked")
	public List<TipoRefeicao> listAll(){
		return this.em.createQuery("from TipoRefeicao tr order by tr.descricao asc").getResultList();
	}
}