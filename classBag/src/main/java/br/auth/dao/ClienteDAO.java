package br.auth.dao;

import br.auth.models.Cliente;
import br.edu.iffar.fw.classBag.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@RequestScoped
public class ClienteDAO extends DAO<Cliente>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Cliente findClienteByName(String name){
		try {
	        return (Cliente) this.em.createQuery("""
	                    select c from Cliente c where c.nome = :name
	        """).setParameter("name",name)
	        .getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
    }

}
