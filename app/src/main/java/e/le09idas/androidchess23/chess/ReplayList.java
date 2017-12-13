package e.le09idas.androidchess23.chess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;



import static java.util.Collections.sort;

public class ReplayList implements Serializable{


    private ArrayList<Replay> replayList;

    public ReplayList(){
        replayList = new ArrayList<Replay>();
    }

    public void addReplay(Replay replay){
        replayList.add(replay);
    }

    public Replay getReplay(int index){
        return replayList.get(index);
    }

    // unnecessary?
    public void removeReplay(int index){
        replayList.remove(index);
    }

    public ArrayList<Replay> getReplayList(){
        return replayList;
    }
    // function to read from stored data

    public void sortByDate(){
        Collections.sort(replayList, new SortByDate());
    }

    public void sortByName(){
        Collections.sort(replayList, new SortByName());
    }

    public String[] stringList(){
        String[] list = new String[replayList.size()];
        for(int i = 0; i < replayList.size(); i++){
            list[i] = replayList.get(i).toString();
        }
        return list;
    }


}
