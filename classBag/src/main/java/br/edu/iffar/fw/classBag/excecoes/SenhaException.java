package br.edu.iffar.fw.classBag.excecoes;

public class SenhaException extends UsuarioException{

    public SenhaException(){
        super("Senha invalida");
    }
    public SenhaException(String msg){
        super(msg);
    }
}
