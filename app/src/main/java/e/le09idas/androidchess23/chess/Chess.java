package e.le09idas.androidchess23.chess;

import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.Scanner;
import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * <h1>CHESS</h1>
 * This is the main file for the Chess program.
 * The program is a massive loop which takes in
 * input and applies it to a Board object that
 * holds Piece objects. 
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */
public class Chess {

	public static boolean check = false;//indicates whether or not the game is in check
	public static int[][] cP = {{2,0}, {6, 0}, {2, 7}, {6, 7}};//castle coord; spaces the king can go to in a castling move

	public static GridLayout cb;

	// create game board
	public static Board board;// = new Board();

	/**
	 * There is one main loop, which uses another while loop to keep taking input if it is 
	 * bad input.  It checks if the move is valid and moves the pieces.
	 */

	// classy debug boolean
	public static final boolean DEBUG = false;

	// source/destination variables
	private static int srcX = -1, srcY = -1, destX = -1, destY = -1;

	// variable to simulate switching turns; true means it's white's turn
	private static boolean turn = true;

	// declare list of spaces for check
	private static ArrayList<int[]> pathToKing;

	// declare list of respondants for check
	private static ArrayList<int[]> respondants = new ArrayList<int[]>();

	// declare boolean check to see if king can move
	private static boolean kingCanMove = false;

	/**
	 * run() runs the main program.
	 */
	public static void run(GridLayout chessboard) {

		// set the reference to chessboard
		cb = chessboard;

		// set chessboard's buttons' properties
		int size = cb.getChildCount();
		for(int i = 0; i < size; i++) {
			final ImageButton ib = (ImageButton) cb.getChildAt(i);
			final int index = i;
			ib.setOnClickListener(new ImageButton.OnClickListener(){
				@Override
				public void onClick(View v){
					Log.d("STATE", String.valueOf(index%8) + ", " + String.valueOf(7 - (index/8)));
					if(srcX == -1 && srcY == -1){
						srcX = index%8;
						srcY = 7 - (index/8);

					} else {
						destX = index%8;
						destY = 7 - (index/8);
						if(checkMove()){
							move(srcX, srcY, destX, destY);
							if(check) {
								check = false;
							}
						}

						// update en passant for pawns (cannot capture after player makes move)
						board.updatePawns(!turn);

						// update danger zones for opposite king
						pathToKing = board.updateDangerZones(turn);

						// if a path to the opposite king has been found, king is in check
						if(!pathToKing.isEmpty()) {

							// try to find options for opponent
							respondants = board.getRespondants(turn, pathToKing);

							// check to see if the king can move for next player's turn
							King king = !turn ? (King)board.wPieces[0][3] : (King)board.bPieces[0][4];
							kingCanMove = king.canMove(board);

							// opponent's check flag is set
							check = true;
						}

						// change turn
						turn = !turn;
						srcX = srcY = destX = destY = -1;
					}
				}
			});
		}

		// create new board instance
		board = new Board(cb);

		/*
			// check if piece moved was a promotable pawn
			if(piece.type == 'p') {
				Pawn pawn = (Pawn)(piece);
				if(pawn.canPromote()) {
					pawn.promote(p, board);
					
					// call move again to update the marker on the tile
					move(xD, yD, xD, yD);
				}
			}
			
			// print board
			//board.printBoard();
		}
		*/
	}

	private static boolean checkMove(){
		if(srcX == destX && srcY == destY){
			if (DEBUG) System.out.println("Cannot have same coordinates");
			System.out.println("Illegal move, try again");
			return false;
		}

		Piece piece = board.getPiece(srcX, srcY);

		// check if there is a piece in the origin
		if(piece == null) {
			if (DEBUG) System.out.println("There is no piece at the origin");
			System.out.println("Illegal move, try again");
			return false;

		// check if the piece is the player's piece
		} else if((piece.color == 'w' && !turn) || (piece.color == 'b' && turn)){
			if (DEBUG) System.out.println("Cannot move a piece that isn't yours");
			System.out.println("Illegal move, try again");
			return false;
		/*
		// check if input is attempting to promote a non-pawn piece
		} else if(piece.type != 'p' && !draw && p > 0) {
			if (DEBUG) System.out.println("Cannot promote a non-pawn piece");
			System.out.println("Illegal move, try again");

		// check if input is attempting to promote a pawn while it isn't promotable
		// if a piece is type is type p, its destination is neither 0 7 and extra is already set
		} else if(piece.type == 'p' && destY != 0 && destY != 7 && !draw && p > 0) {
			if (DEBUG) System.out.println("Cannot promote pawn yet!");
			System.out.println("Illegal move, try again");
		*/
		}

		// check if input is attempting to move a piece that wasn't found by getRespondants
		// or if input was king
		if(check) {
			boolean b = kingCanMove;
			for(int i = 0;i < respondants.size();i++) {
				int[] cds = respondants.get(i);
				b = b || (piece.x == cds[0] && piece.y == cds[1]);
			}
			if(!b) {
				if (DEBUG) System.out.println("Your king is in check!");
				System.out.println("Illegal move, try again");
				return false;
			}
		}

		// attempt to move the selected piece
		if(!piece.checkMove(srcX, srcY, destX, destY, board)) {
			if (DEBUG) System.out.println("This piece can't move that way!");
			System.out.println("Illegal move, try again");
			return false;
		}
		
		return true;
	}

	/**
	 * isOOB() checks if given origin and destination coordinates are out of bounds.
	 * 
	 * @param	xO	original x position
	 * @param	yO	original y position
	 * @param	xD	destination x
	 * @param	yD	destination y
	 * 
	 * @return A boolean of whether the coordinates are out of bounds
	 */
	public static boolean isOOB(int xO, int yO, int xD, int yD) {

		// check origin coordinates
		if(xO < 0 || xO > 7 || yO < 0 || yO > 7) {
			if (DEBUG) System.out.println("Origin coordinates are out of bounds");
			return true;
		}

		// check destination coordinates
		if(xD < 0 || xD > 7 || yD < 0 || yD > 7) {
			if (DEBUG) System.out.println("Destination coordinates are out of bounds");
			return true;
		}

		return false;
	}
	
	/**
	 * move() moves Piece to a given location after checking its possible locations
	 * 
	 * @param	xO	Original x coordinate
	 * @param	yO	Original y coordinate
	 * @param	xD	The destination x coordinate
	 * @param	yD	The destination y coordinate
	 */
	public static Piece move(int xO, int yO, int xD, int yD) {
		
		// get piece at origin
		Piece piece = board.getPiece(xO, yO);
		
		// move piece to the destination
		board.getTile(xO, yO).inhabitant = null;
		if(board.getPiece(xD, yD) != null) {
			board.getPiece(xD, yD).isCaptured = true;
		}
		board.getTile(xD, yD).inhabitant = piece;
		
		// update board in origin and destination to print properly
		//board.getTile(xO, yO).resymbol();
		//board.getTile(xD, yD).resymbol();
		((ImageButton)cb.getChildAt(xO + ((7 - yO) * 8))).setImageResource(android.R.drawable.list_selector_background);
		((ImageButton)cb.getChildAt(xD + ((7 - yD) * 8))).setImageResource(piece.getResId());
		
		// update piece's coordinates if it returns non-null
		if(piece != null) {
			piece.updatePosition(xD, yD);
		}

		// return piece just moved
		return piece;
	}
	
}
