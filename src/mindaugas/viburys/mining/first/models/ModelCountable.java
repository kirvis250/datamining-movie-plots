package mindaugas.viburys.mining.first.models;

import java.util.Comparator;
import java.util.Formatter;

/**
 * Created by minda on 2016-04-21.
 */
public class ModelCountable implements Comparable<ModelCountable> {

    private String name;
    private  int count;

    public ModelCountable(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public ModelCountable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(ModelCountable o) {
        return name.toLowerCase().compareTo(o.getName().toLowerCase());
    }


    public String getString(){
        return  String.format("%-20s %-10d", name, count);
    }


    public static final Comparator<ModelCountable>  AmountComparator = new Comparator<ModelCountable>() {
        @Override
        public int compare(ModelCountable o1, ModelCountable o2) {

            if(o1.getCount() < o2.getCount()) return 1;
            if(o1.getCount() > o2.getCount()) return -1;
            return 0;

        }
    };




}
