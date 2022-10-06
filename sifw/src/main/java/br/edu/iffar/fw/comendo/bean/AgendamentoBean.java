package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.RollbackException;

import org.primefaces.event.SelectEvent;

import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.ParametrosDAO;
import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.Parametros;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeParam;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.comendo.bean.fragment.VinculoSelecionadoBean;
import br.edu.iffar.fw.comendo.primefaces.AgendamentosLazyLoad;

@Named
@ViewScoped
public class AgendamentoBean  implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	@Inject private HeaderBean headerBean;
	@Inject private AgendamentosDAO agendamentosDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	@Inject private ParametrosDAO parametrosDAO;
	@Inject private MessagesUtil messages;
	@Inject private AgendamentosLazyLoad ageLazyLoad;
	@Inject private VinculoSelecionadoBean vinculoSelecionadoBean;
		
	private Agendamento agendamento = null;
	private Parametros parametro;
	
	private List<Refeicao> listRefeicao;
	
	private Usuario user = null;
			
	@PostConstruct
	private void init() {
		this.parametro = this.parametrosDAO.findParametroByTypeParam(TypeParam.AGENDAMENTO_FUTURO_DIAS);
		
		this.user = this.headerBean.getUsuarioLogado();
		
		if(this.vinculoSelecionadoBean.isNecessarioSelecionarVinculo(this.user)) {
			this.vinculoSelecionadoBean.redirect();	
		}else {
	    	this.listRefeicao = this.refeicaoDAO.listRefeicaoByVinculo(this.vinculoSelecionadoBean.getVinculoSelecionado(),null);
		}
	}
	
    public Usuario getUser() {
		return user;
	}

	public void onEventSelect(SelectEvent<Agendamento> selectEvent) {
    	agendamento = selectEvent.getObject();
    	FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('eventDialog').show();");
    }
     
    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
    	LocalDate today = Agendamento.now().toLocalDate();

//		if(selectEvent.getObject().toLocalDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
//				selectEvent.getObject().toLocalDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
//			this.messages.addError("Não é possível realizar agendamentos para sábado e domingo!");
//			return;
//		}

    	if(today.compareTo(selectEvent.getObject().toLocalDate())>0) {
    		this.messages.addError("Data passada não pode ser editada.");
    		return;
    	}
		agendamento = new Agendamento(selectEvent.getObject().toLocalDate(), this.vinculoSelecionadoBean.getVinculoSelecionado());
    	FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('eventDialog').show();");
    }
            
    public void addAgendamento() {
		LocalDate limiteAgendamento;
		try {
			limiteAgendamento = LocalDate.now().plusDays((long) this.parametro.convertTo());
		} catch (Exception e1) {
			e1.printStackTrace();
			limiteAgendamento = LocalDate.now().plusDays(15);
		}

		if (this.agendamento.getDtAgendamento().isAfter(limiteAgendamento)) {
			this.messages.addError("A data maxima para agendamento é " + limiteAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
			return;
		}

		LocalDateTime agora = Agendamento.now();

		LocalDateTime minIni = this.agendamento.diaHoraLimiteParaAlteracaoDtAgendamento();

		if (!this.agendamento.ehEditavelDtInicial && agora.isAfter(minIni)) {
			this.messages.addError(this.agendamento.mensagemAlteracaoDtAgendamento());
			return;
		}
		if (this.agendamentosDAO.existeSobreposicaoDeRefeicao(this.agendamento, this.vinculoSelecionadoBean.getVinculoSelecionado().getUsuario())) {
			this.messages.addError("Não é possivel sobrepor períodos/agendamentos de " + this.agendamento.getRefeicao().getTipoRefeicao().getDescricao() + ", verifique seus vínculos.");
			return;
		}

		try {
			this.agendamentosDAO.updateT(this.agendamento);
			this.ageLazyLoad.updateEvent(this.agendamento);
			this.agendamento = new Agendamento(true);
			this.messages.addSuccess("Agendamento salvo com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel salvar o agendamento.");
		}
    }
    
    public void removeAgendamento() {
    	try {
    		if(this.agendamento.isPermitdoRemover()) {
    			this.ageLazyLoad.deleteEvent(this.agendamento);
            	this.agendamentosDAO.removeT(this.agendamento);
            	this.agendamento = new Agendamento(true);
            	this.messages.addSuccess("Agendamento removido com sucesso.");
    		}else {
    			this.messages.addError("Não é possivel removel este agendamento pois ele já foi usado.");
    		}
    		
		} catch (RollbackException e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel remover o agendamento.");
		}
    }
	
	public Agendamento getAgendamento() {
		return agendamento;
	}
	
	public List<Refeicao> getListRefeicao(){
		return this.listRefeicao;
	}

	public AgendamentosLazyLoad getAgeLazyLoad() {
		return ageLazyLoad;
	}

	public void setAgeLazyLoad(AgendamentosLazyLoad ageLazyLoad) {
		this.ageLazyLoad = ageLazyLoad;
	}
}
