package br.edu.iffar.fw.classBag.db.dao;

import jakarta.enterprise.context.RequestScoped;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Parametros;
import br.edu.iffar.fw.classBag.enun.TypeParam;

@RequestScoped
public class ParametrosDAO extends DAO<Parametros> {
		
	private static final long serialVersionUID = 22021991L;
	
	
	public Parametros findParametroByTypeParam(TypeParam param){
		return (Parametros) this.em.createQuery("select p from Parametros p where p.parametroId = :typeParam").setParameter("typeParam", param).getSingleResult();
	}

}