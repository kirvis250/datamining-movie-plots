package mindaugas.viburys.mining.first;

import mindaugas.viburys.mining.first.reader.PlotsDataReader;

import javax.swing.*;

/**
 * Created by minda on 2016-02-18.
 */
public class StartingClass  extends JPanel {



    public static void main(String args[]){

        PlotsDataReader reader = new PlotsDataReader("plot.list");
        reader.readMovies(1000);

    }

}
