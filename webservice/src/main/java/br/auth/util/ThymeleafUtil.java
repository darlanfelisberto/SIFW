package br.auth.util;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;

@RequestScoped
public class ThymeleafUtil {
	
	private TemplateEngine templateEngine = null;
	
//	private @Inject ResourceContext resourceContext;
//	@Inject HttpHeaders headers;
//	@Inject Request request;
	@Inject ServletContext context;

	@PostConstruct
	public void init() {		
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(JakartaServletWebApplication.buildApplication(context));
		
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/tymeleaf/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("utf-8");

		this.templateEngine = new TemplateEngine();
		this.templateEngine.addTemplateResolver(templateResolver);
	}
	
	public String processes(String htmlFile,Map<String,Object> variables) {
		AbstractContext ctx = new Context(new Locale("pt", "br"));
    	ctx.setVariables(variables);
		return this.templateEngine.process(htmlFile, ctx);
	}
	public String processes(String htmlFile) {
		AbstractContext ctx = new Context(new Locale("pt", "br"));
		return this.templateEngine.process(htmlFile, ctx);
	}
}
