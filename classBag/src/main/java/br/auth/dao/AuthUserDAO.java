package br.auth.dao;

import br.auth.models.AuthUser;
import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;

@RequestScoped
public class AuthUserDAO extends DAO<AuthUser>{

    public AuthUser findUsuarioByUsername(String username){
        try {
            return (AuthUser) this.em.createQuery("""
                    select u from AuthUser u where u.username = :username
                """).setParameter("username",username)
                    .getSingleResult();
        }catch (NoResultException e){}

        return null;
    }

    public AuthUser findAuthUser(Usuario usuario){
        try {
            return (AuthUser) this.em.createQuery("""
                select a from AuthUser a
                left join fetch a.usuario u
                left join fetch a.setPermissao p
                where u = :usuario
            """).setParameter("usuario",usuario)
                        .getSingleResult();
        }catch (NoResultException e){}
            return null;
    }
}
