package e.le09idas.androidchess23.chess;

import java.util.Comparator;

/**
 * Created by le09idas on 12/10/2017.
 */

public class SortByName implements Comparator<Replay> {
    @Override
    public int compare(Replay a, Replay b){
        return a.getName().compareTo(b.getName());
    }
}
