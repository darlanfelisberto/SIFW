package br.auth.dao;

import br.auth.models.AuthUser;
import br.edu.iffar.fw.classBag.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@RequestScoped
public class UsuarioDAO extends DAO<AuthUser>{

    public AuthUser findUsuarioByUsername(String username){
        try {
            return (AuthUser) this.em.createQuery("""
                    select u from AuthUser u where u.username = :username
                """).setParameter("username",username)
                    .getSingleResult();
        }catch (NoResultException e){}

        return null;
    }

}
