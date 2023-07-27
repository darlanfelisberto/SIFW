package br.edu.iffar.api;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML;

import java.io.Serializable;

import br.auth.util.ThymeleafUtil;
import br.edu.iffar.fw.authClassShared.dao.AuthUserDAO;
import br.edu.iffar.fw.authClassShared.models.AuthUser;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


//http://localhost:8080/catraca/auth/catracaAPI/listAllGrupos
@Path("/email")
@RequestScoped
public class EmailEndPoint implements Serializable{
	
	private static final long serialVersionUID = 22021991L;
	@Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;
	
	@Inject
	private AuthUserDAO authUserDAO;
	
    @Inject
    ThymeleafUtil thymeleafUtil;
    
	
	@POST                                                             
    @Path("/gerarlink")
    public void gerarLink(@FormParam("username") String username) {
		
		AuthUser user = this.authUserDAO.findByUsername(username);
		if(user == null) {
			return;
		}

		try{
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("darlan.felisberto@iffarroupilha.edu.br");
            Address[] to = new InternetAddress[] {new InternetAddress(user.getEmail()) };
            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("WildFly Mail");
            m.setSentDate(new java.util.Date());
            m.setContent("Mail sent from WildFly","text/plain");
            Transport.send(m);
            System.out.println("enviado");
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
	}
	
	@GET                                                             
    @Path("/recuperarsenha")
	@Produces(MediaType.TEXT_HTML)
    public Response recuperarSenha() {
		//http://localhost:8080/service/email/recuperarsenha/email

		return Response.ok(thymeleafUtil.processes("formRecuperarSenha"), TEXT_HTML).build();
	}
}
