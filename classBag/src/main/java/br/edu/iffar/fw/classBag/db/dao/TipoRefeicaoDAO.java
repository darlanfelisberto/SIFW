package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class TipoRefeicaoDAO extends DAO<TipoRefeicao> {
	
	private static final long serialVersionUID = 22021991L;

	@SuppressWarnings("unchecked")
	public List<TipoRefeicao> listAll(){
		return this.em.createQuery("from TipoRefeicao tr order by tr.descricao asc").getResultList();
	}
}