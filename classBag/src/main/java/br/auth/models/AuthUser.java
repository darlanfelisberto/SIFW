package br.auth.models;

import java.util.Set;

import br.auth.oidc.Pbkdf2Hash;
import br.edu.iffar.fw.classBag.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "auth_user", schema = "auth")
public class AuthUser extends  Model<Integer>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth_user")
    private Integer idAuthUser;

    private String username;

    private String password;

    private String salt;

    @Transient
    private String nome;

    private String email;

    @ManyToMany
    @JoinTable(
            name ="auth_user_permissao",
            schema = "auth",
            joinColumns = @JoinColumn(name = "id_auth_user"),
            inverseJoinColumns = @JoinColumn(name = "id_permissao")
    )
    private Set<Permissao> listPermissao;

    public Integer getMMId() {
        return this.idAuthUser;
    }

    public Integer getIdAuthUser() {
        return idAuthUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean authenticate(String password){
        Pbkdf2Hash pbkd = new Pbkdf2Hash().initialize(Pbkdf2Hash.parametersDefault);
        return pbkd.verify(this.password,pbkd.generate(password, this.salt));
    }

    public String[] getArrayPermissoes(){
        String[] array = new String[this.listPermissao.size()+1];
        int c = 0;
        for (Permissao p : this.listPermissao) {
            array[c++] = p.getNome();
        }
        array[c] = "user";
        return array;
    }

    public Set<Permissao> getListPermissao() {
        return listPermissao;
    }

    public void setListPermissao(Set<Permissao> listPermissao) {
        this.listPermissao = listPermissao;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
