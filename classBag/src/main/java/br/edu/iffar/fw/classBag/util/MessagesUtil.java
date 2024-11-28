package br.edu.iffar.fw.classBag.util;

import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.transaction.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RequestScoped
public class MessagesUtil {
	
	private FacesContext fc = FacesContext.getCurrentInstance();
	
	public MessagesUtil() {}
	
	public void addSuccess(String summary,String details) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, summary, details));
	}
	
	public void addSuccess(String summary) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, summary,""));
	}
	
	public void addError(String summary,String details) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, details));
	}
	
	public void addError(String summary) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
	}

	public void addWarn(String summary) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, summary,""));
	}

	public void addWarn(String summary,String details) {
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details));
	}
	
	public void addError(RollbackException e ) {
		if(e.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException c = (ConstraintViolationException) e.getCause();
			c.getConstraintViolations().forEach(violation->{
				fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, violation.getMessage(), ""));
			});		
		}
	}
	
	public void addError(Set<ConstraintViolation<Object>> violacoes) {
		violacoes.forEach(cv->{
			fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, cv.getMessage(), ""));
		});
	}
}
