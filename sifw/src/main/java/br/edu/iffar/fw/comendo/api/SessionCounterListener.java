package br.edu.iffar.fw.comendo.api;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

//@ WebListener
public class SessionCounterListener implements ServletContextListener, HttpSessionListener{//, ServletRequestListener{

    private static final String ATTRIBUTE_NAME = "com.example.SessionCounter";
    private Map<HttpSession, String> sessions = new ConcurrentHashMap<HttpSession, String>();

    @Inject
    HttpServletRequest request;
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(ATTRIBUTE_NAME, this);
        System.out.println("----------- contextInitialized");
    }

//    @Override
//    public void requestInitialized(ServletRequestEvent event) {
//        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
//        HttpSession session = request.getSession(false);
//        if (session!= null && session.isNew()) {
//            sessions.put(session, request.getRemoteAddr());
//        }
//        System.out.println("----------- requestInitialized");
//    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession());
        System.out.println("----------- sessionDestroyed");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // NOOP. Useless since we can't obtain IP here.
    	sessions.put(event.getSession(), request.getRemoteAddr());
    	System.out.println("----------- sessionCreated");
    }

//    @Override
//    public void requestDestroyed(ServletRequestEvent event) {
//        // NOOP. No logic needed.
//    	System.out.println("----------- requestDestroyed");
//    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	System.out.println("----------- contextDestroyed");
        // NOOP. No logic needed. Maybe some future cleanup?
    }

    public static SessionCounterListener getInstance(ServletContext context) {
        return (SessionCounterListener) context.getAttribute(ATTRIBUTE_NAME);
    }

    public int getCount(String remoteAddr) {
        return Collections.frequency(sessions.values(), remoteAddr);
    }

}