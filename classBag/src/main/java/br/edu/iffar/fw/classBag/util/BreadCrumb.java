package br.edu.iffar.fw.classBag.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.menu.BaseMenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuModel;

public class BreadCrumb implements Serializable{
	private static final long serialVersionUID = 22021991L;
	public static final int RAIZ = 0;
//	public static final String THIS_PAGE = "";
	
	private class No implements Serializable{
		private static final long serialVersionUID = 22021991L;
		
		private int anterior;
		String command;
		String update;
		String nome;
		String url;
		
		public No(int anterior, String command, String update, String nome) {
			super();
			this.anterior = anterior;
			this.command = command;
			this.update = update;
			this.nome = nome;
		}
		
		public No(int anterior, String url) {
			super();
			this.anterior = anterior;
			this.url = url;
		}
		
		public No(int anterior, String url,String nome) {
			super();
			this.anterior = anterior;
			this.url = url;
			this.nome = nome;
		}

		public DefaultMenuItem  build() {
			return DefaultMenuItem.builder().value(this.nome)
					.command(command)
					.url(this.url)
//					.ajax(false)
					.immediate(true)
					.process("@this")
					.update(update)
					.build();
		}
	}

	private List<No> nos;
	private BaseMenuModel listItemBreadCrumb = new BaseMenuModel();
	
	
	public MenuModel getModel() {
		return this.listItemBreadCrumb ;
	}
	
	public BreadCrumb inicializa(){
		this.nos = new ArrayList<No>();
		this.nos.add(new No(-1,"index.xhtml"));//raiz a casinha
		return this;
	}
	
	private void getElement(No index){
		if(index.anterior > -1){
			getElement(this.nos.get(index.anterior));
		}
		this.listItemBreadCrumb.getElements().add(index.build());
	}
	
	public void setAtivo(int ativo){
		this.listItemBreadCrumb =new BaseMenuModel();
		this.getElement(this.nos.get(ativo));

//		this.listItemBreadCrumb.generateUniqueIds();
	}
	
	public BreadCrumb addItem(String nome,String command, String update,int anterior){
		this.nos.add(new No(anterior, command, update, nome));
		return this;
	}
//	
	public BreadCrumb addItem(String nome,String url, int anterior){
		this.nos.add(new No(anterior,url,nome));
		return this;
	}
	
	public BreadCrumb addItem(String nome, int anterior){
		this.nos.add(new No(anterior, null, null, nome));
		return this;
	}
}