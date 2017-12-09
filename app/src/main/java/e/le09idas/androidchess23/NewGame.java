package e.le09idas.androidchess23;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.view.SurfaceView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.view.View.OnClickListener;

import e.le09idas.androidchess23.chess.Chess;

public class NewGame extends AppCompatActivity implements View.OnClickListener{
    /*
    BoardView bv;//view that displays game
    Bitmap board;//actual board image
    Bitmap rook1;//actual rook image
    Bitmap rook2;//actual rook image
    float x, y;//arbitrary floats
    */

    GridLayout chessboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        // set back button properties
        Button goBack = (Button) findViewById(R.id.goBackNG);
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
            }
        });

        chessboard = (GridLayout) findViewById(R.id.chessboard);
        Chess.run(chessboard);

    }

    //from previous usage of AppCompActivity class
    private void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int clicked_id = v.getId(); // here you get id for clicked TableRow
        System.out.println(clicked_id);
    }

    /*
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
    */
}
