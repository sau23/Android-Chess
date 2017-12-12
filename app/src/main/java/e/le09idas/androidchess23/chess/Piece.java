package e.le09idas.androidchess23.chess;

import android.media.Image;

import java.util.ArrayList;
/**
 * Piece provides the basic set of moves
 * inherent in each type of piece.  It keeps a copy of the current pieces position
 * and information about its color, type, and whether or not it is captured (this
 * is utilized when determining if there are any other moves @see {@link Board#stalemate(boolean)}
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */
public abstract class Piece {
	
	public int x;//file
	public int y;//rank
	public char color;//w for white; b for black
	public char type;//P -> pawn
	public boolean isCaptured;
	public int resId;
	
	/**
	 * Pieces need coordinates, color and type; initial value for isCaputer is false
	 * 
	 * @param x Current x position
	 * @param y Current y position
	 * @param color 'b' for black; 'w' for white; used to print tile Symbol in @see {@link Tile#toString()}
	 * @param type 'p' for pawn, 'N' for knight, 'B' for bishop, 'R' for rook, 'Q' for queen, 'K' for king.
	 */
	public Piece(int x, int y, char color, char type){
		this.x = y;
		this.y = x;
		this.color = color;
		this.type = type;
		this.isCaptured = false;
	}
	
	/**
	 * Returns symbol for Pieces; taken from {@link #color} and {@link #type}
	 * used by {@link Tile#resymbol()}
	 */
	public String toString(){
		return "" + this.color + this.type + "";
	}
	
	/**
	 * Updates a piece's position to the input coordinates in the pair (x, y)
	 * 
	 * @param	x	the new x position
	 * @param	y	the new y position
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * isBound() tells whether a certain set of coordinates is 
	 * on the regulation chess board
	 *
	 * @param x The place's x position
	 * @param y The place's y position
	 * @return Whether the point is in the board
	 */
	public boolean isBound(int x, int y){
		if((x > - 1 && x < 8) && (y > - 1 && y < 8)) {
			return true;
		}
		return false;
	}

	public int getResId(){
		return this.resId;
	}

	/**
	 * checkMove() sees whether a given position matches
	 * the moves set of the piece in question; specific implementations
	 * are described in detail for each pieces in their files
	 * 
	 * @param xO x origin point of reference
	 * @param yO y origin point of reference
	 * @param xD x destination coordinate
	 * @param yD y destination coordinate
	 * @param board Given to see if anything is in the path of a move
	 * @return -> Whether a piece can make a move to a spot
	 */
	public abstract boolean checkMove(int xO, int yO, int xD, int yD, Board board);

	/**
	 * canMove() checks all possible moves from a piece, depending on its type;
	 * 
	 * @param board For reference to its position
	 * @return If a piece can move
	 */
	public abstract boolean canMove(Board board);
	
	/**
	 * Updates the board to show "danger zones" - where the king of the opposite 
	 * color can or cannot move - using the piece's attacking algorithm. Returns 
	 * a path to the opposite color's king if the piece finds it in its attacking 
	 * line of sight.
	 * 
	 * @param	board	 the current board configuration
	 * 
	 * @return a path to the opposite color's king
	 */
	abstract ArrayList<int[]> findDangerZone(Board board);

}
