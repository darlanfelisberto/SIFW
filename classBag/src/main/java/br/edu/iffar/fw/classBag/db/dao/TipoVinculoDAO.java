package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;

@SuppressWarnings("unchecked")
@RequestScoped
public class TipoVinculoDAO extends DAO<TipoVinculo> {
	
	private static final long serialVersionUID = 22021991L;

	public List<TipoVinculo> listAll(){
		return this.em.createQuery("from TipoVinculo tr order by tr.descricao asc").getResultList();
	}
	
	public List<TipoVinculo> listTipoVinculoAlunos(){
		return this.em.createQuery("from TipoVinculo tv where tv.tipoMatricula = true order by tv.descricao asc").getResultList();
	}
	
	public List<TipoVinculo> listTipoVinculoServidores(){
		return this.em.createQuery("from TipoVinculo tv where tv.tipoUsuario = true order by tv.descricao asc").getResultList();
	}
	
	public List<TipoVinculo> listTipoVinculoUsuarios(){
		return this.em.createQuery("from TipoVinculo tr where tr.tipoUsuario = true order by tr.descricao asc").getResultList();
	}
}