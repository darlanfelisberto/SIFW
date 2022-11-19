package br.edu.iffar.fw.classBag.db.model.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Usuario;

@Entity
@Table(name = "api_usuarios")
@JsonIgnoreProperties({ "listAgendamento"
})
public class APIUsuario extends Model<UUID> {

    public static final long serialVersionUID = 22021991L;

    @Id
    @Column(name = "usuario_id", insertable = true, updatable = false)
    private UUID usuarioId;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "token_ru")
    private String tokenRu;

    @Column(name = "nome")
    private String nome;

	private boolean ativo;

//    @OneToMany(mappedBy="usuario",fetch = FetchType.LAZY)
//	private List<APIMatricula> listMatricula = new ArrayList<APIMatricula>();
//    
//    @OneToMany(mappedBy="usuario",fetch = FetchType.LAZY)
//	private List<APIServidor> listServidor = new ArrayList<APIServidor>();
//    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<APIAgendamento> listAgendamento = new ArrayList<APIAgendamento>();

//    @OneToOne(mappedBy = "usuario",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private APISaldo saldo;

    @XmlTransient
    public UUID getMMId() {
        return this.usuarioId;
    }

    public Usuario converteForUsuario() {
        Usuario u = new Usuario();
        u.setCpf(this.cpf);
        u.setIdUsuario(this.getUsuarioId());
        u.setUserName(this.username);
        return u;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String labelIniciais() {
        String[] nome = this.nome.split(" ");
        StringBuilder iniciais = new StringBuilder();
        for (String string : nome) {
            iniciais.append(string.charAt(0) + ".");
        }
        return iniciais.toString().toUpperCase();
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTokenRu() {
        return tokenRu;
    }

    public void setTokenRu(String tokenRu) {
        this.tokenRu = tokenRu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<APIAgendamento> getListAgendamento() {
        return listAgendamento;
    }

    public void setListAgendamento(List<APIAgendamento> listAgendamento) {
        this.listAgendamento = listAgendamento;
    }

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
