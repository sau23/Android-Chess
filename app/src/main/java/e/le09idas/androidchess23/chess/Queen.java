package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * Queen inherits from Piece; can move in the same way
 * as Bishop and Rook. 
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */

public class Queen extends Piece {
		
		/**
		 * Queen takes in the same input as any other
		 * Piece.
		 * 
		 * @param x x coordinate of the piece
		 * @param y y coordinate of the piece
		 * @param c Color for queen
		 * @param t Character for queen
		 */
		public Queen(int x, int y, char c, char t){
			super(x, y, c, t);
			if(c == 'w'){
				this.resId = R.drawable.white_queen;
			} else {
				this.resId = R.drawable.black_queen;
			}
		}

		/**
		 * checkMove() checks the diagonals and vertical and horizontal 
		 * regions which it can move to see if it can go from (xO, yO) to (xD, yD).
		 * 
		 * @param xO Original x coordinate
		 * @param yO Original y coordinate
		 * @param xD Destination x coordinate
		 * @param yD Destination y coordinate
		 * @param board The board of reference for the queen
		 * @return True if the queen can make the move; false if not
		 */
		public boolean checkMove(int xO, int yO, int xD, int yD, Board board){
			
			// vars
			int rise = yD - yO;
			int run = xD - xO;
			Piece dest = board.getPiece(xD, yD);
			
			// determine which way the queen wants to move before checking if its possible
			
			// moving along cardinal directions
			if((run == 0 && rise != 0)||(rise == 0 && run != 0)) {

				// only vertical movement
				if(run == 0) {
					
					// vars
					int ymax = yD > yO ? yD : yO;
					int ymin = yD < yO ? yD : yO;
					
					// if you find any piece within the path to the destination, automatically return false
					for(int i = 1;ymin + i < ymax;i++) {
						if(board.getPiece(xO, ymin + i) != null) {
							return false;
						}
					}
					
					// check if piece exists and if its color is opposite to moving piece
					if(dest == null || (dest != null && dest.color != this.color)) {
						return true;
					}
					
					return false;
					
				// only horizontal movement
				} else {
					
					// vars
					int xmax = xD > xO ? xD : xO;
					int xmin = xD < xO ? xD : xO;
					
					// if you find any piece within the path to the destination, automatically return false
					for(int i = 1;xmin + i < xmax;i++) {
						if(board.getPiece(xmin + i, yO) != null) {
							return false;
						}
					}
					
					// check if piece exists and if its color is opposite to moving piece
					if(dest == null || (dest != null && dest.color != this.color)) {
						return true;
					}
					
					return false;
				}
	
			//moving along diagonal directions
			} else {
				
				double slope = (double)rise/(double)run;
				
				// slope must either be 1 or -1
				if(slope != 1.0 && slope != -1.0) {
					return false;
				}
				
				// vars
				int xlow = xO < xD ? xO : xD;
				int ylow = yO < yD ? yO : yD;
				int xhigh = xO > xD ? xO : xD;
				int yhigh = yO > yD ? yO : yD;
				
				// check diagonals accordingly, set diagonals boolean to false if it encounters anything
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
				
				// path to destination is clear
				
				// check for piece at destination
				if(dest != null) {
					if(dest.color == this.color) {
						return false;
					}
				}
				return true;
			}
		}

		/**
		 * canMove() sees if the queen has any further moves
		 * that it can make; used by {@link Board#stalemate(boolean)}.
		 * 
		 * @param board Is the board which the queen uses as a frame of
		 * reference
		 * @return True if any further moves are possible
		 */
		public boolean canMove(Board board){
			
			// a queen can move if any of the 8 spaces around it are not filled

			// cardinal checks
			
			// get coordinates
			int x = this.x;
			int y = this.y;
			
			// cardinal checks
			
			// check up
			if(y + 1 < 8 && (board.getPiece(x, y + 1) == null || (board.getPiece(x, y + 1) != null && board.getPiece(x, y + 1).color != this.color)) ) {
				return true;
			}
			// check right
			if(x + 1 < 8 && (board.getPiece(x + 1, y) == null || (board.getPiece(x + 1, y) != null && board.getPiece(x + 1, y).color != this.color)) ) {
				return true;
			}
			// check down
			if(y - 1 > -1 && (board.getPiece(x, y - 1) == null || (board.getPiece(x, y - 1) != null && board.getPiece(x, y - 1).color != this.color)) ) {
				return true;
			}
			// check left
			if(x - 1 > -1 && (board.getPiece(x - 1, y) == null || (board.getPiece(x - 1, y) != null && board.getPiece(x - 1, y).color != this.color)) ) {
				return true;
			}
			
			// diagonal checks
			
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
		 * 
		 */
		ArrayList<int[]> findDangerZone(Board board){
			
			// vars
			ArrayList<int[]> ret = new ArrayList<int[]>();
			char c = this.color;
			int x = this.x, y = this.y;
			Piece piece;
			
			// diagonal checks

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
					} else if (piece.color != c){
						break;
					}
				}
			}
			
			// cardinal checks
			
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
						break;
					} else if(piece.color != c){
						break;
					}
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
						break;
					} else if(piece.color != c){
						break;
					}
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
						break;
					} else if(piece.color != c){
						break;
					}
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
						break;
					} else if(piece.color != c){
						break;
					}
				}
			}
			
			return ret;
		}
}
