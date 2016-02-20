package mindaugas.viburys.mining.first.models;

/**
 * Created by minda on 2016-02-20.
 */
public class ModelMoviePlot {

    private String author;
    private String plot;

    public ModelMoviePlot(String author, String plot) {
        this.author = author;
        this.plot = plot;
    }

    public ModelMoviePlot() {
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
