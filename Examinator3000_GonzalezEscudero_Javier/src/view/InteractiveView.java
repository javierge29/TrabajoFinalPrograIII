package view;

import java.util.Scanner;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import controller.Controller;
import model.Examen;
import model.Option;
import model.Question;
import model.QuestionBackupIOException;
import model.RepositoryException;

public class InteractiveView extends BaseView{
    private Scanner sc=new Scanner(System.in);

    @Override
    public void init(){
        showMessage("Bienvenido a Examinator 3000");
        boolean ejec=true;
        while(ejec){
            showMessage("\nMenú principal:");
            showMessage("1. CRUD Preguntas");
            showMessage("2. Exportar/Importar archivo .JSON");
            showMessage("3. Modo examen");
            showMessage("0. Salir");
            
            int opc=leerInt("Elegir opción:");
            switch(opc){
                case 1:
                    menuCrud();
                    break;
                case 2:
                    menuBackup();
                    break;
                case 3:
                    modoExamen();
                    break;
                case 0:
                    ejec=false;
                    break;
                default:
                    showErrorMessage("Opcion no valida");
            }
        }
    }
     
    private void menuCrud(){
        boolean enCrud=true;
        while(enCrud){
            showMessage("\nMenú CRUD:");
            showMessage("1. Crear una nueva pregunta");
            showMessage("2. Listar todas (ordenadas por fecha)");
            showMessage("3. Listar por tema");
            showMessage("0. Volver");
            int opc=leerInt("Elige opción: ");
            switch (opc){
                case 1:
                    crearPregunta();
                    break;
                case 2:
                    listarTodas();
                    break;
                case 3:
                    listarPorTema();
                    break;
                case 0:
                    enCrud=false;
                    break;
                default:
                    showErrorMessage("Opción no valida");
            }
        }
    }

    private void menuBackup(){
        boolean enBackup=true;
        while(enBackup){
            showMessage("\nMenú Backup:");
            showMessage("1. Exportar a .JSON");
            showMessage("2. Importar de .JSON");
            showMessage("0. Volver");
            int opc=leerInt("Elige opción: ");
            switch (opc){
                case 1:
                    String exportarFile=leerString("Nombre del archivo (.json) que deseas exportar: ");
                    try {
                        controller.exportQuestion(exportarFile);
                        showMessage("Exportado con éxito a ~/" + exportarFile);
                    } catch (QuestionBackupIOException | RepositoryException e) {
                        showErrorMessage(e.getMessage());
                    }
                    break;
                case 2:
                    String importarFile=leerString("Nombre de archivo (.json) que deseas importar: ");
                    try {
                        controller.importQuestion(importarFile);
                        showMessage("Importado con éxito desde ~/" + importarFile);
                    } catch (QuestionBackupIOException | RepositoryException e) {
                        showErrorMessage(e.getMessage());
                    }
                    break;
                case 0:
                    enBackup=false;
                    break;
                default:
                    showErrorMessage("Opción no valida");
            }
        }
    }

    private void modoExamen(){
        try {
            List<String> temas=controller.getAllTemas();
            showMessage("Temas disponibles:");
            for(int i=0;i<temas.size();i++){
                showMessage((i+1) + ". " + temas.get(i));
            }
            showMessage((temas.size() + 1) + ". Todos");
            int indice=leerInt("Elegir tema: ") - 1;
            String tema=(indice==temas.size()) ? "todos" : temas.get(indice);
            List<Question> disponibles=(temas.equals("todos")) ? controller.listAllQuestions() : controller.listQuestionByTopic(tema);
            int maxN=disponibles.size();
            int n=leerInt("Número de preguntas (1-" + maxN + "): ");
            Examen examen=controller.crearExamen(tema, n);

            for(int i=0;i<examen.getPreguntas().size();i++){
                Question q=examen.getPreguntas().get(i);
                showMessage("Pregunta " + ((i+1) + ": " + q.getEnunciado()));
                List<Option> opciones=q.getOpcion();
                for(int j=0;j<opciones.size();j++){
                    showMessage((j+1) + ". " + opciones.get(j).getTexto());
                }
                int resp=leerInt("Respuesta (1-4, 0 para saltar): ");
                if(resp!=0){
                    examen.responder(i, resp-1);
                    showMessage(examen.getFeedback(i));
                }
            }

            examen.finalizar();
            showMessage("Resultado:");
            showMessage("Nota: " + examen.calcularNota());
            showMessage("Acertadas: " + examen.getAciertos());
            showMessage("Falladas: " + examen.getFallos());
            showMessage("No contestadas: " + examen.getEnBlanco());
            showMessage("Tiempo: " + examen.getTiempo() + " segundos");
        } catch (RepositoryException | IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }
    
    private void crearPregunta(){
        try {
            String autor=leerString("Autor: ");
            HashSet<String> temas=new HashSet<>();
            temas.add(leerString("Tema: "));
            String enunciado=leerString("Enunciado: ");
            List<Option> opciones=new ArrayList<>();
            for(int i=1;i<=4;i++){
                String texto=leerString("Opción: " + i + "texto: ");
                String justificacion=leerString("Justificación " + i + ": ");
                boolean esCorrecta=leerBooleano("Es correcta? (true/false): ");
                opciones.add(new Option(texto, justificacion, esCorrecta));
            }
            Question pregunta=new Question(autor, temas, enunciado, opciones);
            controller.createQuestion(pregunta);
            showMessage("Pregunta creada: " + pregunta) ;
        } catch (RepositoryException | IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void listarTodas(){
        try {
            List<Question> preguntas=controller.listAllQuestions();
            if(preguntas.isEmpty()){
                showMessage("No hay preguntas");
                return;
            }
            showMessage("Listado completo:");
            for(int i=0;i<preguntas.size();i++)
                showMessage((i+1) + ". " + preguntas.get(i));
            int indice=leerInt("Elegir número para detalle (0 para volver): ")-1;
            if(indice<=0 && indice<preguntas.size())
                mostrarDetalle(preguntas.get(indice));
        } catch (RepositoryException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void listarPorTema(){
        String tema=leerString("Tema a filtrar: ");
        try {
            List<Question> preguntas=controller.listQuestionByTopic(tema);
            if(preguntas.isEmpty()){
                showMessage("No hay preguntas para " + tema);
                return;
            }
            showMessage("Listado por tema " + tema + ":");
            for(int i=0;i<preguntas.size();i++)
                showMessage((i+1) + ". " + preguntas.get(i));
            int indice=leerInt("Elige número para detalle (0 para volver): ")-1;
            if(indice>=0 && indice<preguntas.size())
                mostrarDetalle(preguntas.get(indice));
        } catch (RepositoryException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void mostrarDetalle(Question pregunta){
        showMessage("\nDetalle de pregunta:");
        showMessage(pregunta.toString());
        showMessage("Opciones:");
        for(Option opcion : pregunta.getOpcion())
            showMessage("- " + opcion);
        showMessage("1. Modificar");
        showMessage("2. Eliminar");
        showMessage("0. Volver");
        int opc=leerInt("Elige: ");
        switch (opc) {
            case 1:
                modificarPregunta(pregunta);
                break;
            case 2:
                eliminarPregunta(pregunta);
                break;
        }
    }

    private void modificarPregunta(Question pregunta){
        try {
            pregunta.setAutor(leerString("Nuevo autor (" + pregunta.getAutor() + "): "));
            HashSet<String> nuevosTemas=new HashSet<>();
            nuevosTemas.add(leerString("Nuevo tema: "));
            pregunta.setTemas(nuevosTemas);
            pregunta.setEnunciado(leerString("Nuevo enunciado (" + pregunta.getEnunciado() + "): "));
            List<Option> nuevasOpciones=new ArrayList<>();
            for(int i=1;i<=4;i++){
                String texto=leerString("Nueva opción " + i + "texto: ");
                String justificacion=leerString("Justifiación " + i + ": ");
                boolean esCorrecta=leerBooleano("Correcto? (true/false): ");
                nuevasOpciones.add(new Option(texto, justificacion, esCorrecta));
            }
            pregunta.setOpcion(nuevasOpciones);
            controller.updateQuestion(pregunta);
            showMessage("Pregunta modificada");
        } catch (RepositoryException | IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void eliminarPregunta(Question pregunta){
        if(leerBooleano("Confirmar eliminar pregunta? (true/false): ")){
            try {
                controller.deleteQuestion(pregunta);
                showMessage("Pregunta eliminada");
            } catch (RepositoryException e) {
                showErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void showMessage(String msg){
        System.out.println(msg);
    }

    @Override
    public void showErrorMessage(String msg){
        System.err.println("ERROR: " + msg);
    }

    @Override
    public void end(){
        showMessage("Cerrnado programa...");
        sc.close();
    }

    private String leerString(String msg){
        showMessage(msg);
        return sc.nextLine();
    }

    private int leerInt(String msg){
        showMessage(msg);
        while(!sc.hasNextInt()){
            showErrorMessage("Entrada invalida");
            sc.next();
            showMessage(msg);
        }
        int valor=sc.nextInt();
        sc.nextLine();
        return valor;
    }

    private boolean leerBooleano(String msg){
        showMessage(msg);
        while(!sc.hasNextBoolean()){
            showErrorMessage("Entrada invalida (true/false)");
            sc.next();
            showMessage(msg);
        }
        boolean valor=sc.nextBoolean();
        sc.nextLine();
        return valor;
    }
}
