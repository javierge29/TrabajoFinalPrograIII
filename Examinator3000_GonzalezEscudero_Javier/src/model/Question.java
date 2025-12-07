package model;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;

public class Question {
    private UUID id=UUID.randomUUID();
    private String autor;
    private HashSet<String> temas=new HashSet<>();
    private String enunciado;
    private List<Option> opcion=new ArrayList<>();
    private Date creationDate=new Date();
    
    public Question(String autor, HashSet<String> temas, String enunciado, List<Option> opcion) {
        this.autor = autor;
        this.temas = temas;
        this.enunciado = enunciado;
        this.opcion = opcion;
    }

    //Getters y setters
    public UUID getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public HashSet<String> getTemas() {
        return temas;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<Option> getOpcion() {
        return opcion;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setTemas(HashSet<String> temas) {
        this.temas = temas;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setOpcion(List<Option> opcion) {
        this.opcion = opcion;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    @Override
    public String toString(){
        return "ID: " + id + " | Autor: " + autor + " | Temas: " + temas + " | Enunciado: " + enunciado;
    }
}
