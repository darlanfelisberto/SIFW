package br.edu.iffar.fw.classBag.db.dao.api;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.api.APIImagen;

@RequestScoped
public class JAXImagenDAO extends DAO<APIImagen> {

	private static final long serialVersionUID = 22021991L;


	@SuppressWarnings("unchecked")
	public List<APIImagen> listAll() {
		Query q = this.em.createQuery("from APIImagen u join FETCH u.usuario");
		
		return q.getResultList();
	}
	
}
