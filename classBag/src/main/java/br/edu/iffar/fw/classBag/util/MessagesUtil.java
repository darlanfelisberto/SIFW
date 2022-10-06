package br.edu.iffar.fw.classBag.util;

import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;

@RequestScoped
public class MessagesUtil {
	
	private FacesContext fc = FacesContext.getCurrentInstance();
	
	@Inject private UsuariosDAO usuariosDAO;	
	
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
		System.out.println("user:"+this.usuariosDAO.getUsernameLogado()+"|"+summary);
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
	}
	
	public void addWarn(String summary,String details) {
		System.out.println("user:"+this.usuariosDAO.getUsernameLogado()+"|"+summary+"|"+details);
		fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details));
	}
	
	public void addError(RollbackException e ) {
		if(e.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException c = (ConstraintViolationException) e.getCause();
			c.getConstraintViolations().forEach(violation->{
				System.out.println("user:"+this.usuariosDAO.getUsernameLogado()+"|"+violation.getMessage());
				fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, violation.getMessage(), ""));
			});		
		}
	}
	
	public void addError(Set<ConstraintViolation<Object>> violacoes) {
		violacoes.forEach(cv->{
			System.out.println("user:"+this.usuariosDAO.getUsernameLogado()+"|"+cv.getMessage());
			fc.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, cv.getMessage(), ""));
		});
	}

}
