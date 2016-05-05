package mindaugas.viburys.mining.first.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by minda on 2016-05-04.
 */
public class ModelGlobalCategory implements Comparable<ModelGlobalCategory> {
    protected String name;
    protected List<ModelPlotCategory>  plots;

    public ModelGlobalCategory(String name, List<ModelPlotCategory> plots) {
        this.name = name;
        this.plots = plots;
    }

    public ModelGlobalCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelPlotCategory> getPlots() {
        return plots;
    }

    public void setPlots(List<ModelPlotCategory> plots) {
        this.plots = plots;
    }


    @Override
    public int compareTo(ModelGlobalCategory o) {
        return name.compareTo(o.getName());
    }



    public List<ModelPlotCategory> get10PlotsBestFit() {

        List<ModelPlotCategory> returnable = new ArrayList<>();

        Collections.sort(plots);

        for(int i = 0; i< plots.size() && i < 10; i++){
            returnable.add(plots.get(i));
        }

        return returnable;
    }




}
