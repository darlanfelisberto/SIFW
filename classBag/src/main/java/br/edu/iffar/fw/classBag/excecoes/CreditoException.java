package br.edu.iffar.fw.classBag.excecoes;

public class CreditoException extends RuntimeException{

    public CreditoException(){
        super("Usuário invalido.");
    }
    public CreditoException(String msg){
        super(msg);
    }
}
