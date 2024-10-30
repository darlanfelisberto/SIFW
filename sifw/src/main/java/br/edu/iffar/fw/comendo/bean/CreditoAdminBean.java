package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.AltenacoesCreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.AltenacoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.comendo.bean.fragment.SaldoUserFrament;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;

@Named
@ViewScoped
public class CreditoAdminBean  implements Serializable,BreadCrumbControl{

	private static final long serialVersionUID = 22021991L;
	
	private BreadCrumb breadCrumb;
	
	private int tipoBusca = 1;
	private String buscaIsso;
	
	private List<Usuario> listUsuarios;
	private Usuario userSelect;
	
	private boolean rendTelaFiltro;
	private boolean rendTelaList;
	private boolean rendTelaCredito;
	
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private SaldoUserFrament saldoUserF;
	@Inject private AltenacoesCreditosDAO transfDAO;
	@Inject private MessagesUtil mesUtil;
	
	private boolean rendBuscaCpf = true;
	
	private Credito newCred; 

	@PostConstruct
	public void init() {
		this.createBreadCrumb();
		this.telaFiltro();
	}
	
	public void buscar() {
		if(this.tipoBusca ==1) {
			this.buscaIsso = this.buscaIsso.replaceAll("[^0-9]", "");
			this.userSelect = this.usuariosDAO.getUsuarioByCPF(this.buscaIsso);

			if(this.userSelect == null) {
				this.mesUtil.addError("Nenhum usuário encontrado.");
				return;
			}

			this.selecionaUser();
			this.telaCreditoCpf();
		}else{
			this.listUsuarios = usuariosDAO.listAllUsersByName(this.buscaIsso);
			this.telaLista();
		}
	}

	@Override
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Créditos", "creditos.xhtml", BreadCrumb.RAIZ)// 1
			.addItem("Busca de Usuário" ,"#{creditoAdminBean.telaFiltro()}","frmMain", 1)//2
			.addItem("Lista de Usuários","#{creditoAdminBean.telaLista()}" ,"frmMain", 2)//3
				.addItem("Add. Créditos", 3)// 4
				.addItem("Add. Créditos", 1)// 5
			;
	}
	
	public void selecionaUser() {
		this.saldoUserF.init(this.userSelect, "creditoAdminBean.saldoUserF");
		this.telaCredito();
	}
	
	public void telaFiltro() {
		this.rendTelaFiltro = true;
		this.rendTelaList = false;
		this.rendTelaCredito = false;
		this.breadCrumb.setAtivo(2);
		this.listUsuarios = null;
	}

	public void telaLista() {
		this.rendTelaFiltro = false;
		this.rendTelaList = true;
		this.rendTelaCredito = false;
		this.breadCrumb.setAtivo(3);
	}
	public void telaCredito() {
		this.rendTelaFiltro = false;
		this.rendTelaList = false;
		this.rendTelaCredito = true;
		this.breadCrumb.setAtivo(4);
		
		this.newCred = new Credito(new TipoCredito(TypeCredito.ENTRADA));//TypeCredito.ENTRADA_INTERNA
	}

	public void telaCreditoCpf(){
		this.telaCredito();
		this.breadCrumb.setAtivo(5);
	}
	
	public void addCreditoDinheiro() {
		AltenacoesCreditos altenacoesCreditos = new AltenacoesCreditos();
		altenacoesCreditos.setRealizadoPor(this.usuariosDAO.getUsuarioLogado());
		altenacoesCreditos.setCreditoPara(newCred);
		altenacoesCreditos.setPara(this.userSelect);
		
		try {
			this.transfDAO.persistT(altenacoesCreditos);
			this.selecionaUser();
			mesUtil.addSuccess("Os créditos foram adicionados.");
		}catch (RollbackException e) {
			mesUtil.addError(e);
			mesUtil.addError("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
		} 
		catch (Exception e) {
			mesUtil.addError("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
		}
	}
	
	public void retiradaTotal(boolean total) {

		if(!(this.saldoUserF.getSaldo().getSaldo() > 0)) {
			mesUtil.addError("Saldo deve ser maior que zero.");
			return;
		}
		if(!total  && (this.saldoUserF.getSaldo().getSaldo() < this.newCred.getValor().floatValue())) {
			mesUtil.addError("Saldo insufíciente para retirada.");
			return;
		}
		
		this.newCred.setTipoCredito(new TipoCredito(TypeCredito.RETIRADA));
		
		if(total) {
			this.newCred.setValor(this.saldoUserF.getSaldo().getSaldo());
		}
		
		AltenacoesCreditos altenacoesCreditos = new AltenacoesCreditos();
		altenacoesCreditos.setRealizadoPor(this.usuariosDAO.getUsuarioLogado());
		altenacoesCreditos.setCreditoPara(newCred);
		altenacoesCreditos.setPara(this.userSelect);
		
		try {
			this.transfDAO.persistT(altenacoesCreditos);
			this.selecionaUser();
			mesUtil.addSuccess("Os créditos foram retirados com sucesso.");
		}catch (RollbackException e) {	
			mesUtil.addError(e);
			mesUtil.addError("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
			e.printStackTrace();
		} 
		catch (Exception e) {
			mesUtil.addError("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
			e.printStackTrace();
		}
	}
	
	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	public int getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(int tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getBuscaIsso() {
		return buscaIsso;
	}

	public void setBuscaIsso(String buscaIsso) {
		this.buscaIsso = buscaIsso;
	}

	public boolean isRendBuscaCpf() {
		return rendBuscaCpf;
	}

	public void setRendBuscaCpf(boolean rendBuscaCpf) {
		this.rendBuscaCpf = rendBuscaCpf;
	}

	public List<Usuario> getListUsuario() {
		return listUsuarios;
	}

	public boolean isRendTelaFiltro() {
		return rendTelaFiltro;
	}

	public boolean isRendTelaList() {
		return rendTelaList;
	}

	public boolean isRendTelaCredito() {
		return rendTelaCredito;
	}

	public Usuario getUserSelect() {
		return userSelect;
	}

	public void setUserSelect(Usuario usuario) {
		this.userSelect = usuario;
	}

	public SaldoUserFrament getSaldoUserF() {
		return saldoUserF;
	}

	public Credito getNewCred() {
		return newCred;
	}	
}
