package br.edu.iffar.fw.classBag.db.model.api;

import java.io.Serializable;
import java.util.Objects;


public class ContainerSendModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private APIAgendamentosDisponibilizados agendamentosDisponibilizados;
	
	private APIAgendamento agendamento;

	public ContainerSendModel() {
		
	}
	
	public ContainerSendModel(APIAgendamento agendamento, APIAgendamentosDisponibilizados agendamentosDisponibilizados) {
		this.agendamento = agendamento;
		this.agendamentosDisponibilizados = agendamentosDisponibilizados;
	}

	public APIAgendamentosDisponibilizados getAgendamentosDisponibilizados() {
		return agendamentosDisponibilizados;
	}

	public void setAgendamentosDisponibilizados(APIAgendamentosDisponibilizados agendamentosDisponibilizados) {
		this.agendamentosDisponibilizados = agendamentosDisponibilizados;
	}

	public APIAgendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(APIAgendamento agendamento) {
		this.agendamento = agendamento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agendamentosDisponibilizados, agendamento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContainerSendModel other = (ContainerSendModel) obj;
		return Objects.equals(agendamentosDisponibilizados, other.agendamentosDisponibilizados)
				&& Objects.equals(agendamento, other.agendamento);
	}
}