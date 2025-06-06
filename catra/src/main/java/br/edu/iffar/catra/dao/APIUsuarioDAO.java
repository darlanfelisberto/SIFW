package br.edu.iffar.catra.dao;

import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;

public class APIUsuarioDAO extends APIDAO<APIUsuario>{

	public APIUsuario findUsuaroByUnername(String username) {
		APIUsuario user = null;
		
		this.begin();
		user = (APIUsuario) this.em.createQuery("from APIUsuario u where u.username = :username").setParameter("username", username).getSingleResult();
		this.commit();
		
		return user;
	}

	/**
	 * Este metodo necessita estar dentro de uma transação para funcionar.
	 * 
	 * Ele é usado na sincronização de usuarios, onde a transação é gerenciado por
	 * outro DAO2, usando o metodo da super classe joinTransaction.
	 * 
	 * @return int registos alterados
	 */
	public int disableAllApiUsuario() {
		
		int retorno = this.em.createQuery("update APIUsuario u set u.ativo = false ").executeUpdate();

		return retorno;
	}
}
