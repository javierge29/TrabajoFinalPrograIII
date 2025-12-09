package model;

import java.util.ArrayList;
import java.util.HashSet;
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

    public Question modifyQuestion(Question q) throws RepositoryException{
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

    public void exportQuestion(String fileName) throws QuestionBackupIOException, RepositoryException{
        List<Question> all=getAllQuestions();
        backup.exportQuestion(all, fileName);
    }

    public void importQuestion(String fileName) throws QuestionBackupIOException, RepositoryException{
        List<Question> importado=backup.importQuestion(fileName);
        for(Question q : importado){
            boolean existe=false;
            for(Question exists : getAllQuestions()){
                if(exists.getId().equals(q.getId())){
                    existe=true;
                    break;
                }
            }
            if(!existe){
                addQuestion(q);
            }
        }
    }
    
    public List<String> getAllTemas() throws RepositoryException{
        HashSet<String> temas=new HashSet<>();
        for(Question q : getAllQuestions()){
            temas.addAll(q.getTemas());
        }

        return new ArrayList<>(temas);
    }

    public Examen crearExamen(String tema, int n) throws RepositoryException{
        List<Question> disponibles;
        if("todos".equalsIgnoreCase(tema)){
            disponibles=getAllQuestions();
        }else{
            disponibles=getQuestionsByTopic(tema);
        }
        if(n<1 || n>disponibles.size()){
            throw new IllegalArgumentException("N inválido");
        }
        List<Question> seleccionadas=Examen.seleccionarPreguntas(disponibles, n);
        return new Examen(tema, seleccionadas);
    }
}
