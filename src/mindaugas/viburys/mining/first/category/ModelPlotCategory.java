package mindaugas.viburys.mining.first.category;

/**
 * Created by minda on 2016-05-04.
 */
public class ModelPlotCategory implements Comparable<ModelPlotCategory>{

    protected String category;
    protected double score;
    protected String plotLine;

    public ModelPlotCategory(String category, double score, String plotLine) {
        this.category = category;
        this.score = score;
        this.plotLine = plotLine;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPlotLine() {
        return plotLine;
    }

    public void setPlotLine(String plotLine) {
        this.plotLine = plotLine;
    }

    @Override
    public int compareTo(ModelPlotCategory o) {
        if(score < o.getScore()) return 1;
        if(score > o.getScore()) return -1;
        return 0;
    }
}
