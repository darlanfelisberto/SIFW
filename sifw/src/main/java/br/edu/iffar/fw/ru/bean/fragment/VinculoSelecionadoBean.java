package br.edu.iffar.fw.ru.bean.fragment;

import static br.edu.iffar.fw.classBag.db.SessionDataStore.VINCULO_SELECIONDO;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.dao.VinculosAtivosUsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Named
@RequestScoped
public class VinculoSelecionadoBean implements Serializable {

	private static final long serialVersionUID = 2022021991L;

	@Inject private VinculosAtivosUsuariosDAO vinculosAtivosUsuariosDAO;
	@Inject private SessionDataStore sessionDataStore;
	@Inject private HttpServletRequest request;
	@Inject private HttpServletResponse response;
	
	private List<VinculosAtivosUsuarios> lisVinculosAtivosUsuarios = null;

	public boolean isNecessarioSelecionarVinculo() throws RuntimeException{
		if(this.sessionDataStore.getData(VINCULO_SELECIONDO) != null){
			return false;
		}

		this.lisVinculosAtivosUsuarios = this.getListVinculosAtivosUsuario();
		if(this.lisVinculosAtivosUsuarios.size() == 1) {
			this.sessionDataStore.putData(VINCULO_SELECIONDO, this.lisVinculosAtivosUsuarios.get(0));
			return false;
		}

		if(this.lisVinculosAtivosUsuarios == null || this.lisVinculosAtivosUsuarios.size() > 1) {
			//throw new RuntimeException("Usuário não possui vinculo como aluno ou servidor.");
			return true;
		}

		return false;
	}
	
	public void redirect() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().redirect(request.getContextPath() + "/app/selecionaVinculo.xhtml?faces-redirect=true&return="+ request.getServletPath());
			fc.responseComplete();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String selecionouVinculo(VinculosAtivosUsuarios vinc) {
		this.sessionDataStore.putData(VINCULO_SELECIONDO, vinc);
		return request.getParameter("return")+"?faces-redirect=true";
	}
	
	public List<VinculosAtivosUsuarios> getListVinculosAtivosUsuario(){
		if(this.lisVinculosAtivosUsuarios == null){
			this.lisVinculosAtivosUsuarios = this.vinculosAtivosUsuariosDAO.listVinculosAtivos(sessionDataStore.getUsuarioLogado());
		}
		return lisVinculosAtivosUsuarios;
	}
	
	public VinculosAtivosUsuarios getVinculoSelecionado() {
		return (VinculosAtivosUsuarios) this.sessionDataStore.getData(VINCULO_SELECIONDO);
	}
}
