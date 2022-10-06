package br.edu.iffar.catra.comunica;

import javax.persistence.RollbackException;

import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;

public interface CatracaComunicacao {
	
	public static final String FALHA_SALVAR_ONLINE = "FALHA SALVAR ONLINE.";
	public static final String FALHA_SALVAR_LISTA_ONLINE = "FALHA SALVAR LISTA ONLINE.";
	public static final String SUCESSO_SALVAR_ONLINE = "SUCESSO SALVAR ONLINE.";
	public static final String SUCESSO_SALVAR_LISTA_ONLINE = "FALHA SALVAR LISTA ONLINE.";
	
	public static final String FALHA_SALVAR_LOCAL = "FALHA SALVAR LOCAL.";
	public static final String SUCESSO_SALVAR_LOCAL = "SUCESSO SALVAR LOCAL.";
	

	public APISaldo findSaldoByUsername(String username) throws Exception ;
	
	public APISaldo findSaldoByTokenRU(String token) throws Exception;
		
//	public APICredito sendCredito(APICredito credito) throws FalhaNaComunicacao, RollbackException, Exception ;
	
//	public void sendCredito(APIAgendamento agendamento,APIAgendamentosDisponibilizados ageDispo) throws FalhaNaComunicacao, RollbackException, Exception ;
	
	public void updateAgendamento(APIAgendamentosDisponibilizados apiAgeDisp)  throws FalhaNaComunicacao, RollbackException, Exception;
	
	public void updateAgendamento(APIAgendamento apiAngedamento)  throws FalhaNaComunicacao, RollbackException, Exception;
}

