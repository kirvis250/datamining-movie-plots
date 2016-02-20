package mindaugas.viburys.mining.first.reader;

import com.sun.xml.internal.txw2.IllegalSignatureException;
import mindaugas.viburys.mining.first.models.ModelMovie;
import mindaugas.viburys.mining.first.models.ModelMoviePlot;

import java.io.*;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by minda on 2016-02-20.
 */
public class PlotsDataReader {

    public static final String PREFIX_NAME = "MV:";
    public static final String PREFIX_PLOT_LINE = "PL:";
    public static final String PREFIX_AUTHOR = "BY:";

    public static final String MOVIES_SEPERATOR = "--------------------";

    private String filepath;


    private List<ModelMovie> moviesList;


    private String line;

    public PlotsDataReader( String filepath) {
        this.filepath = filepath;
    }


    public void readMovies(int movieCVount){
        moviesList = new ArrayList<>();
        File ff =new File(filepath);
        line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(ff));
            movies(br,movieCVount);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param br
     * @param count
     * @throws IOException
     */
    public void movies(BufferedReader br, int count) throws IOException {
        line = br.readLine();

        while (!line.contains(MOVIES_SEPERATOR)){
            line = br.readLine();
        }

        int i;
        for( i = 0; i < count && line != null ; i++)
        {
            try {
                ModelMovie movie = readMovie(br);
                moviesList.add(movie);
            }catch (Exception e){
                e.printStackTrace();
            }
         //   if(movie.getPlots().size() > 1)
          //      moviesList.add(movie);
          //  System.out.println(movie.getName()+ "plot count :" + movie.getPlots().size()  );
        }
        System.out.println( "MOVIE COUNT :" + i);
    }


    /**
     *
     * @param br
     * @return
     * @throws IOException
     */
    public ModelMovie readMovie(BufferedReader br) throws IOException{

        ModelMovie movie = new ModelMovie();

        while (!line.contains(PREFIX_NAME)) {
            line = br.readLine();
        }

        movie.setName(line.replace(PREFIX_NAME,""));

        while(!line.contains(MOVIES_SEPERATOR)){
            line = br.readLine();
            ModelMoviePlot plot = readPlot(br);
            if(plot.getPlot() != null && !plot.getPlot().isEmpty())
                movie.addModelMoviePlot(plot);
        }

        return movie;
    }


    /**
     *
     * @param br
     * @return
     * @throws IOException
     */
    public ModelMoviePlot readPlot(BufferedReader br)  throws IOException {
        ModelMoviePlot plot = new ModelMoviePlot();
        String plotlines = readPlotLines(br);
        plot.setPlot(plotlines);

        while (!line.startsWith(PREFIX_AUTHOR) && !line.startsWith(PREFIX_NAME) && !line.contains(MOVIES_SEPERATOR))
            line = br.readLine();

        if(line.startsWith(PREFIX_AUTHOR) ){
            if(plot.getAuthor() != null) throw new IllegalSignatureException("Plot already has author");
            plot.setAuthor(line.replace(PREFIX_AUTHOR,""));
        }

        return plot;
    }


    /**
     *
     * @param br
     * @return
     * @throws IOException
     */
    public String readPlotLines(BufferedReader br) throws IOException {
        String plot = "";

        while (line.isEmpty() && !line.startsWith(PREFIX_PLOT_LINE))
            line = br.readLine();

        while (!line.isEmpty() && line.startsWith(PREFIX_PLOT_LINE)) {

            plot = plot + line.replace(PREFIX_PLOT_LINE,"").replace("\n","");
            line = br.readLine();
        }

        return plot;
    }


    public List<ModelMovie> getMoviesList() {
        return moviesList;
    }

}
