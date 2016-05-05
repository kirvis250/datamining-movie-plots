package mindaugas.viburys.mining.first;



/**
 * Created by minda on 2016-02-20.
 */
public class StartAnalyserCustomText {


    public static void main(String args[]){

        MovieAnalyzer analyzer = new MovieAnalyzer(0);
        analyzer.analyzeSentence("David meets Henry and Peter goes fishing");
        analyzer.printResults();
        analyzer.reset();


    }





}
