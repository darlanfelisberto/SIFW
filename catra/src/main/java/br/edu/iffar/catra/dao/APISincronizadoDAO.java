package br.edu.iffar.catra.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.api.APISincronizado;

public class APISincronizadoDAO extends APIDAO<APISincronizado> {

    public APISincronizado getUltimoSinc() {
    	APISincronizado a = null;
    	try {
    		this.begin();
            Query q = this.em.createQuery("""
                from APISincronizado a
                where a.dtSincronizado = (select max(aa.dtSincronizado)from APISincronizado aa)
            """);

            try {
                a = (APISincronizado) q.getSingleResult();
            } catch (NoResultException e) {
                System.out.println(e.getMessage());
                a = new APISincronizado();
            }

            this.commit();
		} catch (Exception e) {
			return new APISincronizado();
		}
        
        return a;
    }
}
