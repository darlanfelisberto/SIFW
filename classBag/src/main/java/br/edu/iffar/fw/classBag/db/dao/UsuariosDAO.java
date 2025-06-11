package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.com.feliva.sharedClass.db.DAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import org.wildfly.security.http.oidc.OidcSecurityContext;

import br.edu.iffar.fw.classBag.db.model.Saldo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class UsuariosDAO extends DAO<Usuario> {
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private OidcSecurityContext oidcSecurityContext;

	@SuppressWarnings("unchecked")
	public List<Usuario> listAllUsers() {
		List<Usuario> u = this.em.createQuery("""
			select u from Usuario u
			left join fetch u.pessoa p 
			order by unaccent(p.nome) asc
		"""
		).getResultList();
		return u;
	}

	public Usuario findByUserName(String userName) {
		try {
			return (Usuario) this.em.createQuery("""
  				from Usuario u 
  				left join fetch u.pessoa p
				left join fetch p.authUser au
  				where au.username = :username
			""")
			.setParameter("username", userName)
			.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("NoResultException.");
			return null;
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listAllUsersBySubNomeCpf(String query) {
		String n = "%"+query+"%";
		return this.em.createQuery("""
				select u from Usuario u
				left join fetch u.pessoa p 
				where cast(unaccent(p.nome) as String) ilike cast(unaccent(:nome) as String) or p.cpf ilike :cpf order by unaccent(p.nome) asc
				""")
				.setParameter("nome", n.toLowerCase())
				.setParameter("cpf", n)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listAllUsersByName(String query) {
		String n = ""+query+"";
		return this.em.createQuery("""
				select u from Usuario u
				left join fetch u.pessoa p
				where cast(unaccent(lower(p.nome)) as String) like cast(unaccent(:nome) as String)
				order by cast(unaccent(p.nome) as String) asc
				""").setParameter("nome", n.toLowerCase())
				.getResultList();
	}
	
	public Usuario getUsuarioByCPF(String cpf) {
		try {
			return (Usuario) this.em.createQuery("""
				select u from Usuario u
				left join fetch u.pessoa p
				where p.cpf = :cpf"""
			).setParameter("cpf", cpf.trim()).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Usuario logado, NoResultException.");
		}
		return null;
	}

	public Usuario getUsuarioByUserName(String userName) {
		try {
			return (Usuario) this.em.createQuery("""
					select u from Usuario u 
					left join fetch u.pessoa p
					left join fetch p.authUser au
					where au.username = :userName
					""")
					.setParameter("userName", userName)
					.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Usuario logado, NoResultException.");
		}
		return null;
	}
	
	public Usuario findUsuarioByToken(String token) {
		try {
			return (Usuario) this.em.createQuery("""
					select u from Usuario u where u.tokenRU = :token
					""")
					.setParameter("token", token)
					.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Usuario logado, NoResultException.");
		}
		return null;
	}
	
	public Usuario getUsuarioLogado() throws NoResultException{
		try {
			if(this.oidcSecurityContext == null) {
				return null;
			}
			
			return (Usuario) this.em.createQuery("""
						select u from Usuario u 
						left join fetch u.pessoa p
						left join fetch p.authUser au
						where au.username = :userName
					""")
					.setParameter("userName", oidcSecurityContext.getIDToken().getPreferredUsername())
					.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("Usuario logado, NoResultException.");
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getUsernameLogado() {
		return this.oidcSecurityContext.getIDToken().getPreferredUsername();
	}

	@Transactional
	public int generatedTokenRUForAll(boolean generateTokenForAll) {
		String sql = "update usuarios set token_ru = uuid_generate_v4() "+(generateTokenForAll?"":" where token_ru is null");
		Query q =  this.em.createNativeQuery(sql);
		return q.executeUpdate();
	}

}