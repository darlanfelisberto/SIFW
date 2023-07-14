package br.auth.models;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.sec.Pbkdf2Hash;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "auth_user", schema = "auth")
public class AuthUser extends  Model<UUID>{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "auth_user_id",insertable = true,updatable = false,unique = true)
    private UUID authUserId;

    @NotNull
    private String username;

    @NotNull
    private String password;
    @NotNull
    private String salt;

    private boolean inativo;

    @NotNull
    private String email;

    @ManyToMany
    @JoinTable(
            name ="auth_user_permissao",
            schema = "auth",
            joinColumns = @JoinColumn(name = "auth_user_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> setPermissao;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authUser",optional = false)
    private Usuario usuario;

    public UUID getMMId() {
        return this.authUserId;
    }

    public UUID getIdAuthUser() {
        return authUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        if(userName != null) {
            this.username = userName.replaceAll("[^0-9a-zA-Z]", "");
        } else {
            this.username = userName;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null || password.trim().isEmpty()){
            return;
        }
        Pbkdf2Hash pbkd = new Pbkdf2Hash();
        if(this.salt == null){
            this.salt = pbkd.generateSalt();
        }
        this.password = pbkd.generate(password, this.salt);
    }

    public boolean authenticate(String password){
        Pbkdf2Hash pbkd = new Pbkdf2Hash();
        return pbkd.verify(this.password,pbkd.generate(password, this.salt));
    }

    public String[] getArrayPermissoes(){
        String[] array = new String[this.setPermissao.size()+1];
        int c = 0;
        for (Permissao p : this.setPermissao) {
            array[c++] = p.getNome();
        }
        array[c] = "user";
        return array;
    }

    public Set<Permissao> getSetPermissao() {
        return setPermissao;
    }

    public void setSetPermissao(Set<Permissao> listPermissao) {
        this.setPermissao = listPermissao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuario.setAuthUser(this);
    }

    public boolean isInativo() {
        return inativo;
    }

    public void setInativo(boolean inativo) {
        this.inativo = inativo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static AuthUser  createNew(Usuario usuario,Set<Permissao> permissoes){
        AuthUser au = new AuthUser();
        au.setUsuario(usuario);
        au.setUsername(usuario.getCpf());
        au.setSetPermissao(permissoes);
        au.setPassword(usuario.getCpf());
        au.inativo = false;
        return au;
    }
}
