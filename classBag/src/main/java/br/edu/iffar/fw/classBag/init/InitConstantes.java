package br.edu.iffar.fw.classBag.init;

import static br.edu.iffar.fw.classBag.init.InitConstantes.OIDC_JWK_PATH;
import static br.edu.iffar.fw.classBag.init.InitConstantes.OIDC_JWT_FILENAME;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.omnifaces.cdi.Eager;

import com.nimbusds.jose.jwk.RSAKey;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

public class InitConstantes {

	private static Properties config = new Properties();
	
	static {
		String nameFileContantes = "/opt/sifw/configuration_linux.properties";
		try {
			if(System.getProperty("os.name").contains("Win")) {
				nameFileContantes = "C:\\app\\sifw\\configuration_win.properties";
		    }
			
			FileInputStream configFile = new FileInputStream(nameFileContantes);
	        if (configFile != null) {
	            try {
	                config.load(configFile);
	            } catch (IOException e) {
	                throw new IllegalStateException("Could not load OpenIdConfig");
	            }
	        }
		} catch (Exception e) {
			System.err.println("Não foi encontrado arquivo de configuração de constantes: " + nameFileContantes);
			System.exit(1);
		}
	}

	
	static public final String IMAGEM_PATH 			= config.getProperty("imagem.path");
	static public final String IMAGEM_EXTENSAO 		= config.getProperty("imagem.extencao");
	static public final int    OIDC_JWT_SIZE 		= Integer.parseInt(config.getProperty("oidc.jwk.size"));
    static public final String OIDC_JWT_FILENAME 	= config.getProperty("oidc.jwk.filename");
    static public final String OIDC_JWK_PATH 		= config.getProperty("oidc.jwk.path");
    static public final String OIDC_ISSUR 			= config.getProperty("oidc.issur");
}
