package br.edu.iffar.fw.comendo.bean.fragment;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.VinculosAtivosUsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import br.edu.iffar.fw.comendo.SessionDadaBean;
import br.edu.iffar.fw.comendo.bean.HeaderBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class VinculoSelecionadoBean implements Serializable {

	private static final long serialVersionUID = 2022021991L;
	
	private static final String VINCULO = "VINCULO";
	
	@Inject private HeaderBean headerBean;
	@Inject private VinculosAtivosUsuariosDAO vinculosAtivosUsuariosDAO;
	@Inject private SessionDadaBean dadaBean;
	@Inject private HttpServletRequest request;
	
	private List<VinculosAtivosUsuarios> lisVinculosAtivosUsuarios = null;
	
		public boolean isNecessarioSelecionarVinculo(Usuario u) throws RuntimeException{
		
		if(this.dadaBean.getData(VINCULO) != null) {
			return false;
		}
		
		this.lisVinculosAtivosUsuarios = this.vinculosAtivosUsuariosDAO.listVinculosAtivos(u);
		
		if(this.lisVinculosAtivosUsuarios == null || this.lisVinculosAtivosUsuarios.size() ==0) {
			//throw new RuntimeException("Usuário não possui vinculo como aluno ou servidor.");
			return true;
		}
		
		if(this.lisVinculosAtivosUsuarios.size() ==1) {
			this.dadaBean.putData(VINCULO, this.lisVinculosAtivosUsuarios.get(0));
			return false;
		}else {
			return true;
		}
	}
	
	public void redirect() {		
		try {
			FacesContext.getCurrentInstance().getExternalContext()
			.redirect("selecionaVinculo.xhtml?faces-redirect=true&return=" + request.getServletPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void redirectReturn() {
		
	}
	
	public String selecionouVinculo(VinculosAtivosUsuarios vinc) {
		this.dadaBean.putData(VINCULO, vinc);
		return request.getParameter("return")+"?faces-redirect=true";
	}
	
	public List<VinculosAtivosUsuarios> getListVinculosAtivosUsuario(){
		return this.vinculosAtivosUsuariosDAO.listVinculosAtivos(headerBean.getUsuarioLogado());
	}
	
	public VinculosAtivosUsuarios getVinculoSelecionado() {
		return (VinculosAtivosUsuarios) this.dadaBean.getData(VINCULO);
	}
}
