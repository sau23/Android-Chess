package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * Pawn inherits attributes from Piece and includes its own functions
 * for moving and determining its danger zones. It also includes its
 * own boolean that records whether or not the pawn is currently
 * spanning over 2 spaces (en passant).
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */

public class Pawn extends Piece {

	// determines if a pawn is currently spanning over 2 spaces
	boolean enPassant;
	
	/**
	 * Pawn constructor. This also initializes its enPassant
	 * boolean to be false.
	 * 
	 * @param	x	The starting x position
	 * @param	y	The starting y position
	 * @param	c	The color expressed as 'b' for black or 'w' for white
	 * @param	t	The type expressed as 'p' for pawn
	 * 
	 * @return	a new pawn piece
	 */
	
	public Pawn(int x, int y, char side, char type){
		super(x, y, side, type);
		if(side == 'w'){
			this.resId = R.drawable.white_pawn;
		} else {
			this.resId = R.drawable.black_pawn;
		}
		enPassant = false;
	}

	/**
	 * Returns a boolean value based on whether or not the pawn can move
	 * to the given input coordinates and board configuration.
	 * The input destination coordinate pair is guaranteed to be on the board.
	 * 
	 * @param	xO		the original x position
	 * @param	yO		the original y position
	 * @param	xD		the destination x position
	 * @param	yD		the destination y position
	 * @param	board	the current board configuration
	 * 
	 * @return	whether the pawn can move to a given destination
	 */
	
	public boolean checkMove(int xO, int yO, int xD, int yD, Board board){

		// first, get color of the piece
		char color = this.color;

		// depending on what color the pawn is, you're only allowed to move a certain direction

		// variables for later calculations
		int rise = yD - yO;
		int run = xD - xO;
		Piece dest = board.getPiece(xD, yD);
		boolean hasPiece;

		// white side pawns
		if(color == 'w') {

			// solely vertical movement
			if(run == 0) {

				// check for obstruction at destination
				hasPiece = dest != null;

				// check if on rank 2, moving 2 spaces, no enemy at midstep, no enemy at destination
				if(yO == 1 && rise == 2 && board.getPiece(xO, yO + 1) == null && !hasPiece) {
					
					// set en passant
					this.enPassant = true;
					return true;
				}

				// check if moving 1 space, no enemy at destination
				if(rise == 1 && !hasPiece) {
					return true;
				}

				// attempting to move some other distance not 1 or 2 spaces ahead
				return false;

			} else {

				// otherwise, calculate slope for diagonal movement
				double slope = (double)rise/(double)run;

				// spaces diagonally by 1 space and containing enemy
				boolean properDistance;

				// find the distance of the slope
				if(slope == 1.0) {
					properDistance = yD == yO + 1 && xD == xO + 1;
				} else if (slope == -1.0){
					properDistance = yD == yO + 1 && xD == xO - 1;
				} else {

					// if the slope is neither -1 or 1 at this point, you know you can't move there
					return false;
				}

				// check if there is an enemy that the pawn can capture at the destination
				hasPiece = dest != null && dest.color == 'b';
				if(properDistance && hasPiece) {
					return true;
				}
				
				// after movement calculations, you can also check for possible en passant captures
				if(properDistance && board.getPiece(xD, yD) == null && board.getPiece(xD, yD - 1) != null && board.getPiece(xD, yD - 1).type == 'p') {
					Pawn pawn = (Pawn)board.getPiece(xD, yD - 1);
					if(pawn.enPassant) {
						
						// move a guaranteed blank space over the pawn to be captured
						Chess.move(xD, yD, xD, yD - 1);
						return true;
					}
				}

				// the white pawn has nowhere else to check
				return false;
			}

		// black side pawns
		} else {

			// solely vertical movement
			if(run == 0) {

				// check for obstruction at destination
				hasPiece = dest != null;

				// check if on rank 7, moving 2 spaces, no enemy at midstep, no enemy at destination
				if(yO == 6 && rise == -2 && board.getPiece(xO, yO - 1) == null && !hasPiece) {
					
					// set en passant flag
					this.enPassant = true;
					return true;
				}

				// check if moving 1 space, no enemy at destination
				if(rise == -1 && !hasPiece) {
					return true;
				}

				// attempting to move some other distance not 1 or 2 spaces ahead
				return false;

			} else {

				// otherwise, calculate slope for diagonal movement
				double slope = (double)rise/(double)run;

				// spaces diagonally by 1 space and containing enemy
				boolean properDistance;

				// find the distance of the slope
				if(slope == 1.0) {
					properDistance = yD == yO - 1 && xD == xO - 1;
				} else if (slope == -1.0){
					properDistance = yD == yO - 1 && xD == xO + 1;
				} else {

					// if the slope is neither -1 or 1 at this point, you know you can't move there
					return false;
				}

				// check if there is an enemy that the pawn can capture at the destination
				hasPiece = dest != null && dest.color == 'w';
				if(properDistance && hasPiece) {
					return true;
				}

				// after movement calculations, you can also check for possible en passant captures
				if(properDistance && board.getPiece(xD, yD) == null && board.getPiece(xD, yD + 1) != null && board.getPiece(xD, yD + 1).type == 'p') {
					Pawn pawn = (Pawn)board.getPiece(xD, yD + 1);
					if(pawn.enPassant) {
						
						// move a guaranteed blank space over the pawn to be captured
						Chess.move(xD, yD, xD, yD + 1);
						return true;
					}
				}
				
				// the black pawn has nowhere else to check
				return false;
			}
		}
	}

	/**
	 * Returns a boolean value based on whether or not the pawn can make
	 * any move. This checks to see if there is a piece ahead, or 2 ahead
	 * for en passant, if there are opponent pieces diagonally away
	 * based on the direction the pawn is facing, and if the pawn is at an
	 * edge of the board.
	 * 
	 * @param	board	the current board configuration
	 * 
	 * @return	whether the pawn can move at all
	 */
	
	boolean canMove(Board board){

		// first, get color of the piece
		char color = this.color;

		// depending on what color the pawn is, check certain directions

		// white side pawns
		if(color == 'w') {

			// if pawn is in last row possible (should be promoted)
			if(this.y == 7) {
				return false;
			}

			// spaces ahead will always be checked
			boolean ahead = board.getPiece(this.x, this.y + 1) == null;
			boolean upright, upleft;

			// if on rank 2
			if(this.y == 1) {

				// check 2 spaces ahead as well as normal checks
				boolean ahead2 = board.getPiece(this.x, this.y + 2) == null && ahead;

				// if upleft leads to out of left bounds, only calculate ahead, ahead2 and upright
				if(this.x - 1 == -1) {
					upright = board.getPiece(this.x + 1, this.y + 1) != null && board.getPiece(this.x + 1, this.y + 1).color == 'b';
					return ahead || ahead2 || upright;
				}

				// if upright leads to out of right bounds, only calculate ahead, ahead2, and upleft
				if(this.x + 1 == 8) {
					upleft = board.getPiece(this.x - 1, this.y + 1) != null && board.getPiece(this.x - 1, this.y + 1).color == 'b';
					return ahead || ahead2 || upleft;
				}

				// otherwise, it must be one of the center ones
				upright = board.getPiece(this.x + 1, this.y + 1) != null && board.getPiece(this.x + 1, this.y + 1).color == 'b';
				upleft = board.getPiece(this.x - 1, this.y + 1) != null && board.getPiece(this.x - 1, this.y + 1).color == 'b';
				return ahead || ahead2 || upright || upleft;

			}
			// otherwise, pawn is on a different rank, dont need to check 2 spaces ahead

			// if upleft leads to out of left bounds, only calculate ahead and upright
			if(this.x - 1 == -1) {
				upright = board.getPiece(this.x + 1, this.y + 1) != null && board.getPiece(this.x + 1, this.y + 1).color == 'b';
				return ahead || upright;
			}

			// if upright leads to out of right bounds, only calculate ahead and upleft
			if(this.x + 1 == 8) {
				upleft = board.getPiece(this.x - 1, this.y + 1) != null && board.getPiece(this.x - 1, this.y + 1).color == 'b';
				return ahead || upleft;
			}

			// otherwise, it must be one of the center ones
			upright = board.getPiece(this.x + 1, this.y + 1) != null && board.getPiece(this.x + 1, this.y + 1).color == 'b';
			upleft = board.getPiece(this.x - 1, this.y + 1) != null && board.getPiece(this.x - 1, this.y + 1).color == 'b';
			return ahead || upright || upleft;			

		// black side pawns
		} else {

			// if pawn is in last row possible (should be promoted)
			if(this.y == 0) {
				return false;
			}

			// spaces ahead will always be checked
			boolean ahead = board.getPiece(this.x, this.y - 1) == null;
			boolean downright, downleft;

			// if on rank 7
			if(this.y == 6) {

				// check 2 spaces ahead as well as normal checks
				boolean ahead2 = board.getPiece(this.x, this.y - 2) == null && ahead;

				// if downleft leads to out of left bounds, only calculate ahead, ahead2 and downright
				if(this.x - 1 == -1) {
					downright = board.getPiece(this.x + 1, this.y - 1) != null && board.getPiece(this.x + 1, this.y - 1).color == 'w';
					return ahead || ahead2 || downright;
				}

				// if downright leads to out of right bounds, only calculate ahead, ahead2, and downleft
				if(this.x + 1 == 8) {
					downleft = board.getPiece(this.x - 1, this.y - 1) != null && board.getPiece(this.x - 1, this.y - 1).color == 'w';
					return ahead || ahead2 || downleft;
				}

				// otherwise, it must be one of the center ones
				downright = board.getPiece(this.x + 1, this.y - 1) != null && board.getPiece(this.x + 1, this.y - 1).color == 'w';
				downleft = board.getPiece(this.x - 1, this.y - 1) != null && board.getPiece(this.x - 1, this.y - 1).color == 'w';
				return ahead || ahead2 || downright || downleft;

			}
			// otherwise, pawn is on a different rank, dont need to check 2 spaces ahead

			// if downleft leads to out of left bounds, only calculate ahead and downight
			if(this.x - 1 == -1) {		
				downright = board.getPiece(this.x + 1, this.y - 1) != null && board.getPiece(this.x + 1, this.y - 1).color == 'w';
				return ahead || downright;
			}

			// if downright leads to out of right bounds, only calculate ahead and downleft
			if(this.x + 1 == 8) {
				downleft = board.getPiece(this.x - 1, this.y - 1) != null && board.getPiece(this.x - 1, this.y - 1).color == 'w';
				return ahead || downleft;
			}

			// otherwise, it must be one of the center ones
			downright = board.getPiece(this.x + 1, this.y - 1) != null && board.getPiece(this.x + 1, this.y - 1).color == 'w';
			downleft = board.getPiece(this.x - 1, this.y - 1) != null && board.getPiece(this.x - 1, this.y - 1).color == 'w';
			return ahead || downright || downleft;			
		}
	}
		
	/**
	 * Promotes a pawn at its given location into the input type. If no
	 * input is given, this will default to promoting the pawn into a
	 * queen. Promotion also updates the pawn roster to reference the
	 * new piece.
	 * 
	 * @param	type	the type to promote the pawn to
	 * @param	board	the current board configuration
	 */

	void promote(int type, Board board) {

		Piece promotion;
		switch (type) {
		case 'B':
			promotion = new Bishop(this.x, this.y, this.color, 'B');
			break;
		case 'N':
			promotion = new Knight(this.x, this.y, this.color, 'N');
			break;
		default:
		case 'Q':
			promotion = new Queen(this.x, this.y, this.color, 'Q');
			break;
		case 'R':
			promotion = new Rook(this.x, this.y, this.color, 'R');
			break;
		}
		
		// search the pawn roster for the board to see which piece was promoted and update it
		if(this.color == 'w') {
			for(int i = 0;i < 8;i++) {
				
				// check to see if the roster is referencing this instance of pawn
				if(board.wPieces[1][i] == this) {
					board.wPieces[1][i] = promotion;
					break;
				}
			}
		} else {
			for(int i = 0;i < 8;i++) {
				
				// check to see if the roster is referencing this instance of pawn
				if(board.bPieces[1][i] == this) {
					board.bPieces[1][i] = promotion;
					break;
				}
			}
		}
		
		board.getTile(this.x, this.y).inhabitant = promotion;
	}
	
	/**
	 * Checks if a pawn can be promoted based on its location and color.
	 * 
	 * @return whether a piece can be promoted
	 */

	boolean canPromote() {
		
		if(this.y == 7 && this.color == 'w') {
			return true;
		}
		
		if(this.y == 0 && this.color == 'b') {
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
		int i = c == 'w' ? 1 : -1;
		
		// left side
		if(this.x - 1 == -1) {
			
			// check if the space contains anything
			if((board.getPiece(this.x + 1, this.y + i) == null)) {
				if (c == 'w') {
					board.getTile(this.x + 1, this.y + i).bDanger = true;
				} else {
					board.getTile(this.x + 1, this.y + i).wDanger = true;
				}
				
			// space contains something, check if it a king of opposite color
			} else {
				if(board.getPiece(this.x + 1, this.y + i).type == 'K'&& board.getPiece(this.x + 1, this.y + i).color != c) {
					ret.add(new int[] {this.x, this.y});
					
				// or if its a piece of the same color
				} else if(board.getPiece(this.x + 1, this.y + i).color == c) {
					if (c == 'w') {
						board.getTile(this.x + 1, this.y + i).bDanger = true;
					} else {
						board.getTile(this.x + 1, this.y + i).wDanger = true;
					}
				}
			}

		// right side
		} else if(this.x + 1 == 8) {
			
			// check if the space contains anything
			if((board.getPiece(this.x - 1, this.y + i) == null)) {
				if (c == 'w') {
					board.getTile(this.x - 1, this.y + i).bDanger = true;
				} else {
					board.getTile(this.x - 1, this.y + i).wDanger = true;
				}
				
			// space contains something, check if it a king of opposite color
			} else {
				if(board.getPiece(this.x - 1, this.y + i).type == 'K'&& board.getPiece(this.x - 1, this.y + i).color != c) {
					ret.add(new int[] {this.x, this.y});
					
				// or if its a piece of the same color
				} else if(board.getPiece(this.x - 1, this.y + i).color == c) {
					if (c == 'w') {
						board.getTile(this.x - 1, this.y + i).bDanger = true;
					} else {
						board.getTile(this.x - 1, this.y + i).wDanger = true;
					}
				}
			}
		// center
		} else {
			
			// check if the space contains anything
			if((board.getPiece(this.x + 1, this.y + i) == null)) {
				if (c == 'w') {
					board.getTile(this.x + 1, this.y + i).bDanger = true;
				} else {
					board.getTile(this.x + 1, this.y + i).wDanger = true;
				}

			// space contains something, check if it a king of opposite color
			} else {
				if(board.getPiece(this.x + 1, this.y + i).type == 'K'&& board.getPiece(this.x + 1, this.y + i).color != c) {
					ret.add(new int[] {this.x, this.y});
				} else if(board.getPiece(this.x + 1, this.y + i).color == c) {
					if (c == 'w') {
						board.getTile(this.x + 1, this.y + i).bDanger = true;
					} else {
						board.getTile(this.x + 1, this.y + i).wDanger = true;
					}
				}
			}			
			if((board.getPiece(this.x - 1, this.y + i) == null)) {
				if (c == 'w') {
					board.getTile(this.x - 1, this.y + i).bDanger = true;
				} else {
					board.getTile(this.x - 1, this.y + i).wDanger = true;
				}
			} else {
				if(board.getPiece(this.x - 1, this.y + i).type == 'K'&& board.getPiece(this.x - 1, this.y + i).color != c) {
					ret.add(new int[] {this.x, this.y});
				} else if(board.getPiece(this.x - 1, this.y + i).color == c) {
					if (c == 'w') {
						board.getTile(this.x - 1, this.y + i).bDanger = true;
					} else {
						board.getTile(this.x - 1, this.y + i).wDanger = true;
					}
				}
			}

		}
		return ret;
	}
	
}


