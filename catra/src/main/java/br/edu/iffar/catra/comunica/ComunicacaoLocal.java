/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.iffar.catra.comunica;

import javax.persistence.RollbackException;

import br.edu.iffar.catra.dao.APISaldoDAO;
import br.edu.iffar.catra.dao.ComunicaDAO;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;

/**
 *
 * @author qwerty
 */
public class ComunicacaoLocal implements CatracaComunicacao{

	private ComunicaDAO comunicaDAO;

	public ComunicacaoLocal() {
		this.comunicaDAO = new ComunicaDAO();
		
	}
	
	public APISaldo findSaldoByUsername(String username) throws Exception {
		APISaldoDAO usuarioCDAO = new APISaldoDAO();
		
		APISaldo u = usuarioCDAO.findSaldoByUsername(username);
		
		if(u == null) {
			throw new Exception("Usuário não foi encontrado!");
		}
		
		return u ;
	}

//	public void sendCredito(APIAgendamento agendamento,APIAgendamentosDisponibilizados ageDisp) throws RollbackException, Exception {
//		try {
//			this.comunicaDAO.salvar( agendamento, ageDisp );
//		} catch (RollbackException e) {
//			e.printStackTrace();
//			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
//		}
//	}
	
	public void updateAgendamento(APIAgendamentosDisponibilizados apiAgeDisp)  throws FalhaNaComunicacao, RollbackException, Exception{
		try {
			this.comunicaDAO.salvar( apiAgeDisp );
		} catch (RollbackException e) {
			e.printStackTrace();
			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
		}catch (Exception e) {
			e.printStackTrace();
			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
		}
	}
	
	public void updateAgendamento(APIAgendamento apiAngedamento)  throws FalhaNaComunicacao, RollbackException, Exception{
		try {
			this.comunicaDAO.salvar( apiAngedamento );
		} catch (RollbackException e) {
			e.printStackTrace();
			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
		}catch (Exception e) {
			e.printStackTrace();
			throw new FalhaNaComunicacao("Não foi possivel salvar as informações de acesso do Usuário!");
		}
	}

	@Override
	public APISaldo findSaldoByTokenRU(String tokenRu) throws Exception {
		APISaldoDAO usuarioCDAO = new APISaldoDAO();
		try {
			return usuarioCDAO.findSaldoByTokenRU(tokenRu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//
//	@Override
//	public APIAgendamentosDisponibilizados sendAgendamentoDisponibilizado(APIAgendamentosDisponibilizados ageDispo) throws FalhaNaComunicacao, RollbackException, Exception {
//		APIAgendamentosDisponibilizadosDAO ageDispDAO = new APIAgendamentosDisponibilizadosDAO();
//		APIAgendamentosDisponibilizados ad = ageDispDAO.salvar(ageDispo);
//		Log.info("Salvo local:" + ad.geraLogId());
//		return ad;
//	}
}
