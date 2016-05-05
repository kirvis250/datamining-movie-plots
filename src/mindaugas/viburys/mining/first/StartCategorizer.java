package mindaugas.viburys.mining.first;


import mindaugas.viburys.mining.first.analyzers.SentenceParser;
import mindaugas.viburys.mining.first.analyzers.SentencePostTagger;
import mindaugas.viburys.mining.first.analyzers.TokenSpliter;
import mindaugas.viburys.mining.first.category.Categorizer;
import mindaugas.viburys.mining.first.category.CategoryTrainUtil;
import mindaugas.viburys.mining.first.category.ModelGlobalCategory;
import mindaugas.viburys.mining.first.category.ModelPlotCategory;
import mindaugas.viburys.mining.first.models.ModelMovie;
import mindaugas.viburys.mining.first.models.ModelMoviePlot;
import mindaugas.viburys.mining.first.reader.PlotsDataReader;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Sequence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minda on 2016-02-20.
 */
public class StartCategorizer {



    public static void main(String args[]) {

/*       try {
            CategoryTrainUtil.trainModel("movie_categories.txt", "movie_cats.bin");
        }catch (Exception e){}*/


        PlotsDataReader reader = new PlotsDataReader("../plot.list");
        reader.readMovies(5000);

        List<ModelMovie> movies = reader.getMoviesList();
        Categorizer detector = new Categorizer("movie_cats.bin");


        List<ModelGlobalCategory> categories = new ArrayList<>();



        for(int i = 0 ; i< movies.size(); i++) {
            List<ModelMoviePlot> plots = movies.get(i).getPlots();
            if(plots.size() > 0) {

                ModelPlotCategory category = detector.getCategory(plots.get(0).getPlot());


                for (int j = 1; j < plots.size(); j++) {
                    ModelPlotCategory nCat = detector.getCategory(plots.get(j).getPlot());

                    if(nCat.getScore() > category.getScore()){
                        category = nCat;
                    }
                }

                int index = getCategoryIndex(categories,category);
                if(index !=-1){
                    categories.get(index).getPlots().add(category);
                } else {
                    List<ModelPlotCategory> categoryList = new ArrayList<>();
                    categoryList.add(category);
                    categories.add(new ModelGlobalCategory(category.getCategory(), categoryList));
                }
            }
        }

        for(int i = 0; i< categories.size(); i++){
                System.out.println( categories.get(i).getName());
                System.out.println("  Numbers of movies in category " + categories.get(i).getPlots().size());
                System.out.println("  Examples");
                for(ModelPlotCategory cat : categories.get(i).get10PlotsBestFit()){
                    System.out.println("    " + cat.getScore() +",  " + cat.getPlotLine());
                }

        }

    }


    public static int getCategoryIndex( List<ModelGlobalCategory> categories,ModelPlotCategory category  ){
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getName().equalsIgnoreCase(category.getCategory())){
                return i;
            }
        }
        return -1;
    }


}



