package br.edu.iffar.fw.classBag.db.dao;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;

@RequestScoped
public class LogDAO extends DAO<Model<?>> {
	
	private static final long serialVersionUID = 22021991L;
	
	@Transactional
	public void log(Model<?> log) throws RollbackException {
		this.persist(log);
	}
	
}