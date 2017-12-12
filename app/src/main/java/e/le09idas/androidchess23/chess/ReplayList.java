package e.le09idas.androidchess23.chess;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.w3c.dom.EntityReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.sort;

public class ReplayList implements Parcelable, Serializable{

    public static final String LOG_TAG = "ANDROID-CHESS-23";
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
    public static void importList() throws IOException, ClassNotFoundException {


        /*if (isSDReadable()) {

            File list = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED), "replays.ser");
            if (!(list.exists() && list.isDirectory())) {
                replayList = new ArrayList<>();
                list.mkdir();
                return;
            }
            String root = list.getAbsolutePath();
            try{
                ObjectInput in = new ObjectInputStream(new FileInputStream(root));
                replayList = (ArrayList<Replay>) in.readObject();
                in.close();
            }catch (IOException e){

            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }


        }*/
    }

    public static void exportList(){
        if(isSDWritable()){
            try{
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                        new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED)
                                .getAbsolutePath() + "replays.ser")));
                out.writeObject(replayList);
                out.close();
            }catch(IOException e){
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

    public String[] stringList(){
        String[] list = new String[replayList.size()];
        for(int i = 0; i < replayList.size(); i++){
            list[i] = replayList.get(i).toString();
        }
        return list;
    }

}
