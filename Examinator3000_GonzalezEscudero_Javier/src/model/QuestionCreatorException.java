package model;

public class QuestionCreatorException extends Exception{
    public QuestionCreatorException(String msg, Throwable cause){
        super(msg,cause);
    }

    public QuestionCreatorException(String msg){
        super(msg);
    }
}
