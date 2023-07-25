package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;

@RequestScoped
@SuppressWarnings("unchecked")
public class ServidorDAO extends DAO<Servidor> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<Servidor> listServidorByUsuario(Usuario usuario) {
		return this.em.createQuery("""
			select s from Servidor s 
			left join fetch s.usuario u 
			left join fetch s.tipoVinculo  tv
			where u = :usuario 
		""", Servidor.class)
		.setParameter("usuario", usuario)
		.getResultList();
	}
	
	public Servidor findServidorBySiape(Integer siape) {
		try {
			return this.em.createQuery("""
					select s from Servidor s where s.siape = :siape 
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