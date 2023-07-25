package br.edu.iffar.fw.authClassShared.dao;

import br.edu.iffar.fw.authClassShared.models.AuthUser;
import br.edu.iffar.fw.authClassShared.models.Usuario;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@RequestScoped
public class AuthUserDAO extends DAO<AuthUser>{

    public AuthUser findByUsername(String username){
        try {
            return (AuthUser) this.em.createQuery("""
                    select u from AuthUser u
                    left join fetch a.usuario us 
                    where u.username = :username
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
