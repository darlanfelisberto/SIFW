package br.edu.iffar.fw.classBag.db.model.api;

import br.edu.iffar.fw.classBag.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "api_usuario_refeicao")
public class APIUsuarioRefeicao extends Model<APIUsuarioRefeicaoPK>{
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private APIUsuarioRefeicaoPK id;
	
	@NotNull(message = "Informe o tipo da refeição.")
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "refeicao_id",referencedColumnName = "refeicao_id",insertable = false,updatable = false)
	private APIRefeicao2 refeicao;
		
	@NotNull(message = "Informe o usuario do agendamento.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id",referencedColumnName = "usuario_id",insertable = false,updatable = false)
	private APIUsuario usuario;
	
	@XmlTransient
	private boolean ativa = true;

	@Override
	public APIUsuarioRefeicaoPK getMMId() {
		return id;
	}

	public APIRefeicao2 getRefeicao() {
		return refeicao;
	}

	public void setRefeicao(APIRefeicao2 refeicao) {
		this.refeicao = refeicao;
	}

	public APIUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(APIUsuario usuario) {
		this.usuario = usuario;
	}

	public APIUsuarioRefeicaoPK getId() {
		return id;
	}

	public void setId(APIUsuarioRefeicaoPK id) {
		this.id = id;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	
	
		
}