//package br.edu.iffar.fw.comendo.db.model;
//
//import java.io.Serializable;
//import javax.persistence.*;
//import java.util.List;
//import java.util.UUID;
//
//
///**
// * The persistent class for the permissoes database table.
// * 
// */
//@Entity
//@Table(name="permissoes")
//@NamedQuery(name="Permissao.findAll", query="SELECT p FROM Permissao p")
//public class Permissao extends br.edu.iffar.fw.comendo.db.Model implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name="id_permissao")
//	private String idPermissao;
//
//	private String permissao;
//
//	//bi-directional many-to-many association to Usuario
////	@ManyToMany(mappedBy="permissoes")
////	private List<Usuario> usuarios;
//
//	public Permissao() {
//	}
//
//	public String getIdPermissao() {
//		return this.idPermissao;
//	}
//
//	public void setIdPermissao(String idPermissao) {
//		this.idPermissao = idPermissao;
//	}
//
//	public String getPermissao() {
//		return this.permissao;
//	}
//
//	public void setPermissao(String permissao) {
//		this.permissao = permissao;
//	}
//
//	public List<Usuario> getUsuarios() {
//		return this.usuarios;
//	}
//
//	public void setUsuarios(List<Usuario> usuarios) {
//		this.usuarios = usuarios;
//	}
//
//	@Override
//	public UUID getId() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}