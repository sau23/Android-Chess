package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * Bishop inherits attributes from Piece and includes its own functions
 * for moving and determining its danger zones.
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */
public class Bishop extends Piece{
	
	/**
	 * Bishop constructor.
	 * 
	 * @param	x	The starting x position
	 * @param	y	The starting y position
	 * @param	c	The color expressed as 'b' for black or 'w' for white
	 * @param	t	The type expressed as 'B' for bishop
	 * 
	 * @return	a new bishop piece
	 */
	
	public Bishop(int x, int y, char c, char t){
		super(x, y, c, t);
		if(c == 'w'){
			this.resId = R.drawable.white_bishop;
		} else {
			this.resId = R.drawable.black_bishop;
		}
	}
	
	/**
	 *checkMove() sees whether a Bishop can move diagonally to a destination spot.
	 *
	 *@param xO Original x coordinate
	 *@param yO Original y coordinate
	 *@param xD Destination x coordinate
	 *@param yD Destination y coordinate
	 *@param board The reference used by Bishop for moving
	 *@return true if Bishop can move; false otherwise
	 */
	
	public boolean checkMove(int xO, int yO, int xD, int yD, Board board) {
		
		// vars
		int rise = yD - yO;
		int run = xD - xO;
		
		// attempted vertical movement
		if(run == 0) {
			return false;
		}
		
		double slope = (double)rise/(double)run;
		
		// bishops can only move diagonally
		if(slope != 1.0 && slope != -1.0) {
			return false;
		}
		
		// vars
		int xlow = xO < xD ? xO : xD;
		int ylow = yO < yD ? yO : yD;
		int xhigh = xO > xD ? xO : xD;
		int yhigh = yO > yD ? yO : yD;
		
		// if there's anything in the way, return false immediately
		// depending on slope, check pieces along line accordingly:
		
		if(slope == 1.0) {
			for(int i = 1;ylow + i < yhigh;i++) {
				if(board.getPiece(xlow + i, ylow + i) != null) {
					return false;
				}
			}
		} else {
			for(int i = 1;ylow + i < yhigh;i++) {
				if(board.getPiece(xhigh - i, ylow + i) != null) {
					return false;
				}
			}
		}
		
		// check for piece at destination
		Piece dest = board.getPiece(xD, yD);
		
		if(dest != null) {
			if(dest.color == this.color) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns a boolean value based on whether or not the bishop can make
	 * any move. This simply checks to see if any of its adjacent potential
	 * positions are obstructed by pieces of the same color or if they exist
	 * (if this piece is currently at an edge of the board).
	 * 
	 * @param	board	the current board configuration
	 * 
	 * @return	whether the bishop can move at all
	 */
	
	public boolean canMove(Board board){
		
		// a bishop won't be able to move its 4 spots are surrounded by its own pieces
		
		// get coordinates
		int x = this.x;
		int y = this.y;
		
		// check up right direction for adjacent pieces
		if(x + 1 < 8 && y + 1 < 8 && (board.getPiece(x + 1, y + 1) == null || (board.getPiece(x + 1, y + 1) != null && board.getPiece(x + 1, y + 1).color != this.color)) ) {
			return true;
		}
		// check up left direction for adjacent pieces
		if(x - 1 > -1 && y + 1 < 8 && (board.getPiece(x - 1, y + 1) == null || (board.getPiece(x - 1, y + 1) != null && board.getPiece(x - 1, y + 1).color != this.color)) ) {
			return true;
		}
		// check down left direction for adjacent pieces
		if(x - 1 > -1 && y - 1 > -1 && (board.getPiece(x - 1, y - 1) == null || (board.getPiece(x - 1, y - 1) != null && board.getPiece(x - 1, y - 1).color != this.color)) ) {
			return true;
		}
		// check down right direction for adjacent pieces
		if(x + 1 < 8 && y - 1 > -1 && (board.getPiece(x + 1, y - 1) == null || (board.getPiece(x + 1, y - 1) != null && board.getPiece(x + 1, y - 1).color != this.color)) ) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a path to the opposite color's king if it is within the piece's
	 * current line of sight. This path is returned as an ArrayList of coordinate
	 * pairs expressed as int arrays of size 2. This function also updates the 
	 * danger zone flags in each tile that it can see.
	 * 
	 * @param	board	the current board configuration
	 * 
	 * @return	an ArrayList containing the path to the opposite king
	 */
	ArrayList<int[]> findDangerZone(Board board){
		
		// vars
		ArrayList<int[]> ret = new ArrayList<int[]>();
		char c = this.color;
		int x = this.x, y = this.y;
		Piece piece;
		
		// check top right 
		for(int i = 1;x + i < 8 && y + i < 8;i++) {
			
			// search for open tiles
			if(board.getPiece(x + i, y + i) == null) {
				if(c == 'w') {
					board.getTile(x + i, y + i).bDanger = true;
				} else {
					board.getTile(x + i, y + i).wDanger = true;
				}
			} else {
				piece = board.getPiece(x + i, y + i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x + j < piece.x && y + j < piece.y;j++) {
						ret.add(new int[] {x + j, y + j});
					}
					
				// if its a piece of same color, make sure its covered by this piece's danger zone
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x + i, y + i).bDanger = true;
					} else {
						board.getTile(x + i, y + i).wDanger = true;
					}
					break;
				} else if(piece.color != c){
					break;
				}
			}
		}
		
		// check top left
		for(int i = 1;x - i > -1 && y + i < 8;i++) {
			
			// search for open tiles
			if(board.getPiece(x - i, y + i) == null) {
				if(c == 'w') {
					board.getTile(x - i, y + i).bDanger = true;
				} else {
					board.getTile(x - i, y + i).wDanger = true;
				}
			} else {
				piece = board.getPiece(x - i, y + i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x - j > piece.x && y + j < piece.y;j++) {
						ret.add(new int[] {x - j, y + j});
					}
					
				// if its a piece of same color, make sure its covered by this piece's danger zone
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x - i, y + i).bDanger = true;
					} else {
						board.getTile(x - i, y + i).wDanger = true;
					}
					break;
				} else if(piece.color != c){
					break;
				}
			}
		}
		
		// check bottom right
		for(int i = 1;x + i < 8 && y - i > -1;i++) {
			
			// search for open tiles
			if(board.getPiece(x + i, y - i) == null) {
				if(c == 'w') {
					board.getTile(x + i, y - i).bDanger = true;
				} else {
					board.getTile(x + i, y - i).wDanger = true;
				}
			} else {
				piece = board.getPiece(x + i, y - i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x + j < piece.x && y - j > piece.y;j++) {
						ret.add(new int[] {x + j, y - j});
					}
					
				// if its a piece of same color, make sure its covered by this piece's danger zone
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x + i, y - i).bDanger = true;
					} else {
						board.getTile(x + i, y - i).wDanger = true;
					}
					break;
				} else if(piece.color != c){
					break;
				}
			}
		}
		
		// check bottom left
		for(int i = 1;x - i > -1 && y - i > -1;i++) {
			
			// search for open tiles
			if(board.getPiece(x - i, y - i) == null) {
				if(c == 'w') {
					board.getTile(x - i, y - i).bDanger = true;
				} else {
					board.getTile(x - i, y - i).wDanger = true;
				}
			} else {
				piece = board.getPiece(x - i, y - i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x - j > piece.x && y - j > piece.y;j++) {
						ret.add(new int[] {x - j, y - j});
					}
					
				// if its a piece of same color, make sure its covered by this piece's danger zone
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x - i, y - i).bDanger = true;
					} else {
						board.getTile(x - i, y - i).wDanger = true;
					}
					break;
				} else if(piece.color != c){
					break;
				}
			}
		}
		
		return ret;
	}
	
}
