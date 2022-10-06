/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.iffar.catra.control;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.edu.iffar.catra.comunica.CatracaComunicacao;
import br.edu.iffar.catra.comunica.ComunicacaoLocal;
import br.edu.iffar.catra.comunica.ComunicacaoREST;
import br.edu.iffar.catra.comunica.FalhaPassagemBloqueada;
import br.edu.iffar.catra.comunica.FalhaPassagemPermitida;
import br.edu.iffar.catra.comunica.KeycloakSession;
import br.edu.iffar.catra.dao.APIAgendamentosDisponibilizadosDAO;
import br.edu.iffar.catra.dao.APICreditoDAO;
import br.edu.iffar.catra.dao.APISincronizadoDAO;
import br.edu.iffar.catra.dao.APIUsuarioRefeicaoDAO;
import br.edu.iffar.catra.dao.AgendamentoAPIDAO;
import br.edu.iffar.catra.forms.FormCatra;
import br.edu.iffar.catra.forms.FormSemAgendamento;
import br.edu.iffar.catra.util.Log;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APICredito;
import br.edu.iffar.fw.classBag.db.model.api.APIImagen;
import br.edu.iffar.fw.classBag.db.model.api.APIRefeicao2;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;

/**
 *
 * @author qwerty
 */
public class PrincipalControl {

    private static PrincipalControl singleton = new PrincipalControl();
    public static final String ROLE_CATRACA = "IFFAR_RU_CATRACA";

    private CatracaComunicacao cc = new ComunicacaoLocal();
    private FormCatra fCatra;
    private TipoRefeicao tipoRefeicao;

    private AgendamentoAPIDAO agendamentoAPIDAO = new AgendamentoAPIDAO();
    private APIUsuarioRefeicaoDAO apiRefeicaoDAO = new APIUsuarioRefeicaoDAO();
    private APICreditoDAO apiCreditoDAO = new APICreditoDAO();

	private APISincronizadoDAO apiSincronizadoDAO = new APISincronizadoDAO();

    private APISaldo saldo;
    private APIAgendamento agendamento;
    private APIAgendamentosDisponibilizados ageDispo;

    public void setTipoRefeicao(TipoRefeicao tf) {
        this.tipoRefeicao = tf;
    }

    public TipoRefeicao getTipoRefeicao() {
        return this.tipoRefeicao;
    }

    public boolean validar() {
    	if (this.saldo == null) {
            this.fCatra.showMessageError("Usuário não foi encontrado.");
            return true;
        }
		if (!this.saldo.getUsuario().isAtivo()) {
			this.fCatra.showMessageError("Usuário está inativo.");
			return true;
		}
    	
        APICredito cred = this.apiCreditoDAO.existeSaidaCreditoParaHoje(this.saldo.getUsuario(), tipoRefeicao);
        if (cred != null) {
            this.fCatra.showMessageError("Usuario já acessou o RU no dia de hoje (" + cred.getDataString() + ").");
            return true;
        }
        return false;
    }
    
    public void creditar() {
    	if(this.validar()) {
    		this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);
    		return;
    	}
    	
    	if (agendamento.getRefeicao().getValor() > saldo.getSaldo()) {
            this.fCatra.showMessageError("Seu saldo é infuciente para esta refeicao!");
            this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);
            return;
        }
    	
    	agendamento.geraCreditoSaida();
    	 
        try {
        	if(this.ageDispo !=null) {
        		cc.updateAgendamento(this.ageDispo);
        	}else {
        		cc.updateAgendamento(this.agendamento);
        	}
            
            String msg = "Foi retirado $" + agendamento.getCredito().getValor() + " de " + agendamento.getUsuario().getNome() + ".";
            Log.info(msg);
            this.fCatra.showMessageSucess(msg);
            this.fCatra.mostraIcone(APIImagen.CHECK_PNG);
        } catch (FalhaPassagemBloqueada e) {
            e.printStackTrace();
            this.fCatra.showMessageError(e.getMessage());
            this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);            
        } catch (FalhaPassagemPermitida e) {
        	String msg = "Foi retirado $" + agendamento.getCredito().getValor() + " de " + agendamento.getUsuario().getNome() + ".";
            Log.info(msg);
            this.fCatra.showMessageSucess(msg);
            this.fCatra.showMessageWarning(e.getMessage());
            this.fCatra.mostraIcone(APIImagen.CHECK_PNG);
		} catch (Exception e) {
            e.printStackTrace();
            this.fCatra.showMessageError(e.getMessage());
            this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);
        }
        this.fCatra.setVisibleBtnAcessos(false);
    }

	private boolean bloqueiaPassagemPorHorario() {
		LocalDateTime instante = LocalDateTime.now();

		instante = ((instante.getDayOfWeek().equals(DayOfWeek.SATURDAY) || instante.getDayOfWeek().equals(DayOfWeek.MONDAY)) ?
				// final off week
				instante.with(this.agendamento.getRefeicao().getHoraInicioFind())
				:
				// day off week
				instante.with(this.agendamento.getRefeicao().getHoraInicioUtil())
				);
		
		LocalDateTime limite = instante.plusHours(2);
		LocalDateTime agora = LocalDateTime.now();
		if(agora.isAfter(instante) && agora.isBefore(limite)) {
			if(!this.apiSincronizadoDAO.getUltimoSinc().getDtSincronizado().isAfter(instante)) {
				new Thread(new SincronizadorControl()).start();
				//faz a sincronização pq aind nao foi feita depois do bloqueio de edições online
				return false;
			}
			return true;
		}

		FormCatra.getInstance().showMessageError("Bloqueio por horário. A refeição do aluno começa as "
				+ instante.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));

		if(KeycloakSession.MODO_OPERACAO.equals(KeycloakSession.MODO_OPERACAO_TESTE)) {
			return true;
		}
		return false;
	}

    public void searchUser(String ident, boolean useUsername) {
		if (ident.trim().isBlank()) {
			this.clearUserInfo();
			return;
		}
    	try {
            this.saldo = (useUsername) ? this.cc.findSaldoByUsername(ident) : this.cc.findSaldoByTokenRU(ident);
        } catch (Exception e) {
			Log.printError(e.getMessage());
            this.saldo = null;
            this.fCatra.showMessageError(e.getMessage());
            this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);
            return;
        }
    	
    	if(this.validar()) {
    		this.fCatra.mostraIcone(APIImagen.CANCEL_PNG);
    		return;
    	}
        
    	this.agendamento = this.agendamentoAPIDAO.findByUserAndDateAndTipoRefeicao(saldo.getUsuario(), LocalDate.now(), this.tipoRefeicao);

        if (this.agendamento == null) {
            this.fCatra.setVisibleBtnAcessos(false);
            this.fCatra.showMessageWarning("Usuário não possui agendamento de " + this.tipoRefeicao.getDescricao() + ", para hoje.");
            APIRefeicao2[] r = this.apiRefeicaoDAO.listRefeicaoByUsuarioETipoRefeicao(saldo.getUsuario(), this.tipoRefeicao);
            
            if (r == null || r.length == 0) {
                this.fCatra.showMessageError("Não existem opções de refeções disponíveis para o usuário!");
                return;
            }
            
			List<APIAgendamentosDisponibilizados> listAgeDisp = new APIAgendamentosDisponibilizadosDAO().listAllAgeDisponibilizadoByData(LocalDate.now(),
					PrincipalControl.getPrincipalControl().getTipoRefeicao());

            FormSemAgendamento fsa = new FormSemAgendamento(this.saldo.getUsuario(),r,listAgeDisp);
            fsa.setVisible(true);
        } else {
			if(this.bloqueiaPassagemPorHorario()) {
				this.fCatra.setVisibleBtnAcessos(true);
			}
        }

        this.fCatra.showUserInfo(saldo, agendamento);
    }
    
    public void setDadosAgendamento(APIAgendamentosDisponibilizados ageDispo,APIAgendamento agendamento){
    	this.ageDispo = ageDispo;
    	if(ageDispo == null) {
    		this.ageDispo = null;
    		this.agendamento = agendamento;
    	}else {
    		this.ageDispo = ageDispo;
    		this.agendamento = this.ageDispo.getAgendamento();
    		this.ageDispo.setUsuarioPedou(this.saldo.getUsuario());
    	}
        
        this.agendamento.setUsuario(this.saldo.getUsuario());
        
        this.fCatra.showUserInfo(saldo, this.agendamento);
        
		if(this.bloqueiaPassagemPorHorario()) {
			this.fCatra.setVisibleBtnAcessos(true);
		}
    }

    public void clearUserInfo() {
        this.saldo = null;
        this.agendamento = null;
        this.ageDispo = null;
        this.fCatra.clearUserInfo();
    }

    public static PrincipalControl getPrincipalControl() {
        return singleton;
    }

    public void setFormCatra(FormCatra f) {
        this.fCatra = f;
    }

    public void usarApiOnline(String user) {
        this.cc = new ComunicacaoREST();
        this.fCatra.catracaOnline(user);
    }
    
	public void usarApiOffLine(String user) {
        this.cc = new ComunicacaoLocal();
		this.fCatra.catracaOffline(user);
    }
    
    public void logof() {
//    	this.usarApiOffLine();
    }
}
