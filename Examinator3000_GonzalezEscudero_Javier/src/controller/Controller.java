package controller;

import model.Examen;
import model.Model;
import model.Question;
import model.QuestionBackupIOException;
import model.RepositoryException;
import view.BaseView;
import java.util.List;

public class Controller {
    private Model model;
    private BaseView view;

    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
    }

    public void start(){
        try {
            model.cargar();
            view.showMessage("Datos cargados correctamente");
        } catch (RepositoryException e) {
            view.showErrorMessage("Error en la carga de datos: " +e.getMessage());
        }

        view.init();

        end();
    }

    public void end(){
        try {
            model.guardar();
            view.showMessage("Datos guardados correctamente");
        } catch (RepositoryException e) {
            view.showErrorMessage("Error al guardar datos: " + e.getMessage());
        }
        view.end();
    }

    public Question createQuestion(Question q) throws RepositoryException{
        return model.addQuestion(q);
    }

    public void deleteQuestion(Question q) throws RepositoryException{
        model.removeQuestion(q);
    }

    public Question updateQuestion(Question q) throws RepositoryException{
        return model.modifyQuestion(q);
    }

    public List<Question> listAllQuestions() throws RepositoryException{
        return model.getAllQuestions();
    }

    public List<Question> listQuestionByTopic(String tema) throws RepositoryException{
        return model.getQuestionsByTopic(tema);
    }

    public void exportQuestion(String fileName) throws QuestionBackupIOException, RepositoryException{
        model.exportQuestion(fileName);
    }

    public void importQuestion(String fileName) throws QuestionBackupIOException, RepositoryException{
        model.importQuestion(fileName);
    }

    public List<String> getAllTemas() throws RepositoryException{
        return model.getAllTemas();
    }

    public Examen crearExamen(String tema, int n) throws RepositoryException{
        return model.crearExamen(tema, n);
    }
}
