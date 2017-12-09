package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * Rook inherits from Piece; has one additional 
 * attribute used for castling {@link King#checkCastle(int, int, Board)}
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */

public class Rook extends Piece{

	boolean hasMoved = false;//used for seeing if the rook has moved for castling
	
	public Rook(int x, int y, char c, char t){

		super(x, y, c, t);
		if(c == 'w'){
			this.resId = R.drawable.white_rook;
		} else {
			this.resId = R.drawable.black_rook;
		}

	}

	/**
	 * checkMove() sees whether a move from (xO, yO) to (xD, yD) is 
	 * a valid move for Rook.
	 * 
	 * @param xO Original x coordinate
	 * @param yO Original y coordinate
	 * @param xD Destination x coordinate
	 * @param yD Destination y coordinate
	 * @param board Reference for the Rook to use
	 * @return true if it can move; false otherwise
	 */
	boolean checkMove(int xO, int yO, int xD, int yD, Board board) {

		//rook can move vertically or horizontally in any direction
		//we don't need slope just displacement

		//variables for later use
		boolean vertical = xO == xD;
		boolean horizontal = yO == yD;
		Piece dest = board.getPiece(xD, yD);
		boolean hasPiece;

		hasPiece = dest != null;
		//for going up or down
		if(vertical){
			//going down
			if(yO > yD){
				for(int i = yO - 1; i > yD; i--){//iterate through linear path
					if(board.getPiece(xD, i) != null)						
						return false;
				}
				if(dest != null && hasPiece){

					if(this.color == dest.color)	
						return false;//can't move because same color
					else
						return true;//can take because opp color
				}
			}
			//moving up
			if(yO < yD){
				for(int i = yO + 1; i < yD; i++){//iterate through linear path
					if(board.getPiece(xD, i) != null)
						return false;
				}
				if(dest != null && hasPiece){
					if(this.color == dest.color)
						return false;//can't move because same color
					else	
						return true;//can take because opp color	
				}
			}
			if(!this.hasMoved)//piece can't castle after it moves
				this.hasMoved = !this.hasMoved;
			return true;
		}
		//for going left of right
		if(horizontal){
			//going left
			if(xO > xD){
				for(int i = xO - 1; i > xD; i--){//iterate through linear path
					if(board.getPiece(i, yD) != null)						
						return false;			
				}
				if(dest != null && hasPiece){	
					if(this.color == dest.color)
						return false;//can't move because same color
					else
						return true;//can take because opp color	
				}
			}
			//moving right
			if(xO < xD){
				for(int i = xO + 1; i < xD; i++){//iterate through linear path	
					if(board.getPiece(i, yD) != null)
						return false;	
				}
				if(dest != null && hasPiece){
					if(this.color == dest.color)
						return false;//can't move because same color
					else
						return true;//can take because opp color		
				}
			}
			if(!this.hasMoved)//piece can't castle after it moves
				this.hasMoved = !this.hasMoved;
			return true;
		}

		//rook can't make the move
		return false;
	}

	/**
	 * canMove() sees whether a Rook can make any more moves.
	 * 
	 * @param board Used as a reference for a Rook
	 * @return true if there are any more; false otherwise
	 */
	boolean canMove(Board board) {

		//have to check vertically and horizontally from rook

		//check up
		if(this.isBound(this.x, this.y + 1) &&
				(board.getPiece(this.x, this.y + 1) == null || board.getPiece(this.x, this.y + 1).color == this.color))
			return true;
		//check down
		if(this.isBound(this.x, this.y - 1) &&
				(board.getPiece(this.x, this.y - 1) == null || board.getPiece(this.x, this.y - 1).color == this.color))
			return true;
		//check right
		if(this.isBound(this.x + 1, this.y) &&
				(board.getPiece(this.x + 1, this.y) == null || board.getPiece(this.x + 1, this.y).color == this.color))
			return true;
		//check left
			if(this.isBound(this.x - 1, this.y + 1) &&
					(board.getPiece(this.x - 1, this.y + 1) == null || board.getPiece(this.x - 1, this.y + 1).color == this.color))
			return true;
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
		
		// add danger zones in the north
		for(int i = 1;y + i < 8;i++) {
			
			// search for open tiles
			if(board.getPiece(x, y + i) == null) {
				if(c == 'w') {
					board.getTile(x, y + i).bDanger = true;
				} else {
					board.getTile(x, y + i).wDanger = true;
				}
				
			// inhabited tile, check if its king of opposite color and end prematurely
			} else {
				piece = board.getPiece(x, y + i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;y + j < piece.y;j++) {
						ret.add(new int[] {x, y + j});
					}
				
				// otherwise, check if its a piece of same color
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x, y + i).bDanger = true;
					} else {
						board.getTile(x, y + i).wDanger = true;
					}
				}
				break;
			}
		}
		
		// add danger zones in the south
		for(int i = 1;y - i > -1;i++) {
			
			// search for open tiles
			if(board.getPiece(x, y - i) == null) {
				if(c == 'w') {
					board.getTile(x, y - i).bDanger = true;
				} else {
					board.getTile(x, y - i).wDanger = true;
				}
				
			// inhabited tile, check if its king of opposite color and end prematurely
			} else {
				piece = board.getPiece(x, y - i);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;y - j > piece.y;j++) {
						ret.add(new int[] {x, y - j});
					}
					
				// otherwise check if its a piece of the same color
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x, y - i).bDanger = true;
					} else {
						board.getTile(x, y - i).wDanger = true;
					}
				}
				break;
			}
		}
		
		// add danger zones in the west
		for(int i = 1;x + i < 8;i++) {
			
			// search for open tiles
			if(board.getPiece(x + i, y) == null) {
				if(c == 'w') {
					board.getTile(x + i, y).bDanger = true;
				} else {
					board.getTile(x + i, y).wDanger = true;
				}
				
			// inhabited tile, check if its king of opposite color and end prematurely
			} else {
				piece = board.getPiece(x + i, y);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x + j < piece.x;j++) {
						ret.add(new int[] {x + j, y});
					}
					
				// otherwise check if its a piece of the same color
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x + i, y).bDanger = true;
					} else {
						board.getTile(x + i, y).wDanger = true;
					}
				}
				break;
			}
		}
		
		// add danger zones in the east
		for(int i = 1;x - i > -1;i++) {
			
			// search for open tiles
			if(board.getPiece(x - i, y) == null) {
				if(c == 'w') {
					board.getTile(x - i, y).bDanger = true;
				} else {
					board.getTile(x - i, y).wDanger = true;
				}
				
			// inhabited tile, check if its king of opposite color and end prematurely
			} else {
				piece = board.getPiece(x - i, y);
				
				// if it is, record the path taken to find the king
				if(piece.type == 'K' && piece.color != c) {
					for(int j = 0;x - j > piece.x;j++) {
						ret.add(new int[] {x - j, y});
					}
					
				// otherwise check if its a piece of the same color
				} else if(piece.color == c) {
					if(c == 'w') {
						board.getTile(x - i, y).bDanger = true;
					} else {
						board.getTile(x - i, y).wDanger = true;
					}
				}
				break;
			}
		}
		
		return ret;
	}
	
}
