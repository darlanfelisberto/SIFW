package br.edu.iffar.catra.dao;

import br.edu.iffar.fw.classBag.db.model.api.UsuarioOffLine;

public class UsuarioOffLineDAO extends APIDAO<UsuarioOffLine> {

	public UsuarioOffLine findUsuaroByUnername(String username) {
		UsuarioOffLine user = null;

		try {
			this.begin();
			user = (UsuarioOffLine) this.em.createQuery("from UsuarioOffLine u where u.username = :username").setParameter("username", username).getSingleResult();
			this.commit();
		}
		catch (Exception e) {
			this.rollback();
		}
		
		return user;
	}

}
