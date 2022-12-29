package br.auth.models;

import static br.auth.oidc.OpenIdConstant.BASIC_TYPE;
import static br.auth.oidc.OpenIdConstant.CODE;
import static br.auth.oidc.OpenIdConstant.STATE;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_login", schema = "auth")
public class AuthLogin extends Model<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth_login", insertable = false)
    private Integer idAuthLogin;

    private UUID state;

    private UUID code;

    @Column(name = "valido_ate")
    private LocalDateTime validoAte;

    private boolean usado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private AuthUser usuario;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    public AuthLogin(){}

    public AuthLogin(UUID state, UUID code, AuthUser usuario,Cliente cliente) {
        this.state = state;
        this.code = code;
        this.usuario = usuario;
        this.cliente = cliente;
        //o client tem 30 segundos para solicitar o access token apartir deste code
        this.validoAte = LocalDateTime.now().plusSeconds(30);
        this.usado = false;
    }

    public boolean aindaValido(){
        return LocalDateTime.now().isBefore(this.validoAte);
    }

    public boolean verifyHeaderAuthorization(String authorizationHeader){
        String autorization = new String(Base64.getDecoder()
                                        .decode(authorizationHeader
                                        .substring(BASIC_TYPE.length() + 1))
                                , StandardCharsets.UTF_8);
        return autorization.equals(this.cliente.getNome() + ":" + this.cliente.getSecret().toString());
    }
    public boolean verifyHeaderAuthorization(String client,String secret){
        if(!this.cliente.getNome().equals(client)){
            return false;
        }
        if(this.cliente.getSecret() != null && !this.cliente.getSecret().toString().equals(secret)){
            return false;
        }

        return true;
    }

    public String queryParam(){
        return "?" + STATE + "=" + state + "&" + CODE + "=" + getCode();
    }

    @Override
    public Integer getMMId() {
        return this.idAuthLogin;
    }

    public Integer getIdAuthLogin() {
        return idAuthLogin;
    }

    public void setIdAuthLogin(Integer idAuthLogin) {
        this.idAuthLogin = idAuthLogin;
    }

    public UUID getState() {
        return state;
    }

    public void setState(UUID state) {
        this.state = state;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public AuthUser getUsuario() {
        return usuario;
    }

    public void setUsuario(AuthUser usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getValidoAte() {
        return validoAte;
    }

    public void setValidoAte(LocalDateTime validoAte) {
        this.validoAte = validoAte;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
