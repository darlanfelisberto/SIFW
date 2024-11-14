package br.edu.iffar.fw.classBag.interfaces;

import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.model.Curso;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class GrupoProcessamento implements Serializable {

    Boolean inativarMatriculasAusentes = false;
    Integer primeiroRegistro = 1;

    Boolean usarColunaPermissao = true;
    Integer colunaPermissao = 8;

    boolean usarColunaCurso = true;
    Integer colunaCurso = 7;
    Curso curso;

    char delimitadorColuna = ',';
    Character delimitadorTexto = '"';
    String codificacaoArquivo = "UTF-8";

    List<FileInMemory> listFile = new ArrayList<>();
    List<Permissao> listPermissoes = new ArrayList<>();

}