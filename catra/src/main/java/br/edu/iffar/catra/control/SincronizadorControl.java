package br.edu.iffar.catra.control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.iffar.catra.comunica.ComunicacaoREST;
import br.edu.iffar.catra.comunica.KeycloakSession;
import br.edu.iffar.catra.dao.APIAgendamentosDisponibilizadosDAO;
import br.edu.iffar.catra.dao.APIImagemDAO;
import br.edu.iffar.catra.dao.APISaldoDAO;
import br.edu.iffar.catra.dao.APISincronizadoDAO;
import br.edu.iffar.catra.dao.APITipoRefeicaoDAO;
import br.edu.iffar.catra.dao.APIUsuarioDAO;
import br.edu.iffar.catra.dao.APIUsuarioRefeicaoDAO;
import br.edu.iffar.catra.dao.AgendamentoAPIDAO;
import br.edu.iffar.catra.forms.FormCatra;
import br.edu.iffar.catra.util.Log;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APIImagen;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;
import br.edu.iffar.fw.classBag.db.model.api.APISincronizado;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuarioRefeicao;

public class SincronizadorControl implements Runnable{
	
	private static SincronizadorControl instance = new SincronizadorControl();
	
    private APISaldoDAO saldoDAO =  new APISaldoDAO();
	private APIUsuarioDAO apiUsuarioDAO = new APIUsuarioDAO();
    private APITipoRefeicaoDAO tipoRefeicaoCDAO = new APITipoRefeicaoDAO();
    private APIImagemDAO imagemCDAO = new APIImagemDAO();
    private AgendamentoAPIDAO agendamentoAPIDAO = new AgendamentoAPIDAO();
    private APIUsuarioRefeicaoDAO refeicaoDAO = new APIUsuarioRefeicaoDAO();
    private APISincronizadoDAO apiSincronizadoDAO =  new APISincronizadoDAO();
    private APIAgendamentosDisponibilizadosDAO ageDispDAO = new APIAgendamentosDisponibilizadosDAO();
    
    
    public static SincronizadorControl getInstance(){
    	return instance;
    }
    
    @Override
	public void run() {
		if(!KeycloakSession.getCatracaInstance().isOnline()) {
			JOptionPane.showMessageDialog(null, "Você deve ficar On-Line antes da sincronização!");
			return;
		}

		this.syncronizar();
	}
	
	private void syncronizar() {
		FormCatra.getInstance().clearMessage();
		FormCatra.getInstance().clearUserInfo();

		FormCatra.getInstance().showMessageWarning("Sincronização iniciada! Aguarde...");
		
		ComunicacaoREST rest = new ComunicacaoREST();
		
		try {
			List<APIAgendamentosDisponibilizados> listNotSyncAgeDisp = this.ageDispDAO.listNotSyncAgeDisp();
			if(listNotSyncAgeDisp != null && !listNotSyncAgeDisp.isEmpty()) {
				rest.syncAgeDisp(listNotSyncAgeDisp);
			}
		}catch (Exception q) {
			q.printStackTrace();
		}
		
		//sinrconisando os credios
		try {
			List<APIAgendamento> listNotSync = this.agendamentoAPIDAO.listAllAgendamentoNotSync();
			if(listNotSync != null && !listNotSync.isEmpty()) {
				rest.sendListCredito(listNotSync);
			}
		}catch (Exception q) {
			q.printStackTrace();
		}
			
		
		try {//saldo e usuario
			List<APISaldo> saldos = rest.listALLSaldos();
			
			saldoDAO.begin();
			saldoDAO.joinTransaction(apiUsuarioDAO);
			int retorno = this.apiUsuarioDAO.disableAllApiUsuario();
			for (APISaldo saldo : saldos) {
				saldoDAO.salvarT(saldo);
			}
			saldoDAO.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			saldoDAO.rollback();
		}

		
		try {
			List<APIImagen> imagens = rest.listAllImages();
			
			for (APIImagen i : imagens) {
				this.imagemCDAO.update(i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.imagemCDAO.rollback();
		}

		try {
			List<TipoRefeicao> tf = rest.listAllTipoRefeicao();
			tipoRefeicaoCDAO.begin();
			for (TipoRefeicao tipoRefeicao : tf) {
				this.tipoRefeicaoCDAO.salvarT(tipoRefeicao);
			}
			tipoRefeicaoCDAO.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tipoRefeicaoCDAO.rollback();
		}


		try {
			List<APIUsuarioRefeicao> disable = this.refeicaoDAO.listAllRefeicaoUsuarioAtivas();
			List<APIUsuarioRefeicao> li = rest.listAllAPIRefeicoes();
			this.refeicaoDAO.begin();
			for (APIUsuarioRefeicao age : li) {
				this.refeicaoDAO.salvarT(age);
				disable.remove(age);
			}
			this.refeicaoDAO.commit();
			
			this.refeicaoDAO.begin();
			for (APIUsuarioRefeicao age : disable) {
				age.setAtiva(false);
				this.refeicaoDAO.salvarT(age);
			}
			this.refeicaoDAO.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			this.refeicaoDAO.rollback();
		}
		
		try {
			List<APIAgendamento> li = rest.listAgendamentoByDateStart(LocalDate.now());
			this.agendamentoAPIDAO.begin();

			for (APIAgendamento age : li) {
				this.agendamentoAPIDAO.salvarT(age);
			}

			this.agendamentoAPIDAO.joinTransaction(this.ageDispDAO);

//			List<APIAgendamentosDisponibilizados> l = this.agendamentoAPIDAO.listAllAgendamentoDisponibilizadoToDelete(li, LocalDate.now());
//			l.forEach(age -> {
//				this.ageDispDAO.removeT(age);
//			});

			List<APIAgendamento> agendamentoRemover = this.agendamentoAPIDAO.listAllAgendamentoToDelete(li, LocalDate.now());

			agendamentoRemover.forEach(age -> {
				this.agendamentoAPIDAO.removeT(age);
			});

			this.agendamentoAPIDAO.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.agendamentoAPIDAO.rollback();
		}
		
		this.ageDispDAO = new APIAgendamentosDisponibilizadosDAO();
		try {
			List<APIAgendamentosDisponibilizados> li = rest.listAgeDisponibilizadosByDateStart(LocalDate.now());
			this.ageDispDAO.begin();
			for (APIAgendamentosDisponibilizados age : li) {
				this.ageDispDAO.salvarT(age);
			}
			this.ageDispDAO.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.ageDispDAO.rollback();
		}
				
		try {
			APISincronizado a = new APISincronizado(LocalDateTime.now(), APISincronizado.TipoSinc.TOTAL);
			this.apiSincronizadoDAO.salvar(a);
			FormCatra.getInstance().setLabelSincronizado(a.getLabel());
			Log.info("Fim Syncronização");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		FormCatra.getInstance().showMessageWarning("Sincronização finalizada.");
	}
}
