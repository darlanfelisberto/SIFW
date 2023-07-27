package br.edu.iffar.fw.classShared.constantes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InitConstantes {

	//PATH_RESOURCEs é alterado por script de instalacao
	static public final String PATH_RESOURCES = "/opt/sifw";//<#PATH#>
	
	private static Properties config = new Properties();

	static {
		// TODO fazer scripty install.py mudar esse arquivo
		String nameFileContantes = PATH_RESOURCES + "/configuration_linux.properties";
		try {
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
