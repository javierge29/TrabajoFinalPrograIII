package model;

import java.io.Serializable;

public class Option implements Serializable{
    private static final long serialVersionUID=1L;
    private String texto;
    private String justificacion;
    private boolean esCorrecta;
    
    public Option(String texto, String justificacion, boolean esCorrecta) {
        this.texto = texto;
        this.justificacion = justificacion;
        this.esCorrecta = esCorrecta;
    }

    //Getters y setters
    public String getTexto() {
        return texto;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    @Override
    public String toString(){
        return texto + " (" + (esCorrecta?"Correcta":"Incorrecta") + ": " + justificacion + ")";
    }
}
