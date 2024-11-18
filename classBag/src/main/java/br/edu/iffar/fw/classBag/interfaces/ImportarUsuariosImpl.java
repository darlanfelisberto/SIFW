package br.edu.iffar.fw.classBag.interfaces;

import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.BackgroudDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ImportarUsuariosImpl implements ImportarUsuarios,Runnable{

    @Inject
    protected BackgroudDAO backDAO;

    protected StringBuffer saidaTextoProcessamento = new StringBuffer();
    protected int line;
    protected LocalDateTime momento;
    protected List<Matricula> listMatriculaHaInativar;
    protected List<CSVRecord> listRescord;


    protected void inativaMatriculasAusentes(){
        if(this.listMatriculaHaInativar == null || this.listMatriculaHaInativar.isEmpty()){
            return;
        }
        this.listMatriculaHaInativar.forEach(mat->{
            try {
                mat.getUsuario().getPessoa().getAuthUser().setInativo(true);
                this.backDAO.mergeT(mat.getUsuario().getPessoa().getAuthUser());
                this.backDAO.persistT(new SituacaoMatricula(TypeSituacao.INATIVA, momento, mat));
                this.saidaInfo("Lançando situação: " + TypeSituacao.INATIVA.getDesc() + " para a matrícula: "+mat.getIdMatricula() + " para o usuário:"+mat.getUsuario().getPessoa().getCpf());
            } catch (RollbackException e) {
                this.saidaErro("Inativação de usuário: [matricula:" +mat.getIdMatricula()+", cpf:"+ mat.getUsuario().getPessoa().getCpf()+"]");
                this.saidaErro( e.getMessage());
            }
        });
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

    public  abstract void execute();

    @Override
    public void run(){
        try {
//            this.listCurso = this.backDAO.getCursosDAO().listAllCursos();


            this.saidaTextoProcessamento = new StringBuffer();
            this.saidaInfo("Iniciando Processamento");
            this.momento =  LocalDateTime.now();
            this.execute();
            this.inativaMatriculasAusentes();
        }catch (Exception e){
            e.printStackTrace();
        }

    };
}
