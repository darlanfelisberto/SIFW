package br.edu.iffar.fw.classBag.interfaces;

import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.BackgroudDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public abstract class ImportarUsuariosImpl implements ImportarUsuarios,Runnable{

    @Inject
    protected BackgroudDAO backDAO;

    protected StringBuffer saidaTextoProcessamento;
    protected int line;
    protected LocalDateTime momento;
    protected List<Matricula> listMatriculaHaInativar;


    protected void inativaMatriculasAusentes(){
        LocalDateTime momento = LocalDateTime.now();
        this.listMatriculaHaInativar.forEach(mat->{
            try {
                mat.getUsuario().getPessoa().getAuthUser().setInativo(true);
                this.backDAO.mergeT(mat.getUsuario().getPessoa().getAuthUser());
                this.backDAO.persistT(new SituacaoMatricula(TypeSituacao.INATIVA, momento, mat));
                this.saidaInfo("Lançando situação: " + TypeSituacao.INATIVA.getDesc() + " para a matrícula: "+mat.getIdMatricula() + " para o usuário:"+mat.getUsuario().getPessoa().getCpf());
            } catch (RollbackException e) {
                e.printStackTrace();
                this.saidaErro("Erro ao Lançar situacão Inativa para o aluno: " + mat.getUsuario().getPessoa().getCpf()+"  --  "+e.getMessage());
            }
        });
        this.saidaSucess("Situacoes salvas com sucesso.");
    }

    public String getSaida(){
        return this.saidaTextoProcessamento.toString();
    }

    public void saidaErro(String msg) {
        String m = String.format("error  [line:%d] %s <br/>",this.line, msg);
        this.saidaTextoProcessamento.append(m);
    }

    public void saidaSucess(String msg) {
        String m = String.format("sucess [line:%d] %s <br/>",this.line, msg);
        this.saidaTextoProcessamento.append(m);
    }

    public void saidaInfo(String msg) {
        String m = String.format("info   [line:%d] %s <br/>",this.line, msg);
        this.saidaTextoProcessamento.append(m);
    }

    @Override
    public void run(){
        this.saidaTextoProcessamento = new StringBuffer();
        this.saidaInfo("Iniciando Processamento");
    };
}
