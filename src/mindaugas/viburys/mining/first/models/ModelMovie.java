package mindaugas.viburys.mining.first.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minda on 2016-02-20.
 */
public class ModelMovie {

    private String name;
    private List<ModelMoviePlot> plots;



    public ModelMovie() {
         name = null;
         plots = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelMoviePlot> getPlots() {
        return plots;
    }

    public void addModelMoviePlot(ModelMoviePlot plot){
        plots.add(plot);
    }


    public void setPlots(List<ModelMoviePlot> plots) {
        this.plots = plots;
    }
}
