package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

public class Replay {

    private ArrayList<int[]> coordinates;
    private String name;

    public Replay(){
        this.coordinates = new ArrayList<int[]>();
        this.name = "";
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<int[]> getReplay(){
        return this.coordinates;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addCoordinates(int xO, int yO, int xD, int yD, int promote){
        int[] temp = new int[5];
        temp[0] = xO;
        temp[1] = yO;
        temp[2] = xD;
        temp[3] = yD;
        temp[4] = promote;
        this.coordinates.add(temp);
    }

    public void removeLast(){
        if(!this.coordinates.isEmpty()) {
            this.coordinates.remove(this.coordinates.size() - 1);
        }
    }
}
