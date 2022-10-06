
package br.edu.iffar.catra.control;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.RollbackException;
import javax.ws.rs.NotAuthorizedException;

import br.edu.iffar.catra.comunica.KeycloakSession;
import br.edu.iffar.catra.dao.UsuarioOffLineDAO;
import br.edu.iffar.catra.forms.Splat;
import br.edu.iffar.fw.classBag.db.model.api.UsuarioOffLine;
import br.edu.iffar.fw.classBag.sec.Pbkdf2Hash;

/**
 * 
 * @author darlan
 *
 */
public class LoginControl {

	private static Map<String, String> parameters = new HashMap<>();

	static {
		parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
		parameters.put("Pbkdf2PasswordHash.Iterations", "27500");
		parameters.put("Pbkdf2PasswordHash.KeySizeBytes", "64");
		parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "16");
	}

	private UsuarioOffLineDAO usuarioOffLineDAO = new UsuarioOffLineDAO();

	public void loginOnLine(String usuario, String senha) throws RollbackException, Exception, NotAuthorizedException {

		if(usuario.isEmpty() || senha.isEmpty()) {
			return;
		}
		KeycloakSession.getCatracaInstance().loginOnline(usuario, senha);

		Pbkdf2Hash c = new Pbkdf2Hash();
		c.initialize(parameters);

		UsuarioOffLine u = this.usuarioOffLineDAO.findUsuaroByUnername(usuario);

//			cria usuario se nao existir
		if(u == null) {
			u = new UsuarioOffLine(usuario);

			u.setSalt(c.generateSalt());
			u.setHash(c.generate(senha, u.getSalt()));
		}
//			atualiza senha
		if(!c.verify(u.getHash(), c.generate(senha, u.getSalt()))) {
			u.setSalt(c.generateSalt());
			u.setHash(c.generate(senha, u.getSalt()));
		}

		this.usuarioOffLineDAO.salvar(u);

		if(Splat.getinstance().getProgress() != 100) {
			return;
		}
	}

	public void loginOffLine(String usuario, String senha) throws RollbackException, Exception, NotAuthorizedException {
		Pbkdf2Hash c = new Pbkdf2Hash();

		c.initialize(parameters);

		UsuarioOffLine u = this.usuarioOffLineDAO.findUsuaroByUnername(usuario);

		if(u == null) {
			throw new NotAuthorizedException("Usuário ou senha não encontrado!");
		}

		if(c.verify(u.getHash(), c.generate(senha, u.getSalt()))) {
			KeycloakSession.getCatracaInstance().loginOffLine();
			PrincipalControl.getPrincipalControl().usarApiOffLine(u.getUsername());
		} else {
			throw new NotAuthorizedException("Usuário ou senha não encontrado!");
		}
	}

}
