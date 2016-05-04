package mindaugas.viburys.mining.first.analyzers;

import mindaugas.viburys.mining.first.models.ModelMoviePlot;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minda on 2016-04-21.
 */
public class TokenSpliter {

    Tokenizer tokenizer;


    public TokenSpliter() {
        InputStream modelIn = null;
        try {
            modelIn = new FileInputStream("en-token.bin");
            TokenizerModel model = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(model);
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

    public String [] tokenize(String sentence ){
        return tokenizer.tokenize(sentence);
    }

}
