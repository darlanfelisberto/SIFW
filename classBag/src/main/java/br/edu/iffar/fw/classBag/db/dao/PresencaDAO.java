package br.edu.iffar.fw.classBag.db.dao;

import java.time.LocalDate;
import java.util.List;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Presenca;
import br.edu.iffar.fw.classBag.db.model.Unidade;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class PresencaDAO extends DAO<Presenca> {
	
	private static final long serialVersionUID = 22021991L;
	

	@SuppressWarnings("unchecked")
	public List<Presenca> listAusenciaByDtReferenciaUnidade(LocalDate dtReferencia,Unidade unidade){
		return this.em.createQuery("""
				select au from Presenca au
				inner join fetch au.habitanteUnidade hu
				inner join fetch hu.matricula m
				inner join fetch m.usuario u
				where
				au.dtReferencia = :dtReferencia
				and hu.unidade = :unidade
				""")
		.setParameter("dtReferencia", dtReferencia)
		.setParameter("unidade", unidade)
		.getResultList();
	}
	
}