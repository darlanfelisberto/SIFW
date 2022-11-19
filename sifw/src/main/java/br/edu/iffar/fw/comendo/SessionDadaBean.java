package br.edu.iffar.fw.comendo;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.enterprise.context.SessionScoped;

@SessionScoped
public class SessionDadaBean implements Serializable{

	private static final long serialVersionUID = 19910222L;
	
	private HashMap<String,Object> data =  new HashMap<String, Object>();
	
	public Object getData(String id) {
		return data.get(id);
	}
	
	public void putData(String id,Object object) {
		data.put(id,object);
	}
		
}
