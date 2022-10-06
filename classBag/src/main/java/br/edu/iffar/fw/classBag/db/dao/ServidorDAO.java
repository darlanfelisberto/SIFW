package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

//import org.keycloak.KeycloakSecurityContext;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.Usuario;

@RequestScoped
@SuppressWarnings("unchecked")
public class ServidorDAO extends DAO<Servidor> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<Servidor> listServidorByUsuario(Usuario usuario) {
		return this.em.createQuery("""
			from Servidor s 
			join fetch s.usuario u
			join fetch s.tipoVinculo tv
			where u = :usuario 
		""")
		.setParameter("usuario", usuario)
		.getResultList();
	}
	
	public Servidor findServidorBySiape(Integer siape) {
		try {
			return this.em.createQuery("""
					from Servidor s where s.siape = :siape 
				""",Servidor.class)
				.setParameter("siape", siape)
				.getSingleResult();	
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * retorna true se o servidor pode ser removido e false caso a busca retorne alguma coisa
	 * 
	 * @param servidor
	 * @return boolean
	 */
	public boolean poderRemover(Servidor servidor) {
		try {
			this.em.createQuery("""
					select 1 from Agendamento a left join a.servidor s where s = :servidor   
				""")
				.setParameter("servidor", servidor)
				.setMaxResults(1)
				.getSingleResult();
			return false;
		} catch (NoResultException e) {
			return true;
		}
	}
}