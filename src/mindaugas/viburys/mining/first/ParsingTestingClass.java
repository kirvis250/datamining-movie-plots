package mindaugas.viburys.mining.first;


import mindaugas.viburys.mining.first.analyzers.SentenceParser;
import mindaugas.viburys.mining.first.analyzers.SentencePostTagger;
import mindaugas.viburys.mining.first.analyzers.TokenSpliter;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Sequence;

/**
 * Created by minda on 2016-02-20.
 */
public class ParsingTestingClass {



    public static void main(String args[]){

        SentenceParser parser = new SentenceParser();
        TokenSpliter spliter =new TokenSpliter();


        String sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(parser, spliter, sentence);




    /*    sentence = "Tennesha tries to take a step back from her control issues and lets Errol take the lead on a date,while Laree is challenged to break down some of her walls and commit herself to Karl.";
        analyzeSentence(parser, spliter, sentence);*/

/*
        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence);

        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence);


        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence)*/;


    }



    public static void analyzeSentence( SentenceParser tagge, TokenSpliter spliter, String  sentence  ){


        //String [] splits = spliter.tokenize(sentence);
        Parse[] sequences  = tagge.parseSentence(sentence);

        for(int i = 0; i <sequences.length; i++){
            System.out.println("POST TAGS: " + sequences[i].toString());
        }
        System.out.println("Sentence: " + sentence);
        getRelevants(sequences, sentence);

    }




    public static void getRelevants(Parse [] sequences, String sentence){
        for(int i = 0; i < sequences.length; i++){
            // sequences[i].show();

            String firstName = null;
            String lastName = null;
            String actioncc = null;


            for(Parse chil : sequences[i].getChildren()){

                if(chil.getType().equalsIgnoreCase("S")) {
                    getRelevants(new Parse[]{chil}, sentence);
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

            if(firstName != null && lastName != null && actioncc != null){
                System.out.println("RELEVANT: " + firstName +" "+ actioncc +" "+lastName);
            }
        }
    }


/*    public void getRelations(Parse [] sequences){

    if(chil.getType().equalsIgnoreCase("NP")){
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


    if(firstName != null && lastName != null && actioncc != null){
        System.out.println("RELEVANT: " + firstName +" "+ actioncc +" "+lastName);
    }



    }*/







    public static Parse getNP(Parse chil){

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


    public static Parse getVP(Parse chil){

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




    public static void printTree(String ss){
        StringBuffer buffer = new StringBuffer();
        int depth = 0;

        boolean closing = false;

        boolean closing2 = false;

        for(char a : ss.toCharArray()){

            if(a == '('){
                depth += 1;
                buffer.append( (closing ? "" : "\n") +depth(depth)+a);
                closing = false;
                closing2 = false;
            } else if(a == ')'){

                if(!closing2) {
                    buffer.append("\n"+depth(depth)+a);
                    depth -= 1;
                }else {
                    buffer.append(a);
                    depth -= 1;
                }

                closing2 = false;

            } else {
                buffer.append(a);
                closing = false;
                closing2 = true;
            }
        }
        System.out.println(buffer.toString());
    }



    public static String depth(int depth){
        String aa = new String();
        for(int i = 0; i <depth ; i++)
            aa+= "*";
        return aa;
    }



}



