package br.edu.iffar.catra.dao;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class APIDAO<T> extends Factory{
	
	protected EntityManager em;
	
	public void joinTransaction(APIDAO<?> other) {
		other.setEntytyManager(em);
	}

	/**
	 * use joinTransaction para unir os daos em uma unica transação
	 * 
	 * @param EntityManager em
	 */
	private void setEntytyManager(EntityManager em) {
		this.em = em;
	}
	
	public void open() {
		if(this.em == null) {
			this.em = this.getEntityManager();
		}
		if(!em.isOpen()) {
			this.em.close();
			this.em = this.getEntityManager();
		}
	}
        
	public void begin() {
		this.open();
		if(!em.getTransaction().isActive()) {
			this.em.getTransaction().begin();
		}
	}
		
	public void rollback() {
		if(this.em != null && this.em.isOpen() && this.em.getTransaction().isActive()) {
			this.em.getTransaction().rollback();
			this.em.close();
		}
	}
	
	public void commit() {
		this.em.getTransaction().commit();
		this.em.close();
	}

	public T salvarT(T entity) {
        return this.em.merge(entity);
    }
	
	public void removeT(T entity) {
		this.em.remove(entity);
	}

	public T salvar(T entity) throws RollbackException,Exception{
		this.begin();
		T refresh  =  this.em.merge(entity);
        this.commit();	
        return refresh;
    }
    
}
