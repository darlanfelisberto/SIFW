package br.edu.iffar.catra.dao;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.api.APIImagen;

public class APIImagemDAO extends APIDAO<APIImagen>{
	
	public APIImagen update(APIImagen img) throws IOException {
		this.begin();
		img.deleteFileInDisck();
		img.salveFileInDisck();
		img = super.salvarT(img);
		this.commit();
		return img;
	}
	
	public APIImagen findByUsername(String username) {
		this.begin();
		APIImagen img = null;
		try {
			Query q = this.em.createQuery("from APIImagen a where a.usuario.username = :username");
			q.setParameter("username", username);
			img = (APIImagen)q.getSingleResult();
		}catch (NoResultException e) {
//			Log.info(e.getMessage());
		}
		if (img == null) {
			img = APIImagen.SEMIMAGEN;
        }
		this.commit();
		return img;
	}

}
