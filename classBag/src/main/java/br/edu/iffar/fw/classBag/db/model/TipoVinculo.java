package br.edu.iffar.fw.classBag.db.model;

import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="tipo_vinculo")
@Cacheable
public class TipoVinculo extends Model<UUID>{
	
	public static final int TIPO_MATRICULA_INTEGRADO 		= 10;
	public static final int TIPO_MATRICULA_SUBSEQUENTE 		= 20;
	public static final int TIPO_MATRICULA_GRADUACAO 		= 30;
	
	public static final int TIPO_USUARIO_ALUNO 				= 40;
	public static final int TIPO_USUARIO_PROFESSOR 			= 50;
	public static final int TIPO_USUARIO_TAE 				= 60;
	public static final int TIPO_USUARIO_TERCEIRIZADO  		= 70;
	public static final int TIPO_USUARIO_EXTERNO  			= 100;

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "tipo_vinculo_id",insertable = true)
	private UUID tipoVinculoId;
	
	@Column(name = "id_tipo_vinculo",unique = true)
	private Integer idTipoVinculo;
	
	private String descricao;
	
	@Column(name = "tipo_usuario")
	private boolean tipoUsuario;
	
	@Column(name = "tipo_matricula")
	private boolean tipoMatricula;
	
	private Character nivel;
	
	@Override
	public UUID getMMId() {
		return this.tipoVinculoId;
	}

	public String getDesc() {
		return this.descricao;
	}

	public boolean isTipoUsuario() {
		return tipoUsuario;
	}

	public boolean isTipoMatricula() {
		return tipoMatricula;
	}

	public void setDesc(String desc) {
		this.descricao = desc;
	}

	public void setTipoUsuario(boolean tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void setTipoMatricula(boolean tipoMatricula) {
		this.tipoMatricula = tipoMatricula;
	}
	
	@XmlTransient
	public boolean isAluno() {
		return this.idTipoVinculo.intValue() <= TipoVinculo.TIPO_USUARIO_ALUNO;
	}

	public UUID getTipoVinculoId() {
		return tipoVinculoId;
	}

	public void setTipoVinculoId(UUID tipoVinculoId) {
		this.tipoVinculoId = tipoVinculoId;
	}

	public Integer getIdTipoVinculo() {
		return idTipoVinculo;
	}

	public void setIdTipoVinculo(Integer idTipoVinculo) {
		this.idTipoVinculo = idTipoVinculo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Character getNivel() {
		return nivel;
	}

	public void setNivel(Character nivel) {
		this.nivel = nivel;
	}

	@XmlTransient
	public boolean isIntegrado() {
		return this.idTipoVinculo.equals(TIPO_MATRICULA_INTEGRADO);
	}

	public boolean isSubSequente() {
		return this.idTipoVinculo.equals(TIPO_MATRICULA_SUBSEQUENTE);
	}
	
	
}
