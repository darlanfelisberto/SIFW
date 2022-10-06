package br.edu.iffar.api;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT",realmName = "iffar")
@DeclareRoles({ "IFFAR_ADMIN", "IFFAR_RU_CATRACA" })
public class Activador extends Application {
    
}