package e.le09idas.androidchess23.chess;

import java.util.ArrayList;
import java.util.Date;

public class Replay {

    private ArrayList<int[]> coordinates;
    private String name;
    private Date date;
    private String result;

    public Replay(){
        this.coordinates = new ArrayList<int[]>();
        this.name = "";
        date = new Date();
        result = null;
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

    public void addCoordinates(int xO, int yO, int xD, int yD, int promote, int take){
        int[] temp = new int[6];
        temp[0] = xO;
        temp[1] = yO;
        temp[2] = xD;
        temp[3] = yD;
        temp[4] = promote;
        temp[5] = take;
        this.coordinates.add(temp);
    }

    public int[] getLast(){
        if(!this.coordinates.isEmpty()) {
            return this.coordinates.get(this.coordinates.size() - 1);
        }
        return null;

    }

    public void removeLast(){
        if(!this.coordinates.isEmpty()) {
            this.coordinates.remove(this.coordinates.size() - 1);
        }
    }

    public String lastMoveString(){
        if(getReplay().size() == 0){
            return null;
        }
        return "" + (char)(getLast()[0] + 97) + "" + (getLast()[1] + 1)+
                " to " + (char)(getLast()[2] + 97) + "" + (getLast()[3] + 1);
    }

    public Date getDate() {
        return date;
    }

    public boolean comareTo(Replay otherReplay){

        if(name.compareTo(otherReplay.getName()) == 0){
            if(date.compareTo(otherReplay.getDate()) == 0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public String getResult(){
        return result;
    }

    public void setResult(String result){
        this.result = result;
    }

    public String toString(){
        return this.name + this.date.toString();
    }


}
