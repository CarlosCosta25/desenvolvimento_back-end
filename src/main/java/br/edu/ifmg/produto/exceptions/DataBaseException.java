package br.edu.ifmg.produto.exceptions;

public class DataBaseException extends RuntimeException{

    public DataBaseException(String msg){
        super(msg);
    }

    public DataBaseException(){
        super();
    }
}
