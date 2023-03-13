package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.AlteracoesAgendamentoDAO;
import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.AlteracoesAgendamentos;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.comendo.bean.fragment.VinculoSelecionadoBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.UserTransaction;

@Named
@ViewScoped
public class TransferirAgendamentosBean implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	//@formatter:off
	@Inject private MessagesUtil messages;
	@Inject private AgendamentosDAO agendamentosDAO;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	@Inject private AlteracoesAgendamentoDAO altAgendDAO; 
	@Inject private UserTransaction userTransaction;
	@Inject private VinculoSelecionadoBean vinculoSelecionadoBean;

	@Inject private SessionDataStore sessionDataStore;
	
	private BreadCrumb breadCrumb;
	
	private List<Agendamento> listAgendTransferiveis;
	private List<AlteracoesAgendamentos> listAlteracoesAgendamentos;
	private List<Refeicao> listRefeicoes;
	
	private Agendamento agendamento;
	private Usuario userLogado;
	private AlteracoesAgendamentos altAgendamento;

	@PostConstruct
	private void init() {
		if(this.vinculoSelecionadoBean.isNecessarioSelecionarVinculo()){
			this.vinculoSelecionadoBean.redirect();
			return;
		}
		this.userLogado = this.sessionDataStore.getUsuarioLogado();
		this.initListas();
	}
	
	public void initListas() {
		this.listAgendTransferiveis = this.agendamentosDAO.listAgendamentosTransferiveis(this.userLogado, LocalDate.now());
		this.listAlteracoesAgendamentos = this.altAgendDAO.listAllAlteracoesAgendamentos(this.userLogado);
		this.novaAlteracaoAgendamento();
	}
		
	public void novaAlteracaoAgendamento() {
		this.altAgendamento = new AlteracoesAgendamentos(this.userLogado);
	} 
	
	public void selecionouAgendamento() {
		this.listRefeicoes = this.refeicaoDAO.listRefeicaoByVinculo(this.vinculoSelecionadoBean.getVinculoSelecionado(), this.altAgendamento.getAgendamento().getRefeicao().getTipoRefeicao());
	}
	
	public void transferirAgendamento() {
		if(this.altAgendamento.getUsuarioOrigem().equals(this.altAgendamento.getUsuarioDestino())) {
			this.messages.addError("Você não pode transferir o agendamento para você mesmo.");
			return;
		}
		
		try {
			this.altAgendamento.setDtTranferencia(LocalDateTime.now());
			this.altAgendDAO.persistT(this.altAgendamento);
			this.messages.addSuccess("Tranferência do agendamento foi realizada com sucesso! Usuário de destino deve aceitar a transferência.");
			this.initListas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recusarTransferencia() {
		try {
			this.altAgendamento.setAceito(false);
			this.altAgendDAO.updateT(this.altAgendamento);
			this.messages.addSuccess("Transferência do agendamento foi RECUSADA com sucesso!");
			this.initListas();
		} catch (Exception e) {
			e.printStackTrace();
			this.messages.addError("Ocorreu um erro ao salvar a tranferência do agendamento.");
		}		
	}
	
	public void aceitarTransferencia() {
		if(this.agendamentosDAO.existeSobreposicaoDeRefeicao(this.altAgendamento.getAgendamento(), this.userLogado)) {
			this.messages.addError("Já existe uma refeição do mesmo tipo para o dia " + this.altAgendamento.getAgendamento().getDtAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			this.recusarTransferencia();
			return;
		}
		
		Agendamento a = this.agendamentosDAO.findById(this.altAgendamento.getAgendamento().getMMId());
		if(a.getCredito() != null) {
			this.messages.addError("Usuário de origem utilizou o agendamento.");
			this.recusarTransferencia();
			return;
		}
		
		if(!this.listRefeicoes.contains(this.altAgendamento.getAgendamento().getRefeicao())) {
			this.messages.addError("Selecione uma refeição disponível.");
			return;
		}
		
		try {
			this.userTransaction.begin();
				this.altAgendamento.setAceito(true);
//				this.altAgendamento.getAgendamento().setUsuario(this.altAgendamento.getUsuarioDestino());
				this.altAgendamento.getAgendamento().mudaVinculo(this.vinculoSelecionadoBean.getVinculoSelecionado());
				this.altAgendDAO.update(this.altAgendamento);
				this.agendamentosDAO.update(this.altAgendamento.getAgendamento());
				this.messages.addSuccess("Tranferência do agendamento foi ACEITA com sucesso!");
				this.novaAlteracaoAgendamento();
			this.userTransaction.commit();
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception ew) {
			}
			this.messages.addError("Ocorreu um erro ao salvar a tranferência do agendamento.");
			e.printStackTrace();
		}		
	}
	
	public void brnCancelarTransferencia() {
		this.altAgendamento = new AlteracoesAgendamentos(this.userLogado);
	}
	
	public void cancelarTransRealizada() {
		try {
			this.userTransaction.begin();
			AlteracoesAgendamentos aa = this.altAgendDAO.findById(this.altAgendamento);
			if(aa.getAceito() == null) {
				this.altAgendDAO.remove(aa);
				this.userTransaction.commit();
				this.messages.addSuccess("Tranferência de agendamento removida com sucesso");
				this.initListas();
			}else {
				this.messages.addError("Usuário de destino aceitou o agendamento!","Não será possivel desfazer a transferência.");
				this.userTransaction.rollback();
			}			
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception ew) {
			}
			this.messages.addError("Ocorreu um erro ao salvar a tranferência do agendamento.");
			e.printStackTrace();
		}
	}	
		
	public List<Usuario> buscarUserDestino(String busca){
		return this.usuariosDAO.listAllUsersBySubNomeCpf(busca);
	}

	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}

	public List<Agendamento> getListAgendTransferiveis() {
		return listAgendTransferiveis;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public List<AlteracoesAgendamentos> getListAlteracoesAgendamentos() {
		return listAlteracoesAgendamentos;
	}

	public void setListAlteracoesAgendamentos(List<AlteracoesAgendamentos> listAlteracoesAgendamentos) {
		this.listAlteracoesAgendamentos = listAlteracoesAgendamentos;
	}

	public AlteracoesAgendamentos getAltAgendamento() {
		return altAgendamento;
	}

	public void setAltAgendamento(AlteracoesAgendamentos altAgendamento) {
		this.altAgendamento = altAgendamento;
	}

	public Usuario getUserLogado() {
		return userLogado;
	}

	public void setUserLogado(Usuario userLogado) {
		this.userLogado = userLogado;
	}

	public List<Refeicao> getListRefeicoes() {
		return listRefeicoes;
	}

	public void setListRefeicoes(List<Refeicao> listRefeicoes) {
		this.listRefeicoes = listRefeicoes;
	}
	
	
}
