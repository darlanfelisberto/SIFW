package br.edu.iffar.fw.classBag.db;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class LogUtil {
	
//	@Inject private EntityManager em;
	
	private static LogUtil l = new LogUtil();
	
	static public LogUtil getLogUtil() {
		
		return l;
	}
	
	

}
