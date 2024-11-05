package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;

import br.edu.iffar.fw.classBag.db.model.Usuario;
import org.primefaces.model.menu.BaseMenuModel;

import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.ru.bean.fragment.SaldoUserFrament;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CreditosBean implements Serializable,BreadCrumbControl {

	private static final long serialVersionUID = 22021991L;
	
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private HeaderBean headerBean;
	
	@Inject
	private SaldoUserFrament saldoUserF ;
	
	private BaseMenuModel tabMenuModel = new BaseMenuModel();	
		
	private Usuario user = null;
	private BreadCrumb breadCrumb;

	@PostConstruct
	private void init() {
		this.user = this.headerBean.getUsuarioLogado();
		this.saldoUserF.init(user,"creditosBean.saldoUserF");		
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
			.addItem("Meu Saldo", BreadCrumb.RAIZ)
			;
		this.breadCrumb.setAtivo(1);
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public BaseMenuModel getTabMenuModel() {
		return tabMenuModel;
	}

	public SaldoUserFrament getSaldoUserF() {
		return saldoUserF;
	}	
	
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	
}
