package model;

import java.util.List;

public interface QuestionBackupIO {
    void exportQuestion(List<Question> questions, String fileName) throws QuestionBackupIOException;
    List<Question> importQuestion(String fileName) throws QuestionBackupIOException;
    String getBackupIODescription() throws QuestionBackupIOException;
}
