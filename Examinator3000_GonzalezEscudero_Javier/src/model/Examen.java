package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;


public class Examen {
    private String tema;
    private List<Question> preguntas;
    private List<Integer> respuestas;
    private long tiempoInicio;
    private long tiempoFin;
   
    public Examen(String tema, List<Question> preguntasSeleccionadas) {
        this.tema = tema;
        this.preguntas = preguntasSeleccionadas;
        this.respuestas = new ArrayList<>();
        for(int i=0;i<preguntas.size();i++){
            respuestas.add(-1);
        }
        this.tiempoInicio = new Date().getTime();
    }
    

    public void responder(int indice,int opcion){
        respuestas.set(indice, opcion);
    }

    public void finalizar(){
        tiempoFin=new Date().getTime();
    }
    
    public double calcularNota(){
        int aciertos=0;
        int fallos=0;
        int enBlanco=0;
        for(int i=0;i<preguntas.size();i++){
            int resp=respuestas.get(i);
            if(resp==-1){
                enBlanco++;
            }else{
                if(preguntas.get(i).getOpcion().get(resp).isEsCorrecta()){
                    aciertos++;
                }else{
                    fallos++;
                }
            }
        }
        double valorPregunta=10.0 / preguntas.size();
        double nota=(aciertos*valorPregunta)-(fallos*(valorPregunta/3));
        return Math.max(0, nota);
    }
    
    public int getAciertos(){
        int cont=0;
        for(int i=0;i<respuestas.size();i++){
            int resp=respuestas.get(i);
            if(resp!=-1 && !preguntas.get(i).getOpcion().get(resp).isEsCorrecta()){
                cont++;
            }
        }
        return cont;
    }

    public int getFallos(){
        int cont=0;
        for(int i=0;i<respuestas.size();i++){
            int resp=respuestas.get(i);
            if(resp!=-1 && !preguntas.get(i).getOpcion().get(resp).isEsCorrecta()){
                cont++;
            }
        }

        return cont;
    }

    public int getEnBlanco(){
        int cont=0;
        for(int resp : respuestas){
            if(resp==-1)
                cont++;
        }

        return cont;
    }

    public long getTiempo(){
        return (tiempoFin-tiempoInicio)/1000;
    }

    public String getFeedback(int indice){
        int resp=respuestas.get(indice);
        if(resp==-1)
            return "No contestada";
        Option opc=preguntas.get(indice).getOpcion().get(resp);
        return opc.isEsCorrecta() ? "Correcta: " + opc.getJustificacion() : "Incorrecta: " + opc.getJustificacion();
    }

    public List<Question> getPreguntas(){
        return preguntas;
    }

    public static List<Question> seleccionarPreguntas(List<Question> disponibles, int n){
        List<Question> seleccionadas=new ArrayList<>(disponibles);
        Random random=new Random();
        while (seleccionadas.size()>n) {
            seleccionadas.remove(random.nextInt(seleccionadas.size()));
        }

        return seleccionadas;
    }
}
