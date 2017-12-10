package e.le09idas.androidchess23.chess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReplayList implements Parcelable {

    private static ArrayList<Replay> replayList;

    public ReplayList(){
        replayList = new ArrayList<Replay>();
    }

    public static void addReplay(Replay replay){
        replayList.add(replay);
    }

    public static Replay getReplay(int index){
        return replayList.get(index);
    }

    // unnecessary?
    public static void removeReplay(int index){
        replayList.remove(index);
    }

    // function to read from stored data
    public static void importList(){

    }

    // function to write to stored data
    public static void exportList(){

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
}
