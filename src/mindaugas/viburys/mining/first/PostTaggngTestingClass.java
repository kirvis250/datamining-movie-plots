package mindaugas.viburys.mining.first;


import mindaugas.viburys.mining.first.analyzers.SentencePostTagger;
import mindaugas.viburys.mining.first.analyzers.TokenSpliter;
import opennlp.tools.util.Sequence;

import java.io.*;

/**
 * Created by minda on 2016-02-20.
 */
public class PostTaggngTestingClass {



    public static void main(String args[]){

        SentencePostTagger tagge = new SentencePostTagger();
        TokenSpliter spliter =new TokenSpliter();


        String sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence);


        sentence = "Tennesha tries to take a step back from her control issues and lets Errol take the lead on a date,while Laree is challenged to break down some of her walls and commit herself to Karl.";
        analyzeSentence(tagge,spliter,sentence);

/*
        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence);

        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence);


        sentence = "Henry asked Hillary on a date, but she refused and tell all about it to her father.";
        analyzeSentence(tagge,spliter,sentence)*/;


    }



    public static void analyzeSentence( SentencePostTagger tagge, TokenSpliter spliter, String  sentence  ){


        String [] splits = spliter.tokenize(sentence);
        Sequence[] sequences  = tagge.postTag(splits);
        String [] postTagStrings = tagge.postTagStrings(splits);

        for(int i = 0; i <sequences.length; i++){
            System.out.println("POST TAGS: " + sequences[i].toString());
        }
        System.out.println("Sentence: "+sentence);
        getRelevants(postTagStrings, splits);

    }




    public static String[] getRelevants(String [] tags, String [] splits){

        boolean run = true;

        int index = 0;


        String name = null;
        String verb = null;
        String name2 = null;

        for(; index <tags.length; index++) {


            if (name == null && tags[index].equalsIgnoreCase("NNP")) {
                name = splits[index];
                continue;
            }

            if (verb == null && name != null && tags[index].equalsIgnoreCase("VB")) {
                verb = splits[index];
                continue;
            }


            if (name2 == null  && name != null  && verb != null&&   tags[index].equalsIgnoreCase("NNP")) {
                name2 = splits[index];
                System.out.println("RELEVANT: "+" "+ name +" "+ verb +" "+ name2 );
                name = null;
                verb = null;
                name2 = null;
                continue;
            }
        }




        return null;
    }



}
