

import controller.Controller;
import model.BinaryRepository;
import model.GeminiQuestionCreator;
import model.IRepository;
import model.JSONQuestionBackupIO;
import model.Model;
import model.QuestionBackupIO;
import model.QuestionCreator;
import view.BaseView;
import view.InteractiveView;

import java.util.List;
import java.util.ArrayList;


public class App {
    public static void main(String[] args) throws Exception {
        IRepository repository=new BinaryRepository();
        QuestionBackupIO backup=new JSONQuestionBackupIO();
        List<QuestionCreator> questionCreators=new ArrayList<>();

        for(int i=0;i<args.length;i++){
            if(args[i].equals("-question-creator") && i+2<args.length){
                String modelId=args[i+1];
                String apiKey=args[i+2];
                if(modelId.startsWith("gemini")){
                    questionCreators.add(new GeminiQuestionCreator(apiKey));
                }
                i+=2;
            }
        }

        Model model=new Model(repository, backup, questionCreators);

        BaseView view=new InteractiveView();

        Controller controller=new Controller(model, view);
        view.setController(controller);

        controller.start();
    }
}
