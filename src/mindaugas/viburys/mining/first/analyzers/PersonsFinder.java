package mindaugas.viburys.mining.first.analyzers;

import mindaugas.viburys.mining.first.models.ModelMoviePlot;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minda on 2016-04-21.
 */
public class PersonsFinder {

    private NameFinderME nameFinder;

    public PersonsFinder() {
        InputStream modelIn = null;
        try {
            modelIn = new FileInputStream("en-ner-person.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            nameFinder = new NameFinderME(model);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }

    }

    public String [] findnames(String [] tokens){

        Span[] names = nameFinder.find(tokens);

        if(names != null && names.length > 0) {
            return Span.spansToStrings(names, tokens);
        }
        nameFinder.clearAdaptiveData();

        return new String[]{};
    }





}
