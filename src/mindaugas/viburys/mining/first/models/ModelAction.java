package mindaugas.viburys.mining.first.models;

import com.sun.javafx.beans.annotations.NonNull;

import java.util.Comparator;

/**
 * Created by minda on 2016-04-28.
 */
public class ModelAction  implements Comparable<ModelAction> {

    private String person;
    private String action;
    private int count;


    public ModelAction(String person, String action, int count) {
        this.person = person;
        this.action = action;
        this.count = count;
    }


    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    @Override
    public int compareTo( @NonNull ModelAction o) {

        int st =  person.toLowerCase().compareTo(o.getPerson().toLowerCase());
        int nd =  action.toLowerCase().compareTo(o.getAction().toLowerCase());

        if(st == 0 &&  nd == 0)
            return 0;

        if(st == 0)
            return nd;

        if(nd == 0)
            return st;

        return 0;
    }


    public String getString(){
        return  String.format("%-20s %-20s %-10d", person,action, count);
    }

    public static final Comparator<ModelAction> AmountComparator = new Comparator<ModelAction>() {
        @Override
        public int compare(ModelAction o1, ModelAction o2) {

            if(o1.getCount() < o2.getCount()) return 1;
            if(o1.getCount() > o2.getCount()) return -1;
            return 0;

        }
    };




}
