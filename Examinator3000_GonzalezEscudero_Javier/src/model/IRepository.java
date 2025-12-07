package model;

public class IRepository {
    Question addQuestion(Question q) throws RepositoryException;
    void removeQuestion(Question q) throws RepositoryException;
    Question modifyQuestion(Question q) throws RepositoryException;
    List<Question> getAllQuestions() throws RepositoryException;
}
