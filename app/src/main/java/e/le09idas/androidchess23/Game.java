package e.le09idas.androidchess23;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import e.le09idas.androidchess23.chess.Board;
import e.le09idas.androidchess23.chess.Piece;
import e.le09idas.androidchess23.chess.ReplayList;

public class Game extends AppCompatActivity{

    private static GridLayout cb;
    private static Board board;
    private static ArrayList<int[]> replay;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        cb = (GridLayout)findViewById(R.id.chessboard);
        replay = ReplayList.getSelected().getReplay();
        index = replay.size()-1;

        Button home = (Button)findViewById(R.id.goBack);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        final Button next = (Button)findViewById(R.id.next);
        if(index == 0){
            next.setEnabled(false);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] moves = replay.get(index);
                System.out.println(moves[0] + ", " + moves[1]);
                System.out.println(moves[2] + ", " + moves[3]);
                System.out.println(moves[5]);
                move(moves[0], moves[1], moves[2], moves[3], moves[5]);
                index--;
                if(index == -1){
                    next.setEnabled(false);
                }
            }
        });

        board = new Board(cb, true);
    }

    private void goBack() {
        Intent intent = new Intent(this, RecordedGame.class);
        finish();
        startActivity(intent);
    }
    public static void move(int xO, int yO, int xD, int yD, int take) {

        // get piece at origin
        Piece piece = board.getPiece(xO, yO);

        //assume no piece will be taken at first
        take = -1;

        // move piece to the destination
        board.getTile(xO, yO).inhabitant = null;
        if (board.getPiece(xD, yD) != null) {

            //id piece and set take appropriately
            switch (board.getPiece(xD, yD).type) {
                case 'p':
                    take = 0;
                    break;
                case 'B':
                    take = 1;
                    break;
                case 'N':
                    take = 2;
                    break;
                case 'Q':
                    take = 3;
                    break;
                case 'R':
                    take = 4;
                    break;
            }
            board.getPiece(xD, yD).isCaptured = true;
        }
        board.getTile(xD, yD).inhabitant = piece;

        // update board in origin and destination to print properly
        board.getTile(xO, yO).resymbol();
        board.getTile(xD, yD).resymbol();
        ((ImageView) cb.getChildAt(xO + ((7 - yO) * 8))).setImageResource(android.R.drawable.list_selector_background);
        if(piece != null) {
            ((ImageView) cb.getChildAt(xD + ((7 - yD) * 8))).setImageResource(piece.getResId());
        } else {
            ((ImageView) cb.getChildAt(xD + ((7 - yD) * 8))).setImageResource(android.R.drawable.list_selector_background);
        }

        // update piece's coordinates if it returns non-null
        if (piece != null) {
            piece.updatePosition(xD, yD);
        }
    }

}
