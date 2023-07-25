package br.edu.iffar.fw.classBag.db.dao;

import static br.edu.iffar.fw.classBag.db.SessionDataStore.IMAGEN_USUARIO_LOGADO;

import java.io.IOException;

import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classShared.db.DAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@RequestScoped
public class ImagenDAO extends DAO<Imagen> {

	@Inject private SessionDataStore sessionDataStore;

	@Inject private UsuariosDAO usuariosDAO;
	public Imagen imagenUsuarioLogado() {

		Imagen i = (Imagen) sessionDataStore.getData(IMAGEN_USUARIO_LOGADO);
		if(i == null) {
			i = findByUsuarioIfNullPattern(usuariosDAO.getUsuarioLogado());
			sessionDataStore.putData(IMAGEN_USUARIO_LOGADO,i);
		}
		return i;
	}

	public Imagen findByUsuarioIfNullPattern(Usuario u) {
		Imagen i = null;
		try {
			Query q = this.em.createQuery("select i from Imagen i where i.usuario = :usuario").setParameter("usuario", u);
			i = (Imagen) q.getSingleResult();
		} catch (NoResultException e) {

		}
		if(i == null) {
			i = Imagen.SEMIMAGEN;
		}
		return i;
	}
	
	public Imagen findByUsuario(Usuario id) {
		try {
			Query q = this.em.createQuery("select i from Imagen i where i.usuario = :id").setParameter("id", id);
			return (Imagen) q.getSingleResult();
		} catch (NoResultException e) {

		}
		return null;
	}

	private static final long serialVersionUID = 22021991L;

	@Transactional(value = TxType.REQUIRES_NEW)
	public Imagen substituiImagen(Imagen atual) throws IOException, RollbackException {
		atual.deleteFileInDisck();
		atual.salveFileInDisck();
		return this.update(atual);
	}

}