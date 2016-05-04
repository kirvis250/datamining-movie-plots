package mindaugas.viburys.mining.first.analyzers;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minda on 2016-04-21.
 */
public class SentenceParser {

    Parser parser;

    public SentenceParser() {
        InputStream modelIn = null;

        try {
            modelIn = new FileInputStream("en-parser-chunking.bin");
            ParserModel   model= new ParserModel(modelIn);
            parser = ParserFactory.create(model);
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

    public Parse[] parseSentence(String sentence){
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

        return topParses;
    }

}
