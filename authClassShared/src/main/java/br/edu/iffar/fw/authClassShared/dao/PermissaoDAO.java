package br.edu.iffar.fw.authClassShared.dao;

import java.util.List;

import br.edu.iffar.fw.authClassShared.models.Permissao;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

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
