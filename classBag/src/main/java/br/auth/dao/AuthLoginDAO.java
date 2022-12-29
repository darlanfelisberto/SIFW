package br.auth.dao;

import java.util.UUID;

import br.auth.models.AuthLogin;
import br.edu.iffar.fw.classBag.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@RequestScoped
public class AuthLoginDAO extends DAO<AuthLogin>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthLogin findAuthLoginByCode(String code){
        try {
            return (AuthLogin) this.em.createQuery("""
                    select a from AuthLogin a 
                    join fetch a.cliente c
                    join fetch a.usuario u
                    left join fetch u.listPermissao 
                    where a.usado = false and a.code = :code
                """).setParameter("code",UUID.fromString(code))
                    .getSingleResult();
        }catch (NoResultException e){}

        return null;
    }

}
