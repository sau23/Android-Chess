package e.le09idas.androidchess23.chess;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;



import static java.util.Collections.sort;

public class ReplayList implements Parcelable, Serializable{

    private static final long serialVersionUID = 10234L;

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

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        //dest.writeTypedList(this.replayList);
    }

    public static final Creator<ReplayList> CREATOR = new Creator<ReplayList>() {
        @Override
        public ReplayList createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public ReplayList[] newArray(int i) {
            return new ReplayList[0];
        }
    };

    public String[] stringList(){
        String[] list = new String[replayList.size()];
        for(int i = 0; i < replayList.size(); i++){
            list[i] = replayList.get(i).toString();
        }
        return list;
    }


}
