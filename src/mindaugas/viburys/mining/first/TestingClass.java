package mindaugas.viburys.mining.first;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;

import java.io.*;

/**
 * Created by minda on 2016-02-20.
 */
public class TestingClass {



    public static void main(String args[]){

        try { chunk("Henry asks a girl out on a date.");  } catch (IOException e) {e.printStackTrace();}
        try { chunk("Henry asks a Hillary out on a date.");  } catch (IOException e) {e.printStackTrace();}

        if(false) {

            InputStream modelIn = null;
            ChunkerModel model = null;
            String[] tags = null;
            String whitespaceTokenizerLine[] = null;
            try {
                modelIn = new FileInputStream("en-chunker.bin");
                model = new ChunkerModel(modelIn);

                POSModel posModel = new POSModelLoader()
                        .load(new File("en-pos-maxent.bin"));

                POSTaggerME tagger = new POSTaggerME(posModel);

                ChunkerME chunker = new ChunkerME(model);


                String sent[] = new String[]{"Megan", "kills", "Rick", "."};

                String pos[] = new String[]{"PRP", "VB", "AB", "."};

                whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                        .tokenize("");
                tags = tagger.tag(whitespaceTokenizerLine);

                Sequence topSequences[] = chunker.topKSequences(sent, pos);
                System.out.println(topSequences.length);

            } catch (IOException e) {
                // Model loading failed, handle the error
                e.printStackTrace();
            } finally {
                if (modelIn != null) {
                    try {
                        modelIn.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

    }


    public static void chunk(String input) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        String whitespaceTokenizerLine[] = null;

        String[] tags = null;
        while ((line = lineStream.read()) != null) {
            whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();

        // chunker
        InputStream is = new FileInputStream("en-chunker.bin");
        ChunkerModel cModel = new ChunkerModel(is);

        ChunkerME chunkerME = new ChunkerME(cModel);
        String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);

        for (String s : result)
            System.out.println(s);

        Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
        for (Span s : span)
            System.out.println(s.toString());
    }


}
