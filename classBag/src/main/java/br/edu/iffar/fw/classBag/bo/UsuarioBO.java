package br.edu.iffar.fw.classBag.bo;

import java.util.*;

import br.com.feliva.authClass.dao.AuthUserDAO;
import br.com.feliva.authClass.dao.PermissaoDAO;
import br.com.feliva.authClass.dao.PessoaDAO;
import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.excecoes.SenhaException;
import br.edu.iffar.fw.classBag.excecoes.UsuarioException;
import br.edu.iffar.fw.classBag.sec.HasRoleBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;

@RequestScoped
@Transactional
public class UsuarioBO {

    @Inject private HasRoleBean hasRoleBean;
    @Inject private PermissaoDAO permissaoDAO;
    @Inject private UsuariosDAO usuariosDAO;
    @Inject private AuthUserDAO authUserDAO;
    @Inject private PessoaDAO pessoaDAO;

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

        this.user.getPessoa().getAuthUser().setPassword(senha);
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
        this.authUserDAO.merge(this.user.getPessoa().getAuthUser());
    }

    public List<Permissao> buscaPermissoesDisponiveis(){
        List<Permissao> l = this.permissaoDAO.listAll();
        if(!this.hasRoleBean.isHasIffarAdmin()){
            if(this.user.getPessoa().getAuthUser().getSetPermissao() != null){
                //remove permisoes que o usuario ja tem da lista de opções
                l.removeAll(this.user.getPessoa().getAuthUser().getSetPermissao().stream().toList());
                return l;
            }else{
                return l;
            }
        }
        return l;
    }

    public UsuarioBO setPermissao( List<Permissao> permissaoList){
        if(permissaoList.size() < 1){
           throw new UsuarioException("Selecione ao menos uma permissão para o usuário.");
        }

        if(this.user.getPessoa().getAuthUser().getSetPermissao() != null){
            this.user.getPessoa().getAuthUser().getSetPermissao().clear();
            this.user.getPessoa().getAuthUser().getSetPermissao().addAll(permissaoList);
        }else{
            Set<Permissao> permissaos = new HashSet<Permissao>();
            permissaos.addAll(permissaoList);
            this.user.getPessoa().getAuthUser().setSetPermissao(permissaos);
        }

        return this;
    }

    public void salvarUser() throws RollbackException,UsuarioException {
        if(this.user.isNovo()) {
            if(this.usuariosDAO.findByUserName(this.user.getPessoa().getAuthUser().getUsername()) != null) {
                throw new UsuarioException("Username já existe!");
            }

            if(this.usuariosDAO.getUsuarioByCPF(this.user.getPessoa().getCpf()) != null) {
                throw new UsuarioException("CPF já existe!");
            }
            this.pessoaDAO.persist(this.user.getPessoa());
            this.usuariosDAO.persist(this.user);
        }else{
            this.pessoaDAO.merge(this.user.getPessoa());
            this.usuariosDAO.merge(this.user);
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
