package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.edu.iffar.fw.classBag.bo.CreditosDepositoBO;
import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.dao.AltenacoesCreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.AlteracoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Deposito;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Retirada;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.ru.bean.fragment.SaldoUserFrament;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.NoResultException;
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
	@Inject private SessionDataStore sessionDataStore;
	@Inject private SaldoUserFrament saldoUserF;
	@Inject private MessagesUtil mesUtil;
	@Inject private CreditosDepositoBO creditosBO;
	
	private boolean rendBuscaCpf = true;
	
	private BigDecimal valorCredito;

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
	}

	public void telaCreditoCpf(){
		this.telaCredito();
		this.breadCrumb.setAtivo(5);
	}
	
	public void addCreditoDinheiro() {
		try {
			this.creditosBO.saveOperacaoCredito(
                     new Deposito()
                            .valor(this.valorCredito)
                            .para(this.userSelect)
                            .realizadoPor(this.sessionDataStore.getUsuarioLogado())
            );
			this.selecionaUser();
		} catch (CreditoException e) {
            throw new RuntimeException(e);
        } catch (NoResultException e) {
            throw new RuntimeException(e);
        }
    }
	
	public void retiradaTotal(boolean total) {

		try {
			this.creditosBO.saveOperacaoCredito(
                    (OperacoesCredito) new Retirada(total)
                            .saldo(this.saldoUserF.getSaldo().getSaldo())
                            .valor(this.valorCredito)
                            .para(this.userSelect)
                            .realizadoPor(this.sessionDataStore.getUsuarioLogado())
							.builder()
            );
			this.selecionaUser();
		}catch (CreditoException e) {
			mesUtil.addError(e.getMessage());
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

	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}
}
