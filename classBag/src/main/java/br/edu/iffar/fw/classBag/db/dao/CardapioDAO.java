package br.edu.iffar.fw.classBag.db.dao;

import java.time.LocalDate;
import java.util.List;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Cardapio;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@SuppressWarnings("unchecked")
@RequestScoped
public class CardapioDAO extends DAO<Cardapio> {
		
	private static final long serialVersionUID = 22021991L;
	
	
	public List<Cardapio> listCardapioByData(LocalDate data){
		String hql = """
						from Cardapio c
						where :data between c.dtInicio and c.dtFim
						order by c.dtInicio
					""";
		
		return this.em.createQuery(hql).setParameter("data", data).getResultList();
	}

	public Cardapio findCardapioByData(LocalDate data){
		String hql = """
						select c from Cardapio c
						where :data between c.dtInicio and c.dtFim
						order by c.dtInicio
					""";
		Cardapio c = null;
		try {
			c = (Cardapio) this.em.createQuery(hql).setParameter("data", data).setMaxResults(1).getSingleResult();
		}catch (NoResultException e){

		}catch (Exception e){
			e.printStackTrace();
		}

		return c;
	}

	public List<Cardapio> findLastCardapio(){
		String hql = "from Cardapio c order by c.dtInicio desc";
		
		return this.em.createQuery(hql).setMaxResults(5).getResultList();
	}
	
	public Cardapio findUltimoCardapio(){
		String hql = """
				from Cardapio c
				order by c.dtInicio desc
				""";
		Cardapio c = null;
		try {
			c = (Cardapio) this.em.createQuery(hql).setMaxResults(1).getSingleResult();
		}catch (NoResultException e){

		}catch (Exception e){
			e.printStackTrace();
		}

		return c;
	}
	
	public List<Cardapio> listAll(){
		String hql = "from Cardapio c\n"
				+ "order by c.dtInicio desc";
		
		return this.em.createQuery(hql).getResultList();
	}

}