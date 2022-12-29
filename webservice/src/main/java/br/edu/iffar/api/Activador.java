package br.edu.iffar.api;

import org.eclipse.microprofile.auth.LoginConfig;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT",realmName = "iffar")
@DeclareRoles({ "IFFAR_ADMIN", "IFFAR_RU_CATRACA" })
public class Activador extends Application {
    
}