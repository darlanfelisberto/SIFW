package br.edu.iffar.fw.classBag.db.dao.api;

import java.util.List;

import br.edu.iffar.fw.classBag.db.model.api.APIImagen;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;

@RequestScoped
public class JAXImagenDAO extends DAO<APIImagen> {

	private static final long serialVersionUID = 22021991L;


	@SuppressWarnings("unchecked")
	public List<APIImagen> listAll() {
		Query q = this.em.createQuery("from APIImagen u join FETCH u.usuario");
		
		return q.getResultList();
	}
	
}
