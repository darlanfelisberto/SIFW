package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.TipoUnidade;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
@SuppressWarnings("unchecked")
public class TipoUnidadeDAO extends DAO<TipoUnidade> {

	private static final long serialVersionUID = 1L;


	public List<TipoUnidade> listAllTipoUnidades() {
		return this.em.createQuery("from TipoUnidade ti order by ti.desc").getResultList();
	}
}
