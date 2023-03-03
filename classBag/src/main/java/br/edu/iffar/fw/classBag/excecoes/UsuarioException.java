package br.edu.iffar.fw.classBag.excecoes;

public class UsuarioException extends RuntimeException{

    public UsuarioException(){
        super("Usuário invalido.");
    }
    public UsuarioException(String msg){
        super(msg);
    }
}
