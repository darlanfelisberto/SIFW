package br.edu.iffar.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import br.edu.iffar.fw.classShared.constantes.InitConstantes;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Path("/img")
@RequestScoped
public class ImageEndPoint implements Serializable{
	
	private static final long serialVersionUID = 22021991L;
	@Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;
		
	@GET                                                             
    @Path("/{img}.png")
	@Produces({"image/png"})
    public Response  getImgb(@PathParam("img") String img) throws IOException {

		if(!Files.exists(Paths.get(InitConstantes.IMAGEM_PATH+ img+ InitConstantes.IMAGEM_EXTENSAO))) {
			return null;
		}
		CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
		return Response.ok(Files.newInputStream(Paths.get(InitConstantes.IMAGEM_PATH+ img+ InitConstantes.IMAGEM_EXTENSAO)),"image/png")
				.cacheControl(cacheControl)
				.build();		
	}
}
