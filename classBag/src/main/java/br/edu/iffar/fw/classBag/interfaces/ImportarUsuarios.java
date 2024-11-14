package br.edu.iffar.fw.classBag.interfaces;

import br.edu.iffar.fw.classBag.db.model.Curso;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

public interface ImportarUsuarios {

    public void setDados(StringBuffer saida, Curso curso, Integer coluna, boolean inativarMatriculaAusente, int firstRecord);
    public void setDados(Configs configs);
    public void initConfigs();

    default public String getNomePadrao(String nome) {
        String[] nn = nome.toLowerCase().trim().split(" ");
        StringBuilder s = new StringBuilder();
        for (String string : nn) {
            s.append(" "+(string.length()>2?string.substring(0, 1).toUpperCase()+string.substring(1, string.length()):string));
        }
        //esse trim remove o espaço colocado no for
        return s.toString().trim();
    }
}
