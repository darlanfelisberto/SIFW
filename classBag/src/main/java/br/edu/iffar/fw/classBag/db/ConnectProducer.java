package br.edu.iffar.fw.classBag.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.servlet.ServletContext;
import javax.sql.DataSource;

import org.omnifaces.cdi.Eager;


@Eager
@ApplicationScoped
public class ConnectProducer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String APP_REAL_PATH;
	
	@PersistenceUnit // (name = "baseUnit")
	private static EntityManagerFactory emBU;

	@PersistenceContext // (unitName = "baseUnit")
    @Produces
	private static EntityManager em;
	
	@Resource(mappedName = "java:jboss/datasources/baseDS") // same JNDI used by Hibernate Persistence Unit
	private static DataSource dss;
	
	@Inject
	private ServletContext context;
	
	@PostConstruct
	public void init() {
		try {
			APP_REAL_PATH = context.getRealPath("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Produces
//	public EntityManager getShoolEntityManeger() {
////		System.out.println("createEntityManager");
//		return emBU.createEntityManager();
//	}
//	
//	public void close(@Disposes EntityManager em) {
//		if(em.isOpen()) {
//			em.close();
//		}
//	}

	public void close(@Disposes Connection com) {
		try {
			if(!com.isClosed()) {
				com.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Produces
	public static Connection getConnection() throws SQLException {
		if(dss == null) {   
	        try {
	            Context ctx = new InitialContext();
	            dss = (DataSource) ctx.lookup("java:jboss/datasources/baseDS");
	        }catch (NamingException e) {
	            e.printStackTrace();
	        }
	    }
		
		
		return dss.getConnection();
	}

	public static String getAPP_REAL_PATH() {//pq não é final property
		return APP_REAL_PATH;
	}
	
}