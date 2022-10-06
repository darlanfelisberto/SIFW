package br.edu.iffar.fw.classBag.db.dao;

import javax.enterprise.context.RequestScoped;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Parametros;
import br.edu.iffar.fw.classBag.enun.TypeParam;

@RequestScoped
public class ParametrosDAO extends DAO<Parametros> {
		
	private static final long serialVersionUID = 22021991L;
	
	
	public Parametros findParametroByTypeParam(TypeParam param){
		return (Parametros) this.em.createQuery(" from Parametros where parametroId = :typeParam").setParameter("typeParam", param).getSingleResult();
	}

}