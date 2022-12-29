package br.edu.iffar.fw.classBag.db.dao;


import java.util.UUID;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import jakarta.enterprise.context.ApplicationScoped;

@SuppressWarnings("rawtypes") 
@ApplicationScoped
public class ConverterModelDAO extends DAO<Model<?>> {
	
	private static final long serialVersionUID = 22021991L;
	
	
	@SuppressWarnings("unchecked")
	public Model findBd(Class entity,Object id){
        return (Model)  em.find(entity, id );
    }
	
	public Model findBd(Class entity,Class gerics,String id){
		if(gerics.equals(UUID.class)) {
			return this.findBd(entity, UUID.fromString(id));
		}
        return  this.findBd(entity, gerics.cast(id) );
    }

}