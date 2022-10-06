package br.edu.iffar.catra.dao;

import java.util.List;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;

@SuppressWarnings("unchecked")
public class APITipoRefeicaoDAO extends APIDAO<TipoRefeicao>{
	
	public TipoRefeicao find(UUID tipoRefeicao) {
    	this.begin();
    	TipoRefeicao t = this.em.find(TipoRefeicao.class, tipoRefeicao);
    	this.commit();
    	return t;
    }
	
	public List<TipoRefeicao> listAll() {
    	this.begin();
    	List<TipoRefeicao> l = this.em.createQuery("from TipoRefeicao t order by t.descricao").getResultList();
    	this.commit();
    	return l;
    }
	
	public TipoRefeicao[] listAllArray() {
		this.begin();
    	List<TipoRefeicao> l = this.em.createQuery("from TipoRefeicao t order by t.descricao").getResultList();
    	this.commit();
    	
    	TipoRefeicao[] t = new TipoRefeicao[0];
    	if(l != null) {
	    	t = new TipoRefeicao[l.size()];
			l.toArray(t);
    	}
		
    	return t;
    }
	
	
}
