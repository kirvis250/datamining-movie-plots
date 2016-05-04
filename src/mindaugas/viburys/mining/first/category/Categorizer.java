package mindaugas.viburys.mining.first.category;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.FileInputStream;
import java.io.InputStream;
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


    public String getCategory(String text) {
        double[] outcomes = myCategorizer.categorize(text);
        String category = myCategorizer.getBestCategory(outcomes);
        return category;
    }

}
