package br.edu.iffar.api;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.AgendamentosDisponilizadosDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.AgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;

@RequestScoped
public class UpdateMethods implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject private AgendamentosDisponilizadosDAO ageDispDAO;
	@Inject private AgendamentosDAO agendamentoDAO;
	

	public void updateAgendamento(APIAgendamentosDisponibilizados apiAgeDisp) throws InternalServerErrorException, NotFoundException{
		AgendamentosDisponibilizados age = null;
		age = ageDispDAO.findById(apiAgeDisp.getMMId());
		if(age == null) {
			throw new NotFoundException(AgendamentosDisponibilizados.class.getName() + " não foi encontrado para o id "+ apiAgeDisp.getMMId());
//			return Response.status(Response.Status.NOT_FOUND).entity(AgendamentosDisponibilizados.class.getName() + " não foi encontrado para o id "+ apiAgeDisp.getMMId()).build();
		}
		
		age.setUsuarioPedouId(apiAgeDisp.getUsuarioPegouId());
		age.setSincronizado(apiAgeDisp.getSincronizado());
		
		if(!age.getAgendamento().getMMId().equals(apiAgeDisp.getAgendamento().getMMId())) {
			throw new InternalServerErrorException("id do agendamento é diferente!");
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("id do agendamento é diferente!").build();
		}
		
//		if(age.getAgendamento().getUsuario() != null) {
//			throw new InternalServerErrorException("Agendamento já possui um usuario associado!");
////			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Agendamento já possui um usuario associado!").build();
//		}
		
		if(age.getAgendamento().getUsuario() != null) {
			throw new InternalServerErrorException("O agendamento já possui um usuario cadastrado, integridade dos dados comprometida");
		}
		if(!age.getAgendamento().getMMId().equals(apiAgeDisp.getAgendamento().getMMId())) {
			throw new InternalServerErrorException("O agendamento id é diferente, integridade dos dados comprometida");	
		}
		if(!age.getUsuarioDisponibilizou().getMMId().equals(apiAgeDisp.getUsuarioDisponibilizou().getMMId())) {
			throw new InternalServerErrorException("usuario que disponibilizou é diferente, integridade dos dados comprometida");
		}
		if(!age.getAgendamento().getRefeicao().getTipoRefeicao().equals(apiAgeDisp.getAgendamento().getRefeicao().getTipoRefeicao())) {
			throw new InternalServerErrorException("Não é possivl alterar um tipo de refeição!");
		}
		
		if(age.getAgendamento().getCredito() != null) {
			throw new InternalServerErrorException("O agendamento já foi utilizado|creditado anteriormente, integridade dos dados comprometida");
		}
		
		age.getAgendamento().updateFromApiAgendamento(apiAgeDisp.getAgendamento());
		
		try {
			this.ageDispDAO.updateT(age);
		} catch (RollbackException| SecurityException| IllegalStateException e) {
			e.printStackTrace();
			
			throw new InternalServerErrorException("Erro ao salvar agendamento disponibilizado. "+e.getMessage() );
			//return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao salvar agendamento disponibilizado.").build();
		}
	}
	
	public void updateAgendamento(APIAgendamento apiAgendamento)  throws InternalServerErrorException, NotFoundException{

		Agendamento agendamento = null;
		agendamento = this.agendamentoDAO.findById(apiAgendamento.getMMId());
		if(agendamento == null) {
			agendamento = new Agendamento(apiAgendamento.getMMId(), apiAgendamento.getDtAgendamento());
		}else {
			if(!agendamento.getUsuario().equals(apiAgendamento.getUsuario().converteForUsuario())) {
				throw new InternalServerErrorException("agendamento pertence a outro usuário.");
			}
		}
		
		if(agendamento.getCredito() != null) {
			throw new InternalServerErrorException("agendamento já possui credito.");
		}
				
		agendamento.updateFromApiAgendamento(apiAgendamento);
		
		if(this.agendamentoDAO.existeSobreposicaoDeRefeicao(agendamento, agendamento.getUsuario())) {
			throw new InternalServerErrorException("Existe um agendamento sendo sobreposto, verifique os dados.");
		}
		
		try {
			this.agendamentoDAO.updateT(agendamento);
		} catch (RollbackException| SecurityException| IllegalStateException  e) {
			e.printStackTrace();
			throw new InternalServerErrorException("Erro ao salvar agendamento disponibilizado.");
		}
	}
}
