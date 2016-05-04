package mindaugas.viburys.mining.first.models;

import java.util.Comparator;

/**
 * Created by minda on 2016-04-26.
 */
public class ModelRelation  implements Comparable<ModelRelation>{

    String personOne;
    String personTwo;
    String relation;
    int count;


    public ModelRelation() {
    }

    public ModelRelation(String personOne, String personTwo, String relation, int count) {
        this.personOne = personOne;
        this.personTwo = personTwo;
        this.relation = relation;
        this.count = count;
    }

    public String getPersonOne() {
        return personOne;
    }

    public void setPersonOne(String personOne) {
        this.personOne = personOne;
    }

    public String getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(String personTwo) {
        this.personTwo = personTwo;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static Comparator<ModelRelation> getAmountComparator() {
        return AmountComparator;
    }

    @Override
    public int compareTo(ModelRelation o) {
        int st =  personOne.toLowerCase().compareTo(o.getPersonOne().toLowerCase());
        int nd =  personTwo.toLowerCase().compareTo(o.getPersonTwo().toLowerCase());

        if(st == 0 &&  nd == 0)
            return 0;

        if(st == 0)
            return nd;

        if(nd == 0)
            return st;

        return 0;
    }


    public String getString(){
        return  String.format("%-20s %-20s %-20s %-10d", personOne, relation ,personTwo,  count);
    }


    public static final Comparator<ModelRelation> AmountComparator = new Comparator<ModelRelation>() {
        @Override
        public int compare(ModelRelation o1, ModelRelation o2) {

            if(o1.getCount() < o2.getCount()) return 1;
            if(o1.getCount() > o2.getCount()) return -1;
            return 0;

        }
    };



}
