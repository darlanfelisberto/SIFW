package br.edu.iffar.fw.classBag.interfaces;

import br.edu.iffar.fw.classBag.db.model.Curso;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.SelectEvent;

import java.util.List;

public interface ImportarUsuarios {

    public void setDados(Configs configs);
    public void initConfigs();
    public void onConfig(SelectEvent<Configs> event);

    public String getSaida();
    
    default public String cpf11(String cpf){
        return "0".repeat(11 - cpf.length()) + cpf;
    } 

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
