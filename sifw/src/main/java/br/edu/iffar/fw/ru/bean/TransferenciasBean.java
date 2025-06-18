package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.edu.iffar.fw.classBag.bo.CreditosDepositoBO;
import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.Saldo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Transferencia;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.validation.constraints.NotNull;

@Named
@ViewScoped
public class TransferenciasBean implements Serializable,BreadCrumbControl{
	

	private static final long serialVersionUID = 22021991L;

	@Inject private SessionDataStore  sessionDataStore;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private MessagesUtil messages; 
	@Inject private CreditosDAO creditosDAO;
	@Inject private CreditosDepositoBO  creditosDepositoBO;
	
	private BreadCrumb breadCrumb;
	private List<Usuario> listUsers;

	@NotNull(message = "Informe o usuário de Destino.")
	private Usuario paraUseuario;

	private Saldo saldo;

	@NotNull(message = "Informe um valor.")
	private BigDecimal valor;


	@PostConstruct
	private void init() {
		this.createBreadCrumb();
		this.breadCrumb.setAtivo(1);
		this.cancelar();
	}

	public void transferir() {
		try {
			Transferencia trans = new Transferencia()
					.valor(this.valor)
					.saldo(this.saldo.getSaldo())
					.para(this.paraUseuario)
					.realizadoPor(this.sessionDataStore.getUsuarioLogado())
					.builder();

			this.creditosDepositoBO.saveOperacaoCredito(trans);

			messages.addSuccess("Transfêrncia realizada com sucesso.");
			this.cancelar();
		} catch (Exception e) {
			e.printStackTrace();
			messages.addError("Não consegui realizar a transferência, se o problema persistir contate o administrado do sistema.");
		}
	}
	
	public void cancelar() {
		this.paraUseuario = null;
		this.saldo = this.creditosDAO.findSaldo(this.sessionDataStore.getUsuarioLogado());
	}

	public Usuario getParaUseuario() {
		return this.paraUseuario;
	}

	public void setParaUseuario(Usuario paraUseuario) {
		this.paraUseuario = paraUseuario;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValor() {
		return this.valor;
	}
	
	public List<Usuario> getListUsers(String nome){
		if(nome.length()<4) {
			return this.listUsers;
		}
		this.listUsers = this.usuariosDAO.listAllUsersBySubNomeCpf(nome);
		return this.listUsers;
	}

	public BigDecimal getSaldo() {
		return this.saldo.getSaldo();
	}

	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
				.inicializa()
				.addItem("Transferências", BreadCrumb.RAIZ)//1
				;
		}

	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}
	
	
}
