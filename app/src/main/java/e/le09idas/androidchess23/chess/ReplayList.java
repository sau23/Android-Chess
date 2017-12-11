package e.le09idas.androidchess23.chess;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.sort;

public class ReplayList implements Parcelable, Serializable{

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

    public ArrayList<Replay> getReplayList(){
        return replayList;
    }
    // function to read from stored data
    public static ReplayList importList(){

        String games = Environment.getExternalStorageDirectory().getPath() + "/AndroidChess23/replays.data";

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(games));
            replayList = (ArrayList<Replay>) is.readObject();
            is.close();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // function to write to stored data
    public static void exportList() {
        if(isSDWritable()){

            String games = Environment.getExternalStorageDirectory().getPath() + "/AndroidChess23/replays.data";

            try {
                ObjectOutputStream os = new ObjectOutputStream((new FileOutputStream(games)));
                os.writeObject(replayList);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sortByDate(){
        Collections.sort(replayList, new SortByDate());
    }

    public void sortByName(){
        Collections.sort(replayList, new SortByName());
    }

    public static boolean isSDWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    public static boolean isSDReadable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;

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
