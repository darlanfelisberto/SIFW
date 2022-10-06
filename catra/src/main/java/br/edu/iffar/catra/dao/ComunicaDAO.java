package br.edu.iffar.catra.dao;

import javax.persistence.RollbackException;

import br.edu.iffar.catra.comunica.CatracaComunicacao;
import br.edu.iffar.catra.util.Log;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;

public class ComunicaDAO extends APIDAO<Object> {

//	public void salvar(APIAgendamento agendamento,APIAgendamentosDisponibilizados ageDisp ) throws RollbackException,Exception{
//		this.begin();
//		this.em.merge(agendamento);
//		Log.info(CatracaComunicacao.SUCESSO_SALVAR_LOCAL, agendamento);
////		Log.info("Salvo local", agendamento.getCredito());
//		
//		//caso nao tenha agendamentos disponivei,este objeto vem null;
//		if(ageDisp != null) {
//			this.em.merge(ageDisp);
//			Log.info("Salvo local",ageDisp);
//        }		
//        this.commit();	
//    }
	
	
	public void salvar(APIAgendamentosDisponibilizados apiAgeDisp ) throws RollbackException,Exception{
		this.begin();
		this.em.merge(apiAgeDisp);
        this.commit();	
		Log.info(CatracaComunicacao.SUCESSO_SALVAR_LOCAL, apiAgeDisp);
//		Log.info("Salvo local: ", apiAgeDisp.getAgendamento());
//		Log.info("Salvo local: ", apiAgeDisp.getAgendamento().getCredito());
    }
	
	public void salvar(APIAgendamento apiAgendamento ) throws RollbackException,Exception{
		this.begin();
		this.em.merge(apiAgendamento);
        this.commit();
    	Log.info(CatracaComunicacao.SUCESSO_SALVAR_LOCAL,apiAgendamento);
//		Log.info("Salvo local", apiAgendamento.getCredito());
    }

}
