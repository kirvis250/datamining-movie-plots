package mindaugas.viburys.mining.first;

import mindaugas.viburys.mining.first.analyzers.*;
import mindaugas.viburys.mining.first.models.ModelAction;
import mindaugas.viburys.mining.first.models.ModelCountable;
import mindaugas.viburys.mining.first.models.ModelMovie;
import mindaugas.viburys.mining.first.models.ModelRelation;
import mindaugas.viburys.mining.first.reader.PlotsDataReader;
import opennlp.tools.parser.Parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by minda on 2016-04-21.
 */
public class MovieAnalyzer {

    private  int mLoadCount = 100;


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

    public MovieAnalyzer(int count) {
        mTokenSpliter = new TokenSpliter();
        mSentenceSpliter = new SentenceSpliter();
        mLocationsFinder = new LocationsFinder();
        mPersonsFinder = new PersonsFinder();
        mSentenceParser = new SentenceParser();
        mSentencePostTagger = new SentencePostTagger();
        this.mLoadCount = count;
    }

    public void start() {

        PlotsDataReader reader = new PlotsDataReader("../plot.list");
        reader.readMovies(mLoadCount);
        List<ModelMovie> movies = reader.getMoviesList();
        System.out.println("Loaded " + mLoadCount + " Movies");

        for (int i = 0; i < movies.size(); i++) {
            ModelMovie movie = movies.get(i);
            System.out.println("ANALYZING: " + i + "/" + mLoadCount);
            analyzeMovie(movie);
        }
        printResults();
    }


    public void reset(){
        mLocationsList = new ArrayList<>();
        mNamesList = new ArrayList<>();
        mActions = new ArrayList<>();
        mRelations = new ArrayList<>();
    }



    public void printResults(){

        System.out.println("10 most common locations");
        Collections.sort(mLocationsList, ModelCountable.AmountComparator);
        for(int i = 0; i < mLocationsList.size() && i < 10; i++){
            System.out.println(" -"+mLocationsList.get(i).getString());
        }


        System.out.println("10 most common persons");
        Collections.sort(mNamesList, ModelCountable.AmountComparator);
        for(int i = 0; i < mNamesList.size() && i < 10; i++){

            String name = mNamesList.get(i).getName();

            System.out.println(" -"+name);

            System.out.println("   10 most related persons");
            int aa = 0;
            for(ModelCountable countable : get10MostRelatedPersons(name)){
                System.out.println("    -"+countable.getName()+ ", " +countable.getCount() );
                if(aa >=9){
                    break;
                }
                aa++;
            }

            aa = 0;
            System.out.println("   10 most common relations");
            for(ModelCountable countable : get10MostCommonRelations(name)){
                System.out.println("    -"+countable.getName()+ ", " +countable.getCount() );
                if(aa >=9){
                    break;
                }
                aa++;
            }

            aa = 0;
            System.out.println("   10 most common actions");
            for(ModelAction countable : get10MostCommonActions(name)){
                System.out.println("    -"+countable.getAction()+ ", " +countable.getCount() );
                if(aa >=9){
                    break;
                }
                aa++;
            }


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


    public List<ModelCountable> get10MostRelatedPersons(String name){
        List<ModelCountable> aa = new ArrayList<>();

        for(int i = 0; i < mRelations.size(); i++){
            if(mRelations.get(i).getPersonOne().equalsIgnoreCase(name)){
                String ndName = mRelations.get(i).getPersonTwo();
                int index = findElement(aa, ndName);
                if(index != -1){
                    aa.get(index).setCount( aa.get(index).getCount()+1);
                } else {
                    aa.add(new ModelCountable(ndName,1));
                }
            }
        }
        Collections.sort(aa,ModelCountable.AmountComparator);
        return aa;
    }



    public List<ModelCountable> get10MostCommonRelations(String name){
        List<ModelCountable> aa = new ArrayList<>();

        for(int i = 0; i < mRelations.size(); i++){
            if(mRelations.get(i).getPersonOne().equalsIgnoreCase(name)){
                String ndName = mRelations.get(i).getRelation();
                int index = findElement(aa, ndName);
                if(index != -1){
                    aa.get(index).setCount( aa.get(index).getCount()+1);
                } else {
                    aa.add(new ModelCountable(ndName,1));
                }
            }
        }
        Collections.sort(aa,ModelCountable.AmountComparator);
        return aa;
    }




    public List<ModelAction> get10MostCommonActions(String name){
        List<ModelAction> aa = new ArrayList<>();

        for(int i = 0; i < mActions.size(); i++){
            if(mActions.get(i).getPerson().equalsIgnoreCase(name)){
                aa.add(mActions.get(i));
            }
        }
        Collections.sort(aa,ModelAction.AmountComparator);
        return aa;
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
            String firstName = null;
            String lastName = null;
            String actioncc = null;

            //sequences[i].show();

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

                    if(name != null) {
                        lastName = name.toString();
                    }

                    if( action != null) {
                        actioncc = action.toString();
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

                boolean exists =false;
                for(String aa : names){
                    if (firstName.equalsIgnoreCase(aa)){
                        exists =true;
                        break;
                    }
                }

                if(!exists) {
                    firstName = null;
                } else {
                    firstName = firstName.replaceAll("[^A-Za-z0-9 ]", "");
                }

            }

            if(lastName != null) {

                boolean exists =false;
                for(String aa : names){
                    if (lastName.equalsIgnoreCase(aa)){
                        exists =true;
                        break;
                    }
                }

                if(!exists) {
                    lastName = null;
                } else {
                    lastName = lastName.replaceAll("[^A-Za-z0-9 ]", "");
                }

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
                mRelations.add(new ModelRelation(firstName, lastName, actioncc, 1));
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
