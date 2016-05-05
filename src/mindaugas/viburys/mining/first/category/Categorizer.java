package mindaugas.viburys.mining.first.category;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * Created by minda on 2016-05-04.
 */
public class Categorizer {


    private InputStream inputStream;
    private DoccatModel docCatModel;
    private DocumentCategorizerME myCategorizer;


    public Categorizer(String modelFile) {
        initModel(modelFile);
    }

    private void initModel(String modelFile) {
        try {
            inputStream = new FileInputStream(modelFile);
            docCatModel = new DoccatModel(inputStream);
            myCategorizer = new DocumentCategorizerME(docCatModel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public ModelPlotCategory getCategory(String text) {
        double[] outcomes = myCategorizer.categorize(text);
        String category = myCategorizer.getBestCategory(outcomes);

        return new ModelPlotCategory(category, getMax(outcomes), stripLine(text)) ;
    }


    public double getMax(double[] outcomes){
        double max = 0;
        for(int i = 0; i < outcomes.length; i++){
            if(outcomes[i] > max){
                max = outcomes[i];
            }
        }
        return max;
    }


    private String stripLine(String ss){
        int lenght = ss.length();

        if(lenght > 200){
            return ss.substring(0,200)+"...";
        } else {
            return ss;
        }

    }




}
