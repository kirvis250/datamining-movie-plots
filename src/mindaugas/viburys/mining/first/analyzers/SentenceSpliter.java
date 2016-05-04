package mindaugas.viburys.mining.first.analyzers;

import mindaugas.viburys.mining.first.models.ModelMoviePlot;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minda on 2016-04-21.
 */
public class SentenceSpliter {

    SentenceDetectorME sentenceDetector;


    public SentenceSpliter() {
        InputStream modelIn =null;
        try {
            modelIn = new FileInputStream("en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(model);

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

    public String[] splitToSentences(ModelMoviePlot plot){
        return sentenceDetector.sentDetect(plot.getPlot());
    }


}
