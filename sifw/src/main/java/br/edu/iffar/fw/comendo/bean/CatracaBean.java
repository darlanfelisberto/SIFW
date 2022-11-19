package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;

import org.primefaces.PrimeFaces;

import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.LogDAO;
import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoRefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.dao.VinculosAtivosUsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.Saldo;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import br.edu.iffar.fw.classBag.db.model.log.LogLeitura;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@Named
@ViewScoped
public class CatracaBean implements Serializable{
	
	private static final String MSG_SEM_REFEICOES = "Vínculo do usuário não pertence a nenhum grupo que tenha acesso a este tipo de refeição!";
	private static final String ERRO_ADD_CREDITO = "Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.";

	private static final long serialVersionUID = 22021991L;	
	
	@Inject private TipoRefeicaoDAO tipoRefeicaoDAO;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private MessagesUtil messagesUtil;
	@Inject private ImagenDAO imagenDAO;
	@Inject private AgendamentosDAO agendamentosDAO;
	@Inject private CreditosDAO creditosDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	@Inject private VinculosAtivosUsuariosDAO vinculosAtivosUsuariosDAO;
	@Inject private LogDAO logDAO;
	
	private List<TipoRefeicao> listTipoRefeicao;
	
	private boolean rendOtpBusca = true;
	private String token = "";

	private TipoRefeicao tipoRefeicao;
	
	
	private List<Refeicao> listRefeicaoDoUsuario;
	private List<VinculosAtivosUsuarios> listVinculosAtivosUsuario;
	private VinculosAtivosUsuarios vinculoSelecionado = null;
	
	private Usuario userLogado;
	
	private Usuario usuario;
	private Agendamento agendamento;
	private Imagen imagen;
	private Saldo saldo;
	private String boaRefeicao = null;
	
	private Credito credito;
	
	private boolean tokenRu;
	
	private LocalDate dtReferencia = LocalDate.now();
	
	@PostConstruct
	private void init() {
		this.userLogado = this.usuariosDAO.getUsuarioLogado();
	}
	
	public void clearDados() {
		this.agendamento = null;
		this.usuario = null;
		this.imagen = null;
		this.saldo = null;
		this.credito = null;
		this.boaRefeicao = null;
		this.vinculoSelecionado = null;
		this.listVinculosAtivosUsuario = null;
		this.listRefeicaoDoUsuario = null;
	}
	
	public void btnCancelar() {
		this.clearDados();
		this.token = "";
	}

	public void buscarUsuario() {
		if(this.token == null || this.token.isBlank()) {
			this.messagesUtil.addError("ATENÇÃO!", "Nada foi lido ou digitado.");
			return;
		}

		String [] info = this.token.split(":");
		this.clearDados();
		if(info.length>1) {
			this.usuario = this.usuariosDAO.findUsuarioByToken(info[1]);
			this.tokenRu = true;
		}else {
			this.usuario = this.usuariosDAO.getUsuarioByUserName(token);
			this.tokenRu = false;
		}
		
		try {
			this.logDAO.log(new LogLeitura(usuario, userLogado, this.token));
		} catch (RollbackException e) {
			System.out.println(e.getMessage());
		}
				
		if(this.usuario == null) {
			this.msgErroSom("Usuário não encontrado ou não tem acesso.");
			return;
		}
		
		this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.usuario);
				
		this.saldo =  this.usuariosDAO.findSaldo(this.usuario);
		this.agendamento = this.agendamentosDAO.findByUserAndDateAndTipoRefeicao(usuario, this.dtReferencia, this.tipoRefeicao);
		
		this.credito = (this.agendamento != null && this.agendamento.getCredito() != null ? this.agendamento.getCredito() : null);// this.creditosDAO.existeSaidaCreditoParaHoje(this.usuario,this.tipoRefeicao);

		if(this.credito != null) {
			this.msgErroSom("Usuário já entrou no RU na data de referẽncia " + this.agendamento.getDtAgendamentoString()+", crédito realizado " + this.credito.getDataString() + ".");
			return;
		}
		
		if(this.agendamento == null) {
			this.msgErroSom("Usuário sem agendamento!");

			this.listVinculosAtivosUsuario = this.vinculosAtivosUsuariosDAO.listVinculosAtivos(this.usuario);
			
			if(this.listVinculosAtivosUsuario != null) {
				this.vinculoSelecionado = this.listVinculosAtivosUsuario.get(0);
				this.listRefeicaoDoUsuario = this.refeicaoDAO.listRefeicaoByVinculo(this.vinculoSelecionado,this.tipoRefeicao);
				
				if(this.listRefeicaoDoUsuario.isEmpty()) {
					this.messagesUtil.addError(MSG_SEM_REFEICOES, "");
				}
			}else {
				this.msgErroSom("Usuário não possui vínculo ativo.");
				return;
			}
			
			this.agendamento = new Agendamento(this.dtReferencia, this.vinculoSelecionado);
			this.agendamento.setAgendado(false);
			return;
		}

		if(this.tokenRu) {
			this.creditar();
		}
	}
	
	public void msgErroSom(String msg) {
		this.messagesUtil.addError(msg, "");
		PrimeFaces.current().executeScript("tocaSomErro()");
	}

	public void creditar() {
		if(saldo.getSaldo()>=this.agendamento.getRefeicao().getValor()) {
			//aqui nao pode usar a dtReferencia, pq é o horario que ocorrei o credito e dia, que podem ser feitos em outro momento
			LocalDateTime data = LocalDateTime.now();
			this.credito = new Credito(data, this.agendamento, this.agendamento.getRefeicao().getValor(), this.usuario, new TipoCredito(TypeCredito.SAIDA), data);
			try {
				this.creditosDAO.updateT(this.credito);
					
				String boarefeicao = "Foi debitado " + this.credito.getValor() + " de <b>" + this.usuario.getNome() + "</b>. Boa Refeição!";
				this.boaRefeicao = boarefeicao;
			}catch (RollbackException e) {
				messagesUtil.addError(e);
				this.msgErroSom(ERRO_ADD_CREDITO);
			} 
			catch (Exception e) {
				e.printStackTrace();
				this.msgErroSom(ERRO_ADD_CREDITO);
			}
		}else {
			this.msgErroSom("Usuário não possui créditos suficiente!");
		}
	}
	
	public void selecionouVinculo() {
		this.listRefeicaoDoUsuario = this.refeicaoDAO.listRefeicaoByVinculo(this.vinculoSelecionado,this.tipoRefeicao);
		this.agendamento.mudaVinculo(this.vinculoSelecionado);
		if(this.listRefeicaoDoUsuario == null || this.listRefeicaoDoUsuario.isEmpty()) {
			this.messagesUtil.addError(MSG_SEM_REFEICOES, "");
		}
	}

	public List<TipoRefeicao> getListTipoRefeicao() {
		if(this.listTipoRefeicao == null) {
			this.listTipoRefeicao = this.tipoRefeicaoDAO.listAll();
		}
		
		return listTipoRefeicao;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}

	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}

	public boolean isRendOtpBusca() {
		return rendOtpBusca;
	}

	public void setRendOtpBusca(boolean rendOtpBusca) {
		this.rendOtpBusca = rendOtpBusca;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}

	public Saldo getSaldo() {
		return saldo;
	}

	public void setSaldo(Saldo saldo) {
		this.saldo = saldo;
	}

	public boolean isTokenRu() {
		return tokenRu;
	}

	public void setTokenRu(boolean tokenRu) {
		this.tokenRu = tokenRu;
	}
	
	public List<Refeicao> getRefeicoesDoUsuario(){
		return this.listRefeicaoDoUsuario;
	}
	
	public Credito getCredito() {
		return credito;
	}

	public String getBoaRefeicao() {
		return boaRefeicao;
	}
	
	public void atualizaBean() {
		//TODO remover sysout
		System.out.println("catracaBean.atualizabean");
	}

	public List<VinculosAtivosUsuarios> getListVinculosAtivosUsuario() {
		return listVinculosAtivosUsuario;
	}

	public VinculosAtivosUsuarios getVinculoSelecionado() {
		return vinculoSelecionado;
	}

	public void setVinculoSelecionado(VinculosAtivosUsuarios vinculoSelecionado) {
		this.vinculoSelecionado = vinculoSelecionado;
	}

	public LocalDate getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(LocalDate dtReferencia) {
		this.dtReferencia = dtReferencia;
	}
	
	public boolean isRendCreditar() {
		if(this.credito != null) {
			return false;
		}
		
		return (this.agendamento != null) || (this.usuario != null && this.listVinculosAtivosUsuario != null);
	}
}
