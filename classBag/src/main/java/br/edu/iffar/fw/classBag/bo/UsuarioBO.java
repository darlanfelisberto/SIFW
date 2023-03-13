package br.edu.iffar.fw.classBag.bo;

import br.auth.dao.AuthUserDAO;
import br.auth.dao.PermissaoDAO;
import br.auth.models.AuthUser;
import br.auth.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.excecoes.SenhaException;
import br.edu.iffar.fw.classBag.excecoes.UsuarioException;
import br.edu.iffar.fw.classBag.sec.FaceletsAuthorizeTagHandler;
import br.edu.iffar.fw.classBag.sec.HasRoleBean;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import org.primefaces.model.DualListModel;
import org.wildfly.security.http.oidc.OidcSecurityContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Transactional
public class UsuarioBO {

    @Inject private HasRoleBean hasRoleBean;
    @Inject private PermissaoDAO permissaoDAO;
    @Inject private UsuariosDAO usuariosDAO;
    @Inject private AuthUserDAO authUserDAO;

    private Usuario user;

    private String senha;
    private String reSenha;

    public UsuarioBO init(Usuario user){
        this.user = user;
        return this;
    }
    public UsuarioBO trocaSenha(String senha, String reSenha) throws SenhaException{
        if(senha == null || reSenha == null ){
            throw new SenhaException("Informe a senha e a sua confirmação.");
        }

        if(senha.trim().replaceAll("^[a-zA-Z0-9!@#$%*..]","").length() < 8){
            throw new SenhaException("A senha deve ter ao menos de 8 caracteres.");
        }

        if(!senha.trim().equals(reSenha.trim())){
            throw new SenhaException("Senhas diferentes.");
        }

        this.user.getAuthUser().setPassword(senha);
        return this;
    }

    public UsuarioBO trocaSenha() throws SenhaException{
        return this.trocaSenha(this.senha,this.reSenha);
    }

    public UsuarioBO trocaSenhaNullPass() throws SenhaException{
        if((this.senha== null || this.senha.trim().isEmpty()) && (this.reSenha== null || this.reSenha.trim().isEmpty())){
            return this;
        }else {
            return this.trocaSenha(this.senha, this.reSenha);
        }
    }

    public void salvarAuth() throws RollbackException {
        this.authUserDAO.update(this.user.getAuthUser());
    }

    public List<Permissao> buscaPermissoesDisponiveis(){
        if(this.hasRoleBean.isHasIffarAdmin()){
            List<Permissao> l = this.permissaoDAO.findAll();
            if(this.user.getAuthUser().getSetPermissao() != null){
                //remove permisoes que o usuario ja tem da lista de opções
                l.removeAll(this.user.getAuthUser().getSetPermissao().stream().toList());
                return l;
            }else{
                return l;
            }

        }
        //permissoes que o usuario que esta logado no momento possui, são as que ele pode mudar nos demais usuarios
        return (this.user.getAuthUser().getSetPermissao() != null ? this.user.getAuthUser().getSetPermissao().stream().toList():new ArrayList<Permissao>());
    }

    public UsuarioBO setPermissao( List<Permissao> permissaoList){
        if(permissaoList.size() < 1){
           throw new UsuarioException("Selecione ao menos uma permissão para o usuário.");
        }

        this.user.getAuthUser().getSetPermissao().clear();
        this.user.getAuthUser().getSetPermissao().addAll(permissaoList);
        return this;
    }

    public void salvarUser() throws RollbackException,UsuarioException {
        if(this.user.isNovo()) {
            if(this.usuariosDAO.findByUserName(this.user.getAuthUser().getUsername()) != null) {
                throw new UsuarioException("Username já existe!");
            }

            if(this.usuariosDAO.getUsuarioByCPF(this.user.getCpf()) != null) {
                throw new UsuarioException("CPF já existe!");
            }
            this.authUserDAO.persist(this.user.getAuthUser());
            this.usuariosDAO.persist(this.user);
        }else{
            this.authUserDAO.update(this.user.getAuthUser());
            this.usuariosDAO.update(this.user);
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getReSenha() {
        return reSenha;
    }

    public void setReSenha(String reSenha) {
        this.reSenha = reSenha;
    }
}
