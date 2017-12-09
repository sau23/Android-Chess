package e.le09idas.androidchess23;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.view.SurfaceView;

public class NewGame extends Activity implements View.OnTouchListener {

    BoardView bv;//view that displays game
    Bitmap board;//actual board image
    Bitmap rook1;//actual rook image
    Bitmap rook2;//actual rook image
    float x, y;//arbitrary floats

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //purpose of next 3 lines is to get the machines native screen size
        //to fit the board
        DisplayMetrics disDim = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disDim);
        int width = disDim.widthPixels;

        bv = new BoardView(this);//the view on which the other object are put on
        bv.setOnTouchListener(this);

        //loads chessboard image into bitmap then resclaes it
        board = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard);
        board = Bitmap.createScaledBitmap(board, width, width, true);

        //does the same for the rook objects
        //TODO: create compartmentalized process to intiialize a board and its pictures
        rook2 = rook1 =  BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);
        rook1 = Bitmap.createScaledBitmap(rook1, width/8, width/8, true);
        rook2 = Bitmap.createScaledBitmap(rook2, width/8, width/8, true);

        setContentView(bv);
        //setContentView(R.layout.activity_new_game);

    }

    //from previous usage of AppCompActivity class
    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //The next two methods are overrides of ones from the Activity class
    @Override
    protected void onPause(){
        super.onPause();
        bv.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        bv.resume();
    }

    //override need to implement View.OnTouchListern
    @Override
    public boolean onTouch(View bv, MotionEvent me) {
        return false;
    }

    //subclass that contains a thread to draw the board and pieces
    //TODO: might need to make separate class to compartmentalize process of created board
    public class BoardView extends SurfaceView implements Runnable{

        Thread t = null;
        SurfaceHolder holder;//controls when a Bitmap can be drawn on a Canvas
        boolean aok = false;

        public BoardView(Context context) {
            super(context);
            holder = getHolder();
        }

        //override of Runnable function
        @Override
        public void run() {
            while(aok){
                //checks if canvas can be modified
                if(!holder.getSurface().isValid()){
                    continue;//skip rest of block if canvas can't be changed
                }
                Canvas c = holder.lockCanvas();//what the bitmaps are drawn on
                c.drawARGB(255, 150, 150, 10);
                c.drawBitmap(board, x, 100, null);//draws board
                c.drawBitmap(rook1, 0, 100, null);//draw rook
                c.drawBitmap(rook2, (750*7)/8, 100, null);//draws rook
                holder.unlockCanvasAndPost(c);
            }
        }

        //pauses the thread process
        public void pause(){
            aok = false;
            while(true){
                try{
                    t.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        //continues the thread process
        public void resume(){
            aok = true;
            t = new Thread(this);
            t.start();

        }
    }
}
