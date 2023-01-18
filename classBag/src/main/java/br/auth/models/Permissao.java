package br.auth.models;

import br.edu.iffar.fw.classBag.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "permissao", schema = "auth")
public class Permissao extends Model<UUID> {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "permissao_id", insertable = false)
    private UUID permissaoId;

    private String nome;

    public Permissao(){}

    public UUID getMMId() {
        return this.permissaoId;
    }

    public UUID getPermissaoId() {
        return permissaoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
