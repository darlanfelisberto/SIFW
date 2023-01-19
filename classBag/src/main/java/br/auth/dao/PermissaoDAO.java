package br.auth.dao;

import br.auth.models.AuthUser;
import br.auth.models.Permissao;
import br.edu.iffar.fw.classBag.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class PermissaoDAO extends DAO<Permissao>{

    public List<Permissao> findAll(){
        try {
            return  this.em.createQuery("""
                    select p from Permissao p 
                """).getResultList();
        }catch (NoResultException e){}

        return null;
    }
}
