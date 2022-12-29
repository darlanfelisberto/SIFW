package br.auth.util;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import br.auth.oidc.OidcKeysUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ThymeleafUtil {
	
	private TemplateEngine templateEngine = null;

	@PostConstruct
	public void init() {
		AbstractConfigurableTemplateResolver templateResolver = new FileTemplateResolver();

		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix(OidcKeysUtil.PATH_WAR + "/WEB-INF/tymeleaf/");
		templateResolver.setSuffix(".html");

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
