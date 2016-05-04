package mindaugas.viburys.mining.first;

import mindaugas.viburys.mining.first.analyzers.*;
import mindaugas.viburys.mining.first.models.ModelAction;
import mindaugas.viburys.mining.first.models.ModelCountable;
import mindaugas.viburys.mining.first.models.ModelMovie;
import mindaugas.viburys.mining.first.models.ModelRelation;
import mindaugas.viburys.mining.first.reader.PlotsDataReader;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by minda on 2016-04-21.
 */
public class MovieAnalyzer {

    private static final int LOAD_COUNT = 50;


    private List<ModelCountable> mLocationsList = new ArrayList<>();
    private List<ModelCountable> mNamesList = new ArrayList<>();
    private List<ModelAction> mActions = new ArrayList<>();
    private List<ModelRelation> mRelations = new ArrayList<>();


    private TokenSpliter mTokenSpliter;
    private SentenceSpliter mSentenceSpliter;
    private LocationsFinder mLocationsFinder;
    private PersonsFinder mPersonsFinder;
    private SentenceParser mSentenceParser;
    private SentencePostTagger mSentencePostTagger;

    public MovieAnalyzer() {
        mTokenSpliter = new TokenSpliter();
        mSentenceSpliter = new SentenceSpliter();
        mLocationsFinder = new LocationsFinder();
        mPersonsFinder = new PersonsFinder();
        mSentenceParser = new SentenceParser();
        mSentencePostTagger = new SentencePostTagger();
    }

    public void start(){

        PlotsDataReader reader = new PlotsDataReader("../plot.list");
        reader.readMovies(LOAD_COUNT);
        List<ModelMovie> movies = reader.getMoviesList();
        System.out.println("Loaded " + LOAD_COUNT + " Movies");

        for(int i = 0; i < movies.size(); i++){
            ModelMovie movie = movies.get(i);
            System.out.println("ANALYZING: "+i +"/"+ LOAD_COUNT);
            analyzeMovie(movie);
        }

        System.out.println("PRINTING: Locations list");
        Collections.sort(mLocationsList, ModelCountable.AmountComparator);
        for(int i = 0; i < mLocationsList.size() && i < 10; i++){
            System.out.println(mLocationsList.get(i).getString());
        }

        System.out.println("PRINTING: Names list");
        Collections.sort(mNamesList, ModelCountable.AmountComparator);
        for(int i = 0; i < mNamesList.size() && i < 10; i++){
            System.out.println(mNamesList.get(i).getString());
        }

        System.out.println("PRINTING: Actions");
        Collections.sort(mActions, ModelAction.AmountComparator);
        for(int i = 0; i < mActions.size() && i < 10; i++){
            System.out.println(mActions.get(i).getString());
        }


        System.out.println("PRINTING: Relations");
        Collections.sort(mRelations, ModelRelation.AmountComparator);
        for(int i = 0; i < mRelations.size() && i < 10; i++){
            System.out.println(mRelations.get(i).getString());
        }
    }


    public void analyzeMovie(ModelMovie movie){

        for(int i = 0; i < movie.getPlots().size(); i++){

            String[] sentences = mSentenceSpliter.splitToSentences(movie.getPlots().get(i));

            for(String sentence : sentences){
                analyzeSentence(sentence);
            }
        }
    }


    public void analyzeSentence(String sentence){
        String[] tokens =   mTokenSpliter.tokenize(sentence);
        String[] names = mPersonsFinder.findnames(tokens);
        String[] locations = mLocationsFinder.findLoactions(tokens);

        //  String[] names = new String[]{};
        //   String[] locations = new String[]{};

        Parse[] parses = mSentenceParser.parseSentence(sentence);

        //Sequence[] seqs = mSentencePostTagger.postTag(tokens);

        getRelevants(parses, names);


        for(String name : names){
            int index = findElement(mNamesList, name);
            if(index != -1){
                mNamesList.get(index).setCount( mNamesList.get(index).getCount()+1);
            } else {
                mNamesList.add(new ModelCountable(name,1));
            }
        }


        for(String location : locations){
            int index = findElement(mLocationsList, location);
            if(index != -1){
                mLocationsList.get(index).setCount( mLocationsList.get(index).getCount()+1);
            } else {
                mLocationsList.add(new ModelCountable(location,1));
            }
        }

    }


    public int findElement(List<ModelCountable> list, String key){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(key)){
                return i;
            }
        }
        return -1;
    }


    public int findElement(List<ModelAction> list, String person, String action ){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getPerson().equalsIgnoreCase(person) && list.get(i).getAction().equalsIgnoreCase(action)){
                return i;
            }
        }
        return -1;
    }




    public  void getRelevants(Parse [] sequences, String[] names){

        for(int i = 0; i < sequences.length; i++){
            // sequences[i].show();

            String firstName = null;
            String lastName = null;
            String actioncc = null;

            sequences[i].show();

            for(Parse chil : sequences[i].getChildren()){

                if(chil.getType().equalsIgnoreCase("S")) {
                    getRelevants(new Parse[]{chil}, names);
                } else if(chil.getType().equalsIgnoreCase("NP")){
                    Parse name = getNP(chil);
                    if(name != null) {
                        firstName =name.toString();
                    } else
                        return;

                } else if(chil.getType().equalsIgnoreCase("VP")){
                    Parse name = getNP(chil);
                    Parse action = getVP(chil);

                    if(name != null &&  action != null) {
                        actioncc = action.toString();
                        lastName = name.toString();
                    } else {
                        return;
                    }
                }

            }

            if(actioncc != null &&( actioncc.equalsIgnoreCase("is")
                    || actioncc.equalsIgnoreCase("are")
                    || actioncc.equalsIgnoreCase("who'd")
                    || actioncc.equalsIgnoreCase("who'd"))){
                actioncc = null;
            }

            if(firstName != null) {
                firstName = firstName.replaceAll("[^A-Za-z0-9 ]", "");
            }


            if (firstName != null && actioncc != null){
                int index = findElement(mActions, firstName, actioncc);
                if (index != -1) {
                    mActions.get(index).setCount(mActions.get(index).getCount() + 1);
                } else {
                    mActions.add(new ModelAction(firstName, actioncc, 1));
                }
            }


            if(firstName != null && lastName != null && actioncc != null){

                boolean exists =false;
                for(String aa : names){
                    if (firstName.contains(aa)){
                        exists =true;
                        break;
                    }
                }

                if(!exists) {
                    firstName = null;
                }


                exists =false;
                for(String aa : names){
                    if (lastName.contains(aa)){
                        exists =true;
                        break;
                    }
                }

                if(!exists) {
                    lastName = null;
                }


                if(firstName != null && lastName != null &&  actioncc != null) {
                    mRelations.add(new ModelRelation(firstName, lastName, actioncc, 1));
                }

            }
        }
    }


    public  Parse getNP(Parse chil){

        if(chil.getType().equalsIgnoreCase("NNP")){
            return chil ;
        } else if(chil.getChildCount() > 0) {
            for(Parse child : chil.getChildren()){
                Parse res = getNP(child);
                if(res != null && res.getType().equalsIgnoreCase("NNP"))
                    return res;
            }
        }
        return null;
    }


    public  Parse getVP(Parse chil){

        if(chil.getType().startsWith("VB") && chil.getType().length() ==3){
            return chil ;
        } else if(chil.getChildCount() > 0) {
            for(Parse child : chil.getChildren()){
                Parse res = getVP(child);
                if(res != null && res.getType().startsWith("VB")  && res.getType().length() ==3)
                    return res;
                return  getVP(child);
            }
        }
        return null;
    }


}
