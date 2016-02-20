package mindaugas.viburys.mining.first;

import mindaugas.viburys.mining.first.models.ModelMovie;
import mindaugas.viburys.mining.first.models.ModelMoviePlot;
import mindaugas.viburys.mining.first.reader.PlotsDataReader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by minda on 2016-02-18.
 */
public class StartingClass  extends JPanel {



    public static void main(String args[]){

        PlotsDataReader reader = new PlotsDataReader("../plot.list");
        reader.readMovies(Integer.MAX_VALUE);


        InputStream sentenceInStream = null;
        InputStream tokenizerInStream = null;

        List<ModelMovie> movies = reader.getMoviesList();

        try {
             sentenceInStream = new FileInputStream("en-sent.bin");
             tokenizerInStream = new FileInputStream("en-token.bin");


            SentenceModel model = new SentenceModel(sentenceInStream);
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerInStream);

            Tokenizer tokenizer = new TokenizerME(tokenizerModel);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

            for(int i = 0 ;i < movies.size(); i++){
                for(int g = 0 ; g < movies.get(i).getPlots().size();g++)
                {
                    ModelMoviePlot plot = movies.get(i).getPlots().get(g);

                    String sentences[] = sentenceDetector.sentDetect(plot.getPlot());
                    for(int ii = 0; ii < sentences.length; ii++){
                        workWithSentence(tokenizer,sentences[ii]);
                    }

                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (sentenceInStream != null) {  try { sentenceInStream.close(); }   catch (IOException e) {}}
            if (tokenizerInStream != null) {  try { tokenizerInStream.close(); }   catch (IOException e) {}}
        }
    }


    public static void workWithSentence( Tokenizer tokenizer,String sentence){

        String tokens[] = tokenizer.tokenize(sentence);


    }

}
