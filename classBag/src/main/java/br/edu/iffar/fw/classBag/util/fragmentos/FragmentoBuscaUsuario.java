package br.edu.iffar.fw.classBag.util.fragmentos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@ViewScoped
public class FragmentoBuscaUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject UsuariosDAO usuariosDAO;
	@Inject MessagesUtil messages;

	private List<Usuario> listUsuarios;
	SelecionaFragmentoInterface bean;
	private boolean rendBusca;
	private boolean rendResultadoBusca;
	private boolean rendDadosUsuario;
	private int tipoBusca = 1;
	
	
	private String busca = "";
	private Usuario user;
	
	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();

	@PostConstruct
	private void init() {
		this.listSelectIten.add(new SelectItem(1, "CPF"));
		this.listSelectIten.add(new SelectItem(4, "Nome"));
	}
	
	public void init(SelecionaFragmentoInterface bean) {
		this.bean = bean;
	}
	
	public void buscarUsuarioProCpf() {
    	
		if(this.tipoBusca == 1) {
			this.user = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));
			if(this.user == null) {
				this.messages.addError("Usuário não foi encontrado.");
				return;
			}
		} else if(this.tipoBusca == 4) {// nome
			if(this.busca.trim().length() < 3) {
				this.messages.addError("Informe 3 caracteres para iniciar a busca.");
				return;
			}

			this.listUsuarios = this.usuariosDAO.listAllUsersByName(this.busca);
			this.bean.telaResultadoBusca();
		}
	}
	
	public void telaBusca() {
		this.rendBusca = true;
		this.rendResultadoBusca = false;
		this.rendDadosUsuario = false;
	}

	public void telaResultadoBusca() {
		this.rendBusca = false;
		this.rendResultadoBusca = true;
		this.rendDadosUsuario = false;
	}
	
	public void telaDadosUsuario() {
		this.rendBusca = false;
		this.rendResultadoBusca = false;
		this.rendDadosUsuario = true;
	}

	public boolean isRendBusca() {
		return rendBusca;
	}

	public void setRendBusca(boolean rendBusca) {
		this.rendBusca = rendBusca;
	}

	public boolean isRendResultadoBusca() {
		return rendResultadoBusca;
	}

	public void setRendResultadoBusca(boolean rendResultadoBusca) {
		this.rendResultadoBusca = rendResultadoBusca;
	}

	public boolean isRendDadosUsuario() {
		return rendDadosUsuario;
	}

	public void setRendDadosUsuario(boolean rendDadosUsuario) {
		this.rendDadosUsuario = rendDadosUsuario;
	}

	public int getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(int tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}

	public void setListSelectIten(List<SelectItem> listSelectIten) {
		this.listSelectIten = listSelectIten;
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<Usuario> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}
}
