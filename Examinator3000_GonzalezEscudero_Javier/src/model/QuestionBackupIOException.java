package model;

public class QuestionBackupIOException extends Exception{
    public QuestionBackupIOException(String msg, Throwable causa){
        super(msg, causa);
    }

    public QuestionBackupIOException(String msg){
        super(msg);
    }
}
