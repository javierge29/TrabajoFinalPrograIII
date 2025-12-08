package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class JSONQuestionBackupIO implements QuestionBackupIO{
    private static final String BASE_PATH=System.getProperty("user.home") + "/";

    @Override
    public void exportQuestion(List<Question> questions, String fileName) throws QuestionBackupIOException{
        String path=BASE_PATH + fileName;
        try(FileWriter escritor=new FileWriter(path)){
            Gson gson=new Gson();
            gson.toJson(questions, escritor);
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error al exportar a archivo .JSON: " + path, e);
        }
    }

    @Override
    public List<Question> importQuestion(String fileName) throws QuestionBackupIOException{
        String path=BASE_PATH + fileName;
        try(FileReader lector=new FileReader(path)){
            Gson gson=new Gson();
            Type listarType=new TypeToken<ArrayList<Question>>(){}.getType();
            return gson.fromJson(lector, listarType);
        } catch (IOException e) {
            throw new QuestionBackupIOException("Error al importar archivo .JSON: " + path, e);
        }
    }

    @Override
    public String getBackupIODescription() throws QuestionBackupIOException{
        return "Backup de archivo .JSON";
    }
    
}
