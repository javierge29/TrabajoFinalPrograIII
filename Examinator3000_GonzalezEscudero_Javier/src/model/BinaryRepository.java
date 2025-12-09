package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BinaryRepository implements IRepository{
    private List<Question> preguntas=new ArrayList<>();
    private static final String RUTA_ARCHIVO=System.getProperty("user.home")+"/question.bin";

    public void cargar() throws RepositoryException{
        File archivo=new File(RUTA_ARCHIVO);
        if (archivo.exists()){
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(archivo))){
                @SuppressWarnings("unchecked")
                List<Question> preguntasCargadas=(ArrayList<Question>) ois.readObject();
                preguntas=preguntasCargadas;
            }catch(IOException | ClassNotFoundException e){
                throw new RepositoryException("Error al intentar cargar preguntas binarias", e);
            }
        }
    }

    public void guardar() throws RepositoryException{
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))){
            oos.writeObject(preguntas);
        }catch (IOException e){
            throw new RepositoryException("Error al intentar guardar preguntas binarias", e);
        }
    }

    @Override
    public Question addQuestion(Question q) throws RepositoryException{
        preguntas.add(q);
        return q;
    }

    @Override
    public void removeQuestion(Question q) throws RepositoryException{
        if(!preguntas.remove(q)){
            throw new RepositoryException("Pregunta no encontrada para eliminar");
        }
    }

    @Override
    public Question modifyQuestion(Question q) throws RepositoryException{
        for(int i=0; i<preguntas.size();i++){
            if(preguntas.get(i).getId().equals(q.getId())){
                preguntas.set(i, q);
                return q;
            }   
        }
        throw new RepositoryException("Pregunta no encontrada para modificar");
    }

    @Override
    public List<Question> getAllQuestions() throws RepositoryException{
        return new ArrayList<>(preguntas);
    }
}
