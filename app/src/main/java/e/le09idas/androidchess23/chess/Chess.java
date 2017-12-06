package e.le09idas.androidchess23.chess;

import java.util.Scanner;
import java.util.ArrayList;
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
	public static boolean checkmate = false;//indicates if check mate has occurred
	public static int[][] cP = {{2,0}, {6, 0}, {2, 7}, {6, 7}};//castle coord; spaces the king can go to in a castling move
	
	// create game board
	public static Board board = new Board();

	/**
	 * There is one main loop, which uses another while loop to keep taking input if it is 
	 * bad input.  It checks if the move is valid and moves the pieces.
	 */

	// class debug boolean
	public static final boolean DEBUG = false;
	
	/**
	 * main() runs the main program.
	 * 
	 * @param args No arguments are given.
	 */
	/*
	public static void main(String[] args) {

		// variable to simulate switching turns; true means it's white's turn
		boolean turn = true;

		// create input buffer
		Scanner sc = new Scanner(System.in);

		// declare list of spaces for check
		ArrayList<int[]> pathToKing;
		
		// declare list of respondants for check
		ArrayList<int[]> respondants = new ArrayList<int[]>();
		
		// declare boolean check to see if king can move
		boolean kingCanMove = false;
		
		// check to see if draw has been asked
		boolean drawAsked = false;
		
		// basic game loop
		while(true) {
			
			// check for checkmate
			if(check && !kingCanMove && respondants.isEmpty()) {
				System.out.println();
				System.out.println("Checkmate");
				System.out.println();
				if(turn) {
					System.out.println("Black wins");
				} else {
					System.out.println("White wins");
				}
				break;
			
			// otherwise if the player is in check, print check and continue
			} else if(check) {
				System.out.println();
				System.out.println("Check");
				
			// but if the player isn't in check, test for stalemate
			} else if(!check && board.stalemate(turn)) {
				System.out.println();
				System.out.println("Stalemate");
				break;
			}

			// loop for valid input
			String input;
			int xO, yO, xD, yD, p;
			Piece piece;
			while(true) {

				System.out.println();
				
				// print whose turn it is
				if(turn) {
					System.out.print("White's move: ");
				} else {
					System.out.print("Black's move: ");
				}
				
				// get input
				input = getInput(sc);
				
				System.out.println();
				
				// check if other user agrees
				if(drawAsked && input.equals("draw")) {
					System.out.println("draw");
					System.exit(0);
				} else if(!drawAsked && input.equals("draw")){
					if (DEBUG) System.out.println("Attempting to end a draw when one hasn't started");
					System.out.println("Illegal move, try again");
					continue;
				} else if(input.equals("-1")){
					System.out.println("Illegal move, try again");
					continue;
				} else {
					drawAsked = false;
				}
				
				// resignation check
				if(input.equals("resign")) {
					if(turn) {
						System.out.println("Black wins");
					} else {
						System.out.println("White wins");
					}
					System.exit(0);
				} 
				
				// draw check
				boolean draw = input.length() > 5 && input.substring(6).equalsIgnoreCase("draw?");
				if(draw) {
					drawAsked = true;
				}
				
				xO = (int)input.charAt(0) - 65;
				yO = (int)input.charAt(1) - 49;
				xD = (int)input.charAt(3) - 65;
				yD = (int)input.charAt(4) - 49;
				p = input.length() > 5 ? (int)input.charAt(6) : -1;

				// check if coordinates are out of bounds
				if(isOOB(xO, yO, xD, yD)){
					System.out.println("Illegal move, try again");
					continue;

				// check if coordinates are the same
				}else if(xO == xD && yO == yD){
					if (DEBUG) System.out.println("Cannot have same coordinates");
					System.out.println("Illegal move, try again");
					continue;
				}
				
				piece = board.getPiece(xO, yO);
				
				// check if there is a piece in the origin
				if(piece == null) {
					if (DEBUG) System.out.println("There is no piece at the origin");
					System.out.println("Illegal move, try again");
					continue;
					
				// check if the piece is the player's piece
				} else if((piece.color == 'w' && !turn) || (piece.color == 'b' && turn)){
					if (DEBUG) System.out.println("Cannot move a piece that isn't yours");
					System.out.println("Illegal move, try again");
					continue;
					
				// check if input is attempting to promote a non-pawn piece
				} else if(piece.type != 'p' && !draw && p > 0) {
					if (DEBUG) System.out.println("Cannot promote a non-pawn piece");
					System.out.println("Illegal move, try again");
					continue;
					
				// check if input is attempting to promote a pawn while it isn't promotable
				// if a piece is type is type p, its destination is neither 0 7 and extra is already set
				} else if(piece.type == 'p' && yD != 0 && yD != 7 && !draw && p > 0) {
					if (DEBUG) System.out.println("Cannot promote pawn yet!");
					System.out.println("Illegal move, try again");
					continue;
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
						continue;
					}
				}
				
				// attempt to move the selected piece
				if(!piece.checkMove(xO, yO, xD, yD, board)) {
					if (DEBUG) System.out.println("This piece can't move that way!");
					System.out.println("Illegal move, try again");
					continue;
				}
			
				break;
			}

			// coordinates are legal, move piece
			move(xO, yO, xD, yD);

			// since the move was legal, you can turn off the check flag if it was set
			if(check) {
				check = false;
			}
			
			// check if piece moved was a promotable pawn
			if(piece.type == 'p') {
				Pawn pawn = (Pawn)(piece);
				if(pawn.canPromote()) {
					pawn.promote(p, board);
					
					// call move again to update the marker on the tile
					move(xD, yD, xD, yD);
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
			
			// print board
			board.printBoard();
		}
	}
*/
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
	 * Takes in a scanner that reads user input and identifies them accordingly.
	 * Possible inputs include coordinates, coordinates with promotion, resignation, draw
	 * initiation, and draw acceptance.
	 * 
	 * @param	sc	The scanner used to take in input.
	 * 
	 * @return A properly formatted string based on the input.
	 */
	public static String getInput(Scanner sc) {

		// read input until it reads one with proper format
		String input;
		while(!((input = sc.nextLine()).matches("[a-h]\\d [a-h]\\d {0,1}[ RNBQ]{0,1}"))) {
			
			// if a player resigns, return some sense
			if(input.equalsIgnoreCase("resign")) {
				return "resign";

			// if a player is attempting to start draw
			} else if(input.length() > 5 && input.substring(6).equalsIgnoreCase("draw?")) {
				return input.toUpperCase();
			
			// if a player is attempting to respond to a draw
			} else if(input.equalsIgnoreCase("draw")) {
				return "draw";
				
			// otherwise, incorrect format, keep checking input
			} else {
				if (DEBUG) System.out.println("Incorrect format");
				return "-1";
			}
		}
		
		return input.toUpperCase();
	}
	
	/**
	 * move() moves Piece to a given location after checking its possible locations
	 * 
	 * @param	xO	Original x coordinate
	 * @param	yO	Original y coordinate
	 * @param	xD	The destination x coordinate
	 * @param	yD	The destination y coordinate
	 */
	public static void move(int xO, int yO, int xD, int yD) {
		
		// get piece at origin
		Piece piece = board.getPiece(xO, yO);
		
		// move piece to the destination
		board.getTile(xO, yO).inhabitant = null;
		if(board.getPiece(xD, yD) != null) {
			board.getPiece(xD, yD).isCaptured = true;
		}
		board.getTile(xD, yD).inhabitant = piece;
		
		// update board in origin and destination to print properly
		board.getTile(xO, yO).resymbol();
		board.getTile(xD, yD).resymbol();
		
		// update piece's coordinates if it returns non-null
		if(piece != null) {
			piece.updatePosition(xD, yD);
		}
	}
	
}
