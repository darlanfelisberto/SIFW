package br.edu.iffar.catra.dao;

import br.edu.iffar.catra.util.Log;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;

public class APISaldoDAO extends APIDAO<APISaldo>{
	
	public APISaldo findSaldoByUsername(String username) {
		APISaldo saldo = null;
		try {
			this.begin();
			saldo = (APISaldo) this.em.createQuery("from APISaldo s join fetch s.usuario u where u.username = :username")
					.setParameter("username", username).getSingleResult();
			this.commit();
		} catch (javax.persistence.NoResultException e) {
			Log.info("Usuário " + username +" não encontrado.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return saldo;
	}
	
	public APISaldo findSaldoByTokenRU(String tokenRU) {
		APISaldo saldo = null;
		try {
			this.begin();
			saldo = (APISaldo) this.em.createQuery("from APISaldo s join fetch s.usuario u where u.tokenRu = :tokenRu")
					.setParameter("tokenRu", tokenRU).getSingleResult();
			this.commit();
		} catch (javax.persistence.NoResultException e) {
			Log.info("Token RU " + tokenRU +" não encontrado.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return saldo;
	}
}
