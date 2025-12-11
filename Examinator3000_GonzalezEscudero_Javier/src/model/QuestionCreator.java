package model;

public interface QuestionCreator {
    Question crearQuestion(String tema) throws QuestionCreatorException;
    String getQuestionCreatorDesc();
}
