package e.le09idas.androidchess23;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.lang.Math;
import e.le09idas.androidchess23.chess.Board;
import e.le09idas.androidchess23.chess.King;
import e.le09idas.androidchess23.chess.Pawn;
import e.le09idas.androidchess23.chess.Bishop;
import e.le09idas.androidchess23.chess.Knight;
import e.le09idas.androidchess23.chess.Queen;
import e.le09idas.androidchess23.chess.Replay;
import e.le09idas.androidchess23.chess.ReplayList;
import e.le09idas.androidchess23.chess.Rook;
import e.le09idas.androidchess23.chess.Piece;

public class NewGame extends AppCompatActivity implements View.OnClickListener {

    public static boolean check;//indicates whether or not the game is in check
    public static int[][] cP = {{2, 0}, {6, 0}, {2, 7}, {6, 7}};//castle coord; spaces the king can go to in a castling move

    public static GridLayout cb;

    // create game board
    public static Board board;// = new Board();

    // classy debug boolean
    public static final boolean DEBUG = true;

    // source/destination variables
    private static int srcX = -1, srcY = -1, destX = -1, destY = -1;

    // variable to simulate switching turns; true means it's white's turn
    private static boolean turn;

    //piece taken during current turn; -1 for none, 0+ for pawn, bishop, etc
    static int take;

    // declare list of spaces for check
    private static ArrayList<int[]> pathToKing;

    // declare list of respondants for check
    private static ArrayList<int[]> respondants;

    // declare boolean check to see if king can move
    private static boolean kingCanMove;

    // image button variable
    public static ImageButton ib;

    // replay moves list
    private Replay replay;

    private static TextView status;

    private boolean canUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        // set back button properties
        Button goBack = (Button) findViewById(R.id.goBackNG);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        cb = (GridLayout) findViewById(R.id.chessboard);

        // set AI button properties
        Button ai = (Button) findViewById(R.id.ai);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get a random piece that can move based on turn
                Piece[][] pieces = turn ? board.wPieces : board.bPieces;

                // since it might take forever to find if a piece can move, if you cant find a new space in 10 iterations,
                // choose a new piece to move
                int count;
                boolean prevTurn = turn;
                while(true) {
                    count = 0;
                    int i = (int) (2 * Math.random()), j = (int) (8 * Math.random());
                    while (pieces[i][j].isCaptured || !pieces[i][j].canMove(board)) {
                        i = (int) (2 * Math.random());
                        j = (int) (8 * Math.random());
                    }

                    // move the piece
                    srcX = pieces[i][j].x;
                    srcY = pieces[i][j].y;
                    while (prevTurn == turn) {
                        destX = (int) (8 * Math.random());
                        destY = (int) (8 * Math.random());
                        if (checkMove()) {
                            move(srcX, srcY, destX, destY);

                            // check if piece moved was a promotable pawn
                            if (!checkPawn(board.getPiece(destX, destY), true)) {
                                replay.addCoordinates(srcX, srcY, destX, destY, -1, -1);

                                if (!kingWillBeInCheck()) {
                                    if (check) {
                                        check = false;
                                    }
                                    continueGame();
                                } else {
                                    turn = !turn;
                                    undoMove();
                                }
                            }
                        }
                        if(count > 10){
                            break;
                        }
                        count++;
                    }
                    if(prevTurn != turn){
                        break;
                    }
                }

                // reset input vars
                srcX = srcY = destX = destY = -1;
            }
        });

        // set undo button properties
        Button undo = (Button) findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoMove();
            }
        });

        Button resign = (Button) findViewById(R.id.resign);
        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resign();
            }
        });

        Button draw = (Button) findViewById(R.id.draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw();
            }
        });

        status = (TextView) findViewById(R.id.status);
        status.setText("White's Turn");

        // reset variables
        check = false;
        turn = true;
        respondants = new ArrayList<int[]>();
        kingCanMove = false;
        replay = new Replay();
        canUndo = true;

        // set chessboard's buttons' properties
        int size = cb.getChildCount();
        for (int i = 0; i < size; i++) {
            ib = (ImageButton) cb.getChildAt(i);
            final int index = i;
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (srcX == -1 && srcY == -1) {
                        srcX = index % 8;
                        srcY = 7 - (index / 8);
                        if (board.getPiece(srcX, srcY) == null) {
                            srcX = srcY = -1;
                        }
                    } else {
                        destX = index % 8;
                        destY = 7 - (index / 8);

                        if (checkMove()) {

                            move(srcX, srcY, destX, destY);

                            // check if piece moved was a promotable pawn
                            if (!checkPawn(board.getPiece(destX, destY), false)) {
                                replay.addCoordinates(srcX, srcY, destX, destY, -1, -1);

                                if(!kingWillBeInCheck()){
                                    if (check) {
                                        check = false;
                                    }
                                    continueGame();
                                } else {
                                    turn = !turn;
                                    undoMove();
                                }

                                // reset input vars
                                srcX = srcY = destX = destY = -1;
                            }
                        } else {
                            srcX = srcY = destX = destY = -1;
                        }
                    }
                }
            });
        }

        // create new board instance
        board = new Board(cb);
    }

    //from previous usage of AppCompActivity class
    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int clicked_id = v.getId(); // here you get id for clicked TableRow
        System.out.println(clicked_id);
    }

    private boolean checkMove() {
        if (srcX == destX && srcY == destY) {
            status.setText("Cannot have same coordinates");
            return false;
        }

        Piece piece = board.getPiece(srcX, srcY);

        // check if there is a piece in the origin
        if (piece == null) {
            status.setText("There is no piece at the origin");
            return false;

            // check if the piece is the player's piece
        } else if ((piece.color == 'w' && !turn) || (piece.color == 'b' && turn)) {

            status.setText("Cannot move a piece that isn't yours");
            return false;

        // check to see if the piece can move to begin with
        } else if(!piece.canMove(board)){
            if (DEBUG) System.out.println("This piece cannot move");
            return false;
        }

        // check if input is attempting to move a piece that wasn't found by getRespondants
        // or if input was king
        if (check) {

            boolean isRespondant = false;
            for (int i = 0; i < respondants.size(); i++) {
                int[] cds = respondants.get(i);
                isRespondant = isRespondant || (piece.x == cds[0] && piece.y == cds[1]);
            }
            boolean movingKing = false;
            if (piece.type == 'K') {
                movingKing = true;
            }
            //if youre not moving the king or the king cant move and you didnt choose a respondant
            if (!movingKing && !kingCanMove && !isRespondant) {
                status.setText("Your king is in check!");
                return false;
            }

            boolean destIsOnPathToKing = false;
            for (int i = 0; i < pathToKing.size(); i++) {
                int[] t = pathToKing.get(i);
                if (destX == t[0] && destY == t[1]) {
                    destIsOnPathToKing = true;
                }
            }
            System.out.println("dest is on path:" + destIsOnPathToKing);

            if((!isRespondant && !destIsOnPathToKing) && (kingCanMove && !movingKing)){
                status.setText("Solve your check!");
                return false;
            }
        }

        // attempt to move the selected piece
        if (!piece.checkMove(srcX, srcY, destX, destY, board)) {
            status.setText("This piece can't move that way!");
            return false;
        }

        return true;
    }

    public boolean checkPawn(Piece piece, boolean isAI) {
        if (piece.type != 'p') {
            return false;
        }

        final Pawn pawn = (Pawn) piece;
        if (pawn.canPromote()) {
            if(isAI){
                int i = (int)(4 * Math.random());
                switch(i){
                    default:
                    case 0:
                        pawn.promote('B', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 0, -1);
                        break;
                    case 1:
                        pawn.promote('N', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 1, -1);
                        break;
                    case 2:
                        pawn.promote('Q', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 2, -1);
                        break;
                    case 3:
                        pawn.promote('R', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 3, -1);
                        break;
                }
                status.setText("" + replay.lastMoveString());
                if (!kingWillBeInCheck()) {
                    if (check) {
                        check = false;
                    }
                    continueGame();
                } else {
                    turn = !turn;
                    undoMove();
                }

                // reset input vars
                srcX = srcY = destX = destY = -1;

            } else {
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("Choose piece to promote pawn");
                dialog.setContentView(R.layout.activity_promote);
                ImageButton bishop, knight, queen, rook;
                bishop = (ImageButton) dialog.findViewById(R.id.bishop);
                bishop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pawn.promote('B', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 0, -1);
                        status.setText("" + replay.lastMoveString());
                        if (!kingWillBeInCheck()) {
                            if (check) {
                                check = false;
                            }
                            continueGame();
                        } else {
                            turn = !turn;
                            undoMove();
                        }

                        // reset input vars
                        srcX = srcY = destX = destY = -1;
                        dialog.cancel();
                    }
                });
                knight = (ImageButton) dialog.findViewById(R.id.knight);
                knight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pawn.promote('N', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 1, -1);
                        status.setText("" + replay.lastMoveString());
                        if (!kingWillBeInCheck()) {
                            if (check) {
                                check = false;
                            }
                            continueGame();
                        } else {
                            turn = !turn;
                            undoMove();
                        }

                        // reset input vars
                        srcX = srcY = destX = destY = -1;
                        dialog.cancel();
                    }
                });
                queen = (ImageButton) dialog.findViewById(R.id.queen);
                queen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pawn.promote('Q', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 2, -1);
                        status.setText("" + replay.lastMoveString());
                        if (!kingWillBeInCheck()) {
                            if (check) {
                                check = false;
                            }
                            continueGame();
                        } else {
                            turn = !turn;
                            undoMove();
                        }

                        // reset input vars
                        srcX = srcY = destX = destY = -1;
                        dialog.cancel();
                    }
                });
                rook = (ImageButton) dialog.findViewById(R.id.rook);
                rook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pawn.promote('R', board);
                        move(destX, destY, destX, destY);
                        replay.addCoordinates(srcX, srcY, destX, destY, 3, -1);
                        status.setText("" + replay.lastMoveString());
                        if (!kingWillBeInCheck()) {
                            if (check) {
                                check = false;
                            }
                            continueGame();
                        } else {
                            turn = !turn;
                            undoMove();
                        }

                        // reset input vars
                        srcX = srcY = destX = destY = -1;
                        dialog.cancel();
                    }
                });
                if (piece.color == 'w') {
                    bishop.setImageResource(R.drawable.white_bishop);
                    knight.setImageResource(R.drawable.white_knight);
                    queen.setImageResource(R.drawable.white_queen);
                    rook.setImageResource(R.drawable.white_rook);
                } else {
                    bishop.setImageResource(R.drawable.black_bishop);
                    knight.setImageResource(R.drawable.black_knight);
                    queen.setImageResource(R.drawable.black_queen);
                    rook.setImageResource(R.drawable.black_rook);
                }
                dialog.show();
            }
            return true;
        }
        return false;
    }

    public boolean kingWillBeInCheck(){
        if(!board.updateDangerZones(!turn).isEmpty()){
            return true;
        }
        return false;
    }

    public void continueGame(){
        // update en passant for pawns (cannot capture after player makes move)
        board.updatePawns(!turn);

        // update danger zones for opposite king
        pathToKing = board.updateDangerZones(turn);

        // if a path to the opposite king has been found, king is in check
        if (!pathToKing.isEmpty()) {

            // try to find options for opponent
            respondants = board.getRespondants(turn, pathToKing);

            // check to see if the king can move for next player's turn
            King king = !turn ? (King) board.wPieces[0][3] : (King) board.bPieces[0][4];
            kingCanMove = king.canMove(board);

            // opponent's check flag is set
            check = true;
        }
        // change turn
        turn = !turn;
        canUndo = true;

        // check for checkmate
        if (check && !kingCanMove && respondants.isEmpty()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewGame.this);
            if (DEBUG) board.printBoard();
            if (turn) {
                alertDialog.setMessage("Black wins!\nSave replay?");
                replay.setResult("Checkmate! Black Wins!");
            } else {
                alertDialog.setMessage("White wins!\nSave replay?");
                replay.setResult("Checkmate! White Wins!");
            }
            alertDialog.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int rand = (int) (Math.random() * 50 + 1);
                            replay.setName("" + rand);
                            try {
                                saveGame();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.setTitle("Checkmate");
            alert.show();

            // otherwise if the player is in check, print check and continue
        } else if (check) {
            if (turn) {
                status.setText("White in check!");
            } else {
                status.setText("Black in check!");
            }
        }
    }

    /**
     * move() moves Piece to a given location after checking its possible locations
     *
     * @param    xO    Original x coordinate
     * @param    yO    Original y coordinate
     * @param    xD    The destination x coordinate
     * @param    yD    The destination y coordinate
     */
    public static void move(int xO, int yO, int xD, int yD) {

        // get piece at origin
        Piece piece1 = board.getPiece(xO, yO);
        Piece piece2 = board.getPiece(xD, yD);
        status.setText(piece1.toString());

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
        board.getTile(xD, yD).inhabitant = piece1;

        // update board in origin and destination to print properly
        board.getTile(xO, yO).resymbol();
        board.getTile(xD, yD).resymbol();
        ((ImageButton) cb.getChildAt(xO + ((7 - yO) * 8))).setImageResource(android.R.drawable.list_selector_background);
        ((ImageButton) cb.getChildAt(xD + ((7 - yD) * 8))).setImageResource(piece1.getResId());

        // update piece's coordinates if it returns non-null
        if (piece1 != null) {
            piece1.updatePosition(xD, yD);
        }
    }

    //save game to external storage
    public void saveGame() throws IOException, ClassNotFoundException {

        ReplayList replays = new ReplayList();
        replays = importList();
        ArrayList<Replay> replayList = replays.getReplayList();
        int i = 0;
        for (Replay replay : replayList) {
            if (replay.comareTo(replayList.get(i))) {
                return;
            }
        }
        replays.addReplay(replay);
        exportList(replays, replay);

    }

    public void undoMove() {

        if (!canUndo) {
            status.setText("Can't undo in sucession");
            return;
        }
        if (replay.getReplay().size() == 0) {
            status.setText("No previous moves");
            return;
        }

        int[] last;
        last = replay.getReplay().get(replay.getReplay().size() - 1);
        replay.removeLast();
        Piece piece1;
        Piece piece2 = null;

        if (last[4] != -1) {
            piece1 = new Pawn(last[2], last[3], board.getPiece(last[2], last[3]).color, 'p');
        } else {
            piece1 = board.getPiece(last[2], last[3]);
        }

        if (last[5] != -1) {
            char side;
            if (piece1.color == 'w') {
                side = 'b';
            } else {
                side = 'w';
            }

            switch (last[5]) {
                case 0:
                    piece2 = board.getTile(last[2], last[3]).inhabitant = new Pawn(last[2], last[3], side, 'p');
                    break;
                case 1:
                    piece2 = board.getTile(last[2], last[3]).inhabitant = new Bishop(last[2], last[3], side, 'B');
                    break;
                case 2:
                    piece2 = board.getTile(last[2], last[3]).inhabitant = new Knight(last[2], last[3], side, 'N');
                    break;
                case 3:
                    piece2 = board.getTile(last[2], last[3]).inhabitant = new Queen(last[2], last[3], side, 'Q');
                    break;
                case 4:
                    piece2 = board.getTile(last[2], last[3]).inhabitant = new Rook(last[2], last[3], side, 'R');
                    break;
            }

        }

        board.getTile(last[2], last[3]).inhabitant = piece2;
        board.getTile(last[0], last[1]).inhabitant = piece1;

        board.getTile(last[2], last[3]).resymbol();
        board.getTile(last[0], last[1]).resymbol();

        if (piece2 == null) {
            ((ImageButton) cb.getChildAt(last[2] + ((7 - last[3]) * 8))).setImageResource(android.R.drawable.list_selector_background);
            ((ImageButton) cb.getChildAt(last[0] + ((7 - last[1]) * 8))).setImageResource(piece1.getResId());
        } else {
            ((ImageButton) cb.getChildAt(last[2] + ((7 - last[3]) * 8))).setImageResource(piece2.getResId());
            ((ImageButton) cb.getChildAt(last[0] + ((7 - last[1]) * 8))).setImageResource(piece1.getResId());
        }


        if (piece1 != null) {
            piece1.updatePosition(last[0], last[1]);
        }

        if (piece2 != null) {
            piece2.updatePosition(last[2], last[3]);
        }

        printMove();
        turn = !turn;
        canUndo = false;

    }

    void draw() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewGame.this);
        alertDialog.setTitle("Draw Offered! Do You Accept?");
        if (DEBUG) board.printBoard();
        alertDialog.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        replay.setResult("Playters Agree to Draw.");
                        alertDialog.setTitle(replay.getResult());
                        alertDialog.show();
                        int rand = (int) (Math.random() * 50 + 1);
                        replay.setName("" + rand);
                        try {
                            saveGame();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.setTitle("Game is still on!");
                        alertDialog.show();
                        continueGame();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    void resign() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewGame.this);
        alertDialog.setTitle("Resignation");
        alertDialog.setMessage("Are you sure you want to Resign?");
        if (DEBUG) board.printBoard();
        alertDialog.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (turn) {
                            alertDialog.setMessage("White Resigns. Black wins!\nSave replay?");
                            replay.setResult("White Resigns. Black Wins!");

                        } else {
                            alertDialog.setMessage("Black Resigns. White wins!\nSave replay?");
                            replay.setResult("Black Resigns. White Wins!");
                        }
                        int rand = (int) (Math.random() * 50 + 1);
                        replay.setName("" + rand);
                        try {
                            saveGame();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.setMessage("The game continus!");
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        finish();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    void printMove() {
        if (replay.getReplay().size() == 0) {
            status.setText("No previous moves White's Turn");
            return;
        }
        if (turn) {
            status.setText(replay.lastMoveString() + " Black's Turn");
        } else {
            status.setText(replay.lastMoveString() + " White's Turn");
        }
    }

    public ReplayList importList() throws IOException, ClassNotFoundException {

        File file = getFilesDir();
        ReplayList replays = new ReplayList();
        if(!(file.exists() && file.isDirectory()) ){
            file.mkdir();
            return replays;
        }
        try {
            FileInputStream fin = openFileInput("replays.ser");
            ObjectInputStream in = new ObjectInputStream(fin);
            replays = (ReplayList)in.readObject();
            in.close();
            fin.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Successfully read from" + file.toString()+ "/replays.ser", Toast.LENGTH_SHORT).show();
        return replays;

    }

    public void exportList(ReplayList replays, Replay replay) {

        File file = getFilesDir();
        try {
            FileOutputStream fout = openFileOutput("replays.ser", MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(replays);
            out.close();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Successfully saved to" + file.toString()+ "/replays.ser", Toast.LENGTH_SHORT).show();
    }
}
