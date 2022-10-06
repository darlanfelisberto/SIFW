package br.edu.iffar.fw.classBag.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;

@RequestScoped
public class VinculosAtivosUsuariosDAO {

	@Inject private MatriculaDAO matriculaDAO;
	@Inject private ServidorDAO servidorDAO;
	
	/**
	 * metodo retorna null caso nao seja encontrado nenhum vinculo para a o usuario
	 * 
	 * @param user
	 * @return
	 */
	public List<VinculosAtivosUsuarios> listVinculosAtivos(Usuario user){
		List<VinculosAtivosUsuarios> listV = new ArrayList<VinculosAtivosUsuarios>();

		listV.addAll(this.servidorDAO.listServidorByUsuario(user));
		
		for (Matricula matricula : this.matriculaDAO.listAllMatriculaByUsuario(user)) {
			if(matricula.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA)) {
				listV.add(matricula);
			}
		}
		
		if(listV.isEmpty()) {
			return null;
		}
		
		return listV;
	}
	
}
