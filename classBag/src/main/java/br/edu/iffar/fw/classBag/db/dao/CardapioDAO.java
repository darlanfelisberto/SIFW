package br.edu.iffar.fw.classBag.db.dao;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.RequestScoped;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Cardapio;

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
	
	public List<Cardapio> findLastCardapio(){
		String hql = "from Cardapio c\n"
				+ "order by c.dtInicio desc";
		
		return this.em.createQuery(hql).setMaxResults(5).getResultList();
	}
	
	public Cardapio findUltimoCardapio(){
		String hql = """
				from Cardapio c
				order by c.dtInicio desc
				""";
		
		return (Cardapio) this.em.createQuery(hql).setMaxResults(1).getSingleResult();
	}
	
	public List<Cardapio> listAll(){
		String hql = "from Cardapio c\n"
				+ "order by c.dtInicio desc";
		
		return this.em.createQuery(hql).getResultList();
	}

}