package br.edu.iffar.fw.ru;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.spec.ServletContextImpl;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

import static br.edu.iffar.fw.classBag.sec.HasRoleBean.ROLE_IFFAR_ADMIN;

public class ApplicationInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("ServletContainerInitializer : sifw");
        //TODO passar confiuracoes de segurança para aqui, e faze um aproveitamento e gerar o menu de navegação
    }
}
