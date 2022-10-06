package br.edu.iffar.fw.comendo.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
@RequestScoped
public class LogofBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//precisa ter esrte oara ser acessado de paginas de erro
	public void logout() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) ec.getRequest();
			HttpServletResponse response = (HttpServletResponse) ec.getResponse();
			request.logout();
			System.out.println(request.getContextPath());
			response.sendRedirect(request.getContextPath());
			
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
