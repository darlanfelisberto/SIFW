package br.edu.iffar.fw.classBag.interfaces;

import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.model.Curso;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
public class GrupoProcessamento implements Serializable {

    UUID id = UUID.randomUUID();

    //tipo arquivo, servidor aluno
    Boolean inativarMatriculasAusentes = false;
    Integer primeiroRegistro = 1;

    Boolean usarColunaPermissao = true;
    Integer colunaPermissao = 6;

    boolean usarColunaCurso = true;
    Integer colunaCurso = 7;
    Curso curso;

    char delimitadorColuna = ',';
    Character delimitadorTexto = '"';
    String codificacaoArquivo = "UTF-8";

    List<FileInMemory> listFile = new ArrayList<>();
    List<Permissao> listPermissoes = new ArrayList<>();

    public Set<Permissao> toSetPermissoes() {
        Set<Permissao> setPermissoes = new HashSet<>();
        setPermissoes.addAll(listPermissoes);
        return setPermissoes;
    }

    public boolean isServidor(){
        return false;
    }


    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Files[");
        listFile.forEach(f -> sb.append(f.fileName).append(","));
        sb.append("]");
        if(usarColunaCurso){
            sb.append(", Curso[coluna:"+this.colunaCurso+"]");
        }else{
            sb.append(", Curso["+this.curso.getNome()+"]");
        }
        sb.append(", Permissoes[");
        if(usarColunaPermissao){
            sb.append("coluna:"+this.colunaPermissao);
        }else {
            this.listPermissoes.forEach(permissao -> sb.append(permissao.getNome()).append(","));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrupoProcessamento that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}