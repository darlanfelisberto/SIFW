package br.edu.iffar.fw.classBag.db.dao;

import javax.enterprise.context.RequestScoped;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;

@RequestScoped
public class LogDAO extends DAO<Model<?>> {
	
	private static final long serialVersionUID = 22021991L;
	
	@Transactional
	public void log(Model<?> log) throws RollbackException {
		this.persist(log);
	}
	
}