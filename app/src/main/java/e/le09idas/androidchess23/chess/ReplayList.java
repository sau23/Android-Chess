package e.le09idas.androidchess23.chess;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReplayList{

    private static ArrayList<Replay> replayList;
    private static String path;
    private static Replay selected;

    public ReplayList(){
        replayList = new ArrayList<Replay>();
    }

    public static void setPath(String s){
        path = s + "/chess";
    }

    public static Replay getReplay(int index){
        return replayList.get(index);
    }

    public static ArrayList<Replay> getReplayList(){
        return replayList;
    }
    // function to read from stored data

    public static void setSelected(Replay r){
        selected = r;
    }

    public static Replay getSelected(){
        return selected;
    }

    public void sortByDate(){
        Collections.sort(replayList, new SortByDate());
    }

    public void sortByName(){
        Collections.sort(replayList, new SortByName());
    }

    public static void readFromData(){

        File f = new File(path);

        // make a new directory for data
        if (!(f.exists() && f.isDirectory())) {
            replayList = new ArrayList<Replay>();
            f.mkdir();
            return;
        }

        // instantiate the replay list
        replayList = new ArrayList<Replay>();
        for(File file : f.listFiles()){
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getPath()));
                Replay replay = (Replay)in.readObject();
                replayList.add(replay);
                in.close();
                System.out.println("Successfully read file");
            } catch (IOException e){
                System.out.println("IO exception reading file");
            } catch (ClassNotFoundException c){
                System.out.println("This shouldn't happen");
            }
        }
    }

    public static void writeToData(Replay replay){
        File f = new File(path);

        // make a new directory for data
        if (!(f.exists() && f.isDirectory())) {
            replayList = new ArrayList<Replay>();
            f.mkdir();
        } else if (replayList == null) {
            replayList = new ArrayList<Replay>();
        }

        if(replayList == null){
            replayList = new ArrayList<Replay>();
        }
        replayList.add(replay);
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + "/" + replay.getName() + ".data"));
            out.writeObject(replay);
            out.close();
            System.out.println("Successfully wrote file");
        } catch (IOException e){
            System.out.println("IO exception writing file");
        }
    }
}
