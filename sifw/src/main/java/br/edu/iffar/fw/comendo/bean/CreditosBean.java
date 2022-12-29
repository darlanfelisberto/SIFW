package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;

import org.primefaces.model.menu.BaseMenuModel;

import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.comendo.bean.fragment.SaldoUserFrament;
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
//		this.createBreadCrumb();
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
		
//	@Transactional
//	public void sav() {
//		Usuario u = new Usuario();
//		u.setSenha("dasdas");
//		u.setUserName(UUID.randomUUID().toString().substring(0, 10));
//		
//		Credito c = this.user.addCredito(new Credito());
//		c.setTipoCredito(TypeCredito.ENTRADA);
//		c.setTipoRefeicao(TypeRefeicao.ALMOCO);
//		c.setValor(2.9F);
////		c.setUsuario(this.user);
//		c.setDtCredito(LocalDateTime.now());
//		
//		this.usuariosDAO.update(this.user);
//		System.out.println("saida");
//	}
	
//	public List<Credito> getlistCreditos(){
//		if(this.listCreditos == null) {
//			this.listCreditos = this.creditosDAO.listAllByUserName(sc.getIdToken().getPreferredUsername());
//		}
//		Float g = this.creditosDAO.getSaldo(sc.getIdToken().getPreferredUsername());
//		return this.listCreditos;
//	}

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
