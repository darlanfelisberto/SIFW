package br.edu.iffar.fw.classBag.db.dao.api;

import java.util.List;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@RequestScoped
public class JAXSaldoDAO extends DAO<APIUsuario> {

	private static final long serialVersionUID = 22021991L;


	@SuppressWarnings("unchecked")
	public List<APIUsuario> getListAllAPIUsuario(){
		Query q = this.em.createQuery("from APIUsuario");
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<APISaldo> listAllAPISaldo(){
		Query q = this.em.createQuery("""
				from APISaldo s
				join FETCH s.usuario u
				where u.ativo = true
				""");
		
		return q.getResultList();
	}
	
	public APISaldo findUserId(String username){
		APISaldo saldo = null;
		try {
			Query q = this.em.createQuery("from APISaldo s join FETCH s.usuario u where u.username = :username");
			q.setParameter("username", username);
			saldo = (APISaldo) q.getSingleResult();
		} catch (NoResultException  e) {
			
		}
		return saldo;
	}

	public APISaldo findUserByToken(String token) {
		APISaldo saldo = null;
		try {
			Query q = this.em.createQuery("from APISaldo s join FETCH s.usuario u where u.tokenRu = :token");
			q.setParameter("token", token);
			saldo = (APISaldo) q.getSingleResult();
		}
		catch (NoResultException e) {
		}
		return saldo;
	}
}
