package br.edu.iffar.fw.classBag.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.omnifaces.cdi.Eager;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

@Named
@ApplicationScoped
@Eager
public class InitConstantes implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Properties config = new Properties();
	
	static {
		try {
			String nameFileContantes = "configuration_linux.properties";
			//TODO parece que tem algum bug que fica uma / antes da uri do path, verificar futuramente 17.0.5.8
			if(System.getProperty("os.name").contains("Win")) {
//				 if(InitConstantes.class.getResource("").getPath().charAt(0) == '/') {
//					 nameFileContantes = InitConstantes.class.getResource("").getPath().substring(1) + "configuration_win.properties";
//				 }else {
					 nameFileContantes = "configuration_win.properties";
//				 }
		    }
			
	        InputStream configFile = InitConstantes.class.getResourceAsStream(nameFileContantes);
	        if (configFile != null) {
	            try {
	                config.load(configFile);
	            } catch (IOException e) {
	                throw new IllegalStateException("Could not load OpenIdConfig");
	            }
	        }
		} catch (Exception e) {
			System.err.println("Não foi encontrado arquivo de configuração de constantes.");
			System.exit(1);
		}
	}

	private static String APP_REAL_PATH;
	
	static public final String IMAGEM_PATH = config.getProperty("imagem.path");
	static public final String IMAGEM_EXTENSAO = config.getProperty("imagem.extencao");
	

	
	@Inject
	ServletContext servletContext;


	@PostConstruct
	public void init() {
		try {
			APP_REAL_PATH = servletContext.getRealPath("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
