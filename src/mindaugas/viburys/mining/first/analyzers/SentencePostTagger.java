package mindaugas.viburys.mining.first.analyzers;



import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minda on 2016-04-24.
 */
public class SentencePostTagger {




    POSTaggerME tagger;

    public SentencePostTagger() {
        InputStream modelIn = null;

        try {
            modelIn = new FileInputStream("en-pos-maxent.bin");
            POSModel model= new POSModel(modelIn);
            tagger = new POSTaggerME(model);
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

    public Sequence[] postTag(String [] tokens){
        Sequence topSequences[] = tagger.topKSequences(tokens);
        return topSequences;
    }


    public String[] postTagStrings(String [] tokens){
        String topSequences[] = tagger.tag(tokens);
        return topSequences;
    }

}
