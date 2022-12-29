package br.edu.iffar.fw.comendo.primefaces;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.comendo.bean.fragment.VinculoSelecionadoBean;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@ViewScoped
public class AgendamentosLazyLoad extends LazyScheduleModel implements Serializable, ScheduleModel{

	private static final long serialVersionUID = 1L;
	
	@Inject private AgendamentosDAO agendamentosDAO;
	@Inject private VinculoSelecionadoBean vinculoSelecionadoBean;

    private List<Agendamento> events = new ArrayList<Agendamento>();
    
	public void loadEvents(LocalDateTime start, LocalDateTime end) {
		this.events = this.agendamentosDAO.findAgendamentoEmPeriodo((Model<?>)this.vinculoSelecionadoBean.getVinculoSelecionado(),start,end);
	}
	
	@Override
	public void addEvent(ScheduleEvent<?> event) {
		this.events.add((Agendamento)event);		
	}

	@Override
	public boolean deleteEvent(ScheduleEvent<?> event) {
		return events.remove(event);
	}
	
	@Override
	public List<ScheduleEvent<?>> getEvents() {
		return (List<ScheduleEvent<?>>)(List<?>) this.events;
	}

    @Override
    public ScheduleEvent<?> getEvent(String id) {
        for (ScheduleEvent<?> event : this.events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

	@Override
    public void updateEvent(ScheduleEvent<?> event) {
        int index = -1;

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                index = i;

                break;
            }
        }

        if (index >= 0) {
            this.events.set(index, (Agendamento) event);
        }else {
        	this.addEvent(event);
        }
    }

	@Override
	public int getEventCount() {
		return this.events.size();
	}

	@Override
	public void clear() {
		events = new ArrayList<>();
	}

	@Override
	public boolean isEventLimit() {
		return false;
	}
}
