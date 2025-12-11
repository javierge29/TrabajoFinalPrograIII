package model;

import com.google.gson.Gson;
import es.usal.genai.GenAiConfig;
import es.usal.genai.GenAiFacade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GeminiQuestionCreator implements QuestionCreator{
    private String apiKey;
    private String id="gemini-2.5-flash";

    public GeminiQuestionCreator(String apiKey){
        this.apiKey=apiKey;
    }

    @Override
    public Question crearQuestion(String tema) throws QuestionCreatorException{
        try {
            System.setProperty("GENAI_API_KEY", apiKey);
            GenAiConfig config=GenAiConfig.fromEnv(id);
            try(GenAiFacade genai=new GenAiFacade(config)){
                String prompt = "Genera una pregunta tipo test sobre '" + tema + "' en JSON: " +
                                "{ \"author\": \"Gemini AI\", \"topics\": [\""+ tema.toUpperCase() +"\"], " +
                                "\"statement\": \"Enunciado\", \"options\": [{\"text\": \"Op1\", \"rationale\": \"Raz1\", \"correct\": true}, ...] }." +
                                " 4 opciones, solo una correcta.";
                
                String json=genai.generateText(prompt);

                Gson gson=new Gson();
                QuestionDTO dto=gson.fromJson(json, QuestionDTO.class);

                HashSet<String> temas=new HashSet<>(dto.temas);
                List<Option> opciones=new ArrayList<>();
                for(OptionDTO opc : dto.opciones){
                    opciones.add(new Option(opc.texto, opc.justificacion, opc.esCorrecta));
                }
                return new Question(dto.autor, temas, dto.enunciado, opciones);
            }
        } catch (Exception e) {
            throw new QuestionCreatorException("Error con Gemini", e);
        } finally {
            System.clearProperty("GENAI_API_KEY");
        }
    }

    @Override
    public String getQuestionCreatorDesc(){
        return "Gemini AI";
    }   
}

class QuestionDTO{
    String autor;
    List<String> temas;
    String enunciado;
    List<OptionDTO> opciones;
}

class OptionDTO{
    String texto;
    String justificacion;
    boolean esCorrecta;
}
