package br.edu.iffar.fw.classBag.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContext;
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