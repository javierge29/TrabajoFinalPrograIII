package model;

public class RepositoryException extends Exception{
    public RepositoryException(String msg, Throwable cause){
        super(msg,cause);
    }

    public RepositoryException(String msg){
        super(msg);
    }
}
