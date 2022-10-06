package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.constraints.NotNull;

import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.Saldo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@Named
@ViewScoped
public class TransferenciasBean implements Serializable,BreadCrumbControl{
	
	public class TransferenciaSubControl {
		
		public Credito saida;
		public Credito entrada;
		
		public TransferenciaSubControl() {
			this.saida = new Credito(TypeCredito.TRANS_SAIDA.createIntance());
			this.entrada = new  Credito(TypeCredito.TRANS_ENTRADA.createIntance());
			this.saida.setParent(this.entrada);
//			this.entrada.setParent(this.saida);//é feito no salvamento
		}
						
		public void setValor(Float f) {
			this.saida.setValor(f);
			this.entrada.setValor(f);
		}
		
		public Float getValor() {
			return this.saida.getValor();
		}
		
		public void setUsuarios(Usuario para,Usuario de) {
			this.saida.setUsuario(de);
			this.entrada.setUsuario(para);
		}
		public Usuario getPara() {
			return this.entrada.getUsuario();
		}
	}

	private static final long serialVersionUID = 22021991L;
		
//	@Inject private UsuarioInfoBean usuarioInfo;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private MessagesUtil messages; 
	@Inject private CreditosDAO creditosDAO;
	@Inject private UserTransaction userTransaction;
	
	private BreadCrumb breadCrumb;
	private List<Usuario> listUsers;
	private TransferenciaSubControl transSub;
//	private Float saldoUser; 
	private Usuario user;
	private Saldo saldo;


	@PostConstruct
	private void init() {
		this.createBreadCrumb();
		this.breadCrumb.setAtivo(1);
		this.cancelar();
	}
	
	public boolean validadete() {
		boolean retu = true;
		if(this.transSub.getValor() <= 0f) {
			messages.addError("Informe um valor para a transferência maior que 0.");
			retu = false;
		}
		if(this.saldo.getSaldo() < getValor()) {
			messages.addError("Saldo insuficiente.");
			retu = false;
		}
		if(this.transSub.getPara() == null) {
			messages.addError("Informe para quem você vai realizar a transferência.");
			retu = false;
		}
		if(this.user.equals(this.transSub.getPara())) {
			messages.addError("Você não pode transferir valores para você mesmo.");
			retu = false;
		}
		
		return retu;
	}
	
	public void transferir() {
		if(this.validadete()) {
			try {
				this.userTransaction.begin();

				creditosDAO.persist(this.transSub.entrada);

				this.transSub.saida.setParent(this.transSub.entrada);
				creditosDAO.persist(this.transSub.saida);

				this.transSub.entrada.setParent(this.transSub.saida);
				creditosDAO.update(this.transSub.entrada);

				this.userTransaction.commit();

				messages.addSuccess("Transfêrncia realizada com sucesso.");
				this.cancelar();
			} catch (Exception e) {
				e.printStackTrace();
				messages.addError("Não consegui realizar a transferência, se o problema persistir contate o administrado do sistema.");
				try {
					this.userTransaction.rollback();
				}
				catch (IllegalStateException | SecurityException | SystemException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void cancelar() {
		this.user = this.usuariosDAO.getUsuarioLogado();
		this.saldo = this.usuariosDAO.findSaldo(user);
		this.transSub = new TransferenciaSubControl();		
	}

	public void setPara(Usuario para) {
		this.transSub.setUsuarios(para,this.user);
	}
	
	@NotNull(message = "Informe o usuário de Destino.")
	public Usuario getPara() {
		return this.transSub.getPara();
	}
	
	public void setValor(Float f) {
		this.transSub.setValor(f);
	}
	
	@NotNull(message = "Informe um valor.")
	public Float getValor() {
		return this.transSub.getValor();
	}
	
	public List<Usuario> getListUsers(String nome){
		if(nome.length()<4) {
			return this.listUsers;
		}
		this.listUsers = this.usuariosDAO.listAllUsersBySubNomeCpf(nome);
		return this.listUsers;
	}

	public TransferenciaSubControl getTransSub() {
		return transSub;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public Saldo getSaldoUser() {
		return this.saldo;
	}

	@Override
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
				.inicializa()
				.addItem("Transferências", BreadCrumb.RAIZ)//1
				;
		}

	@Override
	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}
	
	
}
