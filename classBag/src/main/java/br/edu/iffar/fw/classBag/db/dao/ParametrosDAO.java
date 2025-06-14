package br.edu.iffar.fw.classBag.db.dao;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Parametros;
import br.edu.iffar.fw.classBag.enun.TypeParam;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ParametrosDAO extends DAO<Parametros> {
		
	private static final long serialVersionUID = 22021991L;
	
	
	public Parametros findParametroByTypeParam(TypeParam param){
		return (Parametros) this.em.createQuery("select p from Parametros p where p.parametroId = :typeParam").setParameter("typeParam", param).getSingleResult();
	}

}