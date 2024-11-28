package br.edu.iffar.fw.classBag.db;

import java.io.Serializable;
import java.util.HashMap;

import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.interfaces.VinculosAtivosUsuarios;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;

@SessionScoped
public class SessionDataStore implements Serializable{

	public static String USUARIO_LOGADO = "0";
	public static String VINCULO_SELECIONDO = "1";
	public static String IMAGEN_USUARIO_LOGADO = "2";

	@Inject private UsuariosDAO usuariosDAO;

	private static final long serialVersionUID = 19910222L;
	
	private HashMap<String,Object> data =  new HashMap<String, Object>();

	
	public Object getData(String id) {
		return data.get(id);
	}
	
	public void putData(String id,Object object) {
		data.put(id,object);
	}

	public Usuario getUsuarioLogado(){
		Usuario u = (Usuario) data.get(USUARIO_LOGADO);
		if(u == null){
			u = this.usuariosDAO.getUsuarioLogado();
			data.put(USUARIO_LOGADO,u);
		}
		return u;
	}

	public VinculosAtivosUsuarios getVinculoSelecionado(){
		return (VinculosAtivosUsuarios) this.data.get(VINCULO_SELECIONDO);
	}

}
