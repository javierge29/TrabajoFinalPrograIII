package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private IRepository repositorio;
    private QuestionBackupIO backup;
    private List<QuestionCreator> questionCreators;
    
    public Model(IRepository repositorio, QuestionBackupIO backup, List<QuestionCreator> questionCreators) {
        this.repositorio = repositorio;
        this.backup = backup;
        this.questionCreators = questionCreators;
    }

    public void cargar() throws RepositoryException{
        if(repositorio instanceof BinaryRepository){
            ((BinaryRepository) repositorio).cargar();
        }
    }

    public void guardar() throws RepositoryException{
        if(repositorio instanceof BinaryRepository){
            ((BinaryRepository) repositorio).guardar();
        }
    }

    public Question addQuestion(Question q) throws RepositoryException{
        Question agregada=repositorio.addQuestion(q);
        guardar();
        return agregada;
    }

    public void removeQuestion(Question q) throws RepositoryException{
        repositorio.removeQuestion(q);
        guardar();
    }

    public void modifyQuestion(Question q) throws RepositoryException{
        Question modificada=repositorio.modifyQuestion(q);
        guardar();
        return modificada;
    }

    public List<Question> getAllQuestions() throws RepositoryException{
        List<Question> todas=repositorio.getAllQuestions();
        todas.sort((q1, q2) -> q2.getCreationDate().compareTo(q1.getCreationDate()));
        return todas;
    }

    public List<Question> getQuestionsByTopic(String tema) throws RepositoryException{
        String temaNormalizado=tema.toUpperCase();
        List<Question> filtradas=new ArrayList<>();
        for(Question q : getAllQuestions()){
            if(q.getTemas().contains(temaNormalizado));
                filtradas.add(q);
        }
        return filtradas;
    }
}
