package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.R;

/**
 * Knight inherits from Piece. Moves to 8
 * possible spots, skipping over pieces.
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 *
 */

public class Knight extends Piece{
	
	/**
	 * Knight takes what the same input as a Piece
	 * 
	 * @param x File of knight
	 * @param y Rank of knight
	 * @param side Color of knight
	 * @param t Character 'N' describing piece
	 */
	public Knight(int x, int y, char side, char t){
		super(x, y, side, t);
		if(side == 'w'){
			this.resId = R.drawable.white_knight;
		} else {
			this.resId = R.drawable.black_knight;
		}
	}
	
	/**
	 * checkMove() ensures that the piece can move from the original tile
	 * to the destination tile; knights don't have to worry about checking if the space between
	 * its positions are inhabited
	 * 
	 * @param xO x origin point of reference
	 * @param yO y origin point of reference
	 * @param xD x destination coordinate
	 * @param yD y destination coordinate
	 * @param board Given to see if anything is in the path of a move
	 * @return Whether a piece can make a move to a spot
	 */
	public boolean checkMove(int xO, int yO, int xD, int yD, Board board) {

		//knight moves 2 or 1 spaces up or down, left or right
		//there are 8 possible types of moves
		//we check by seeing if the vertical movement is 1 or 2 and then if the horizontal is 2 or 1 respectively

		//variables for later use
		int yDisplace = Math.abs(yO - yD);
		int xDisplace = Math.abs(xO - xD);

		//just have to check if the piece's vertical and horizontal movements are 2 and 1 or 1 and 2
		return this.isBound(xD, yD) && ((((yDisplace == 1) && 
				(xDisplace == 2)) || ((yDisplace == 2) && 
						(xDisplace == 1))) && 
				(board.getPiece(xD, yD) == null || 
				board.getPiece(xD, yD).color != this.color));

	}
		
	/**
	 * canMove() tells whether a Knight has any remaining moves; it is used by {@link Board#stalemate(boolean)}.
	 * 
	 * @param board Serves as a reference for a Knight.
	 * @return true if a Knight can move; false if not.
	 */
	public boolean canMove(Board board) {

		//there are 8 places to check
		int[][] spot = {{this.x + 1, this.y + 2},//i.e., right 1, left 2 
				{this.x + 1, this.y - 2},
				{this.x - 1, this.y + 2},
				{this.x - 1, this.y - 2},
				{this.x + 2, this.y + 1},
				{this.x + 2, this.y - 1},
				{this.x - 2, this.y + 1},
				{this.x - 2, this.y - 1}};

		for(int i = 0; i < 8; i++){

			if(this.isBound(spot[i][0], spot[i][1]) &&//is on board 
					(board.getPiece(spot[i][0], spot[i][1]) == null //is empty
					|| board.getPiece(spot[i][0], spot[i][1]).color != this.color))//is an enemy piece
				return true;

		}
		//ran out of moves
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

		if (c == 'w') {
			if(this.isBound(x + 1, y + 2)) {
				if(board.getPiece(x + 1, y + 2) == null) {
					board.getTile(x + 1, y + 2).bDanger = true;
				} else {
					if(board.getPiece(x + 1, y + 2).type == 'K' && board.getPiece(x + 1, y + 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 1, y + 2).color == c) {
						board.getTile(x + 1, y + 2).bDanger = true;
					}
				}
			}
			if(this.isBound(x + 1, y - 2)) {
				if(board.getPiece(x + 1, y - 2) == null) {
					board.getTile(x + 1, y - 2).bDanger = true;
				} else {
					if(board.getPiece(x + 1, y - 2).type == 'K' && board.getPiece(x + 1, y - 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 1, y - 2).color == c) {
						board.getTile(x + 1, y - 2).bDanger = true;
					}
				}
			}
			if(this.isBound(x - 1, y + 2)) {
				if(board.getPiece(x - 1, y + 2) == null) {
					board.getTile(x - 1, y + 2).bDanger = true;
				} else {
					if(board.getPiece(x - 1, y + 2).type == 'K' && board.getPiece(x - 1, y + 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 1, y + 2).color == c) {
						board.getTile(x - 1, y + 2).bDanger = true;
					}
				}
			}
			if(this.isBound(x - 1, y - 2)) {
				if(board.getPiece(x - 1, y - 2) == null) {
					board.getTile(x - 1, y - 2).bDanger = true;
				} else {
					if(board.getPiece(x - 1, y - 2).type == 'K' && board.getPiece(x - 1, y - 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 1, y - 2).color == c) {
						board.getTile(x - 1, y - 2).bDanger = true;
					}
				}
			}
			if(this.isBound(x + 2, y + 1)) {
				if(board.getPiece(x + 2, y + 1) == null) {
					board.getTile(x + 2, y + 1).bDanger = true;
				} else {
					if(board.getPiece(x + 2, y + 1).type == 'K' && board.getPiece(x + 2, y + 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 2, y + 1).color == c) {
						board.getTile(x + 2, y + 1).bDanger = true;
					}
				}
			}
			if(this.isBound(x + 2, y - 1)) {
				if(board.getPiece(x + 2, y - 1) == null) {
					board.getTile(x + 2, y - 1).bDanger = true;
				} else {
					if(board.getPiece(x + 2, y - 1).type == 'K' && board.getPiece(x + 2, y - 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 2, y - 1).color == c) {
						board.getTile(x + 2, y - 1).bDanger = true;
					}
				}
			}
			if(this.isBound(x - 2, y + 1)) {
				if(board.getPiece(x - 2, y + 1) == null) {
					board.getTile(x - 2, y + 1).bDanger = true;
				} else {
					if(board.getPiece(x - 2, y + 1).type == 'K' && board.getPiece(x - 2, y + 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 2, y + 1).color == c) {
						board.getTile(x - 2, y + 1).bDanger = true;
					}
				}
			}
			if(this.isBound(x - 2, y - 1)) {
				if(board.getPiece(x - 2, y - 1) == null) {
					board.getTile(x - 2, y - 1).bDanger = true;
				} else {
					if(board.getPiece(x - 2, y - 1).type == 'K' && board.getPiece(x - 2, y - 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 2, y - 1).color == c) {
						board.getTile(x - 2, y - 1).bDanger = true;
					}
				}
			}
		} else {
			if(this.isBound(x + 1, y + 2)) {
				if(board.getPiece(x + 1, y + 2) == null) {
					board.getTile(x + 1, y + 2).wDanger = true;
				} else {
					if(board.getPiece(x + 1, y + 2).type == 'K' && board.getPiece(x + 1, y + 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 1, y + 2).color == c) {
						board.getTile(x + 1, y + 2).wDanger = true;
					}
				}
			}
			if(this.isBound(x + 1, y - 2)) {
				if(board.getPiece(x + 1, y - 2) == null) {
					board.getTile(x + 1, y - 2).wDanger = true;
				} else {
					if(board.getPiece(x + 1, y - 2).type == 'K' && board.getPiece(x + 1, y - 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 1, y - 2).color == c) {
						board.getTile(x + 1, y - 2).wDanger = true;
					}
				}
			}
			if(this.isBound(x - 1, y + 2)) {
				if(board.getPiece(x - 1, y + 2) == null) {
					board.getTile(x - 1, y + 2).wDanger = true;
				} else {
					if(board.getPiece(x - 1, y + 2).type == 'K' && board.getPiece(x - 1, y + 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 1, y + 2).color == c) {
						board.getTile(x - 1, y + 2).wDanger = true;
					}
				}
			}
			if(this.isBound(x - 1, y - 2)) {
				if(board.getPiece(x - 1, y - 2) == null) {
					board.getTile(x - 1, y - 2).wDanger = true;
				} else {
					if(board.getPiece(x - 1, y - 2).type == 'K' && board.getPiece(x - 1, y - 2).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 1, y - 2).color == c) {
						board.getTile(x - 1, y - 2).wDanger = true;
					}
				}
			}
			if(this.isBound(x + 2, y + 1)) {
				if(board.getPiece(x + 2, y + 1) == null) {
					board.getTile(x + 2, y + 1).wDanger = true;
				} else {
					if(board.getPiece(x + 2, y + 1).type == 'K' && board.getPiece(x + 2, y + 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 2, y + 1).color == c) {
						board.getTile(x + 2, y + 1).wDanger = true;
					}
				}
			}
			if(this.isBound(x + 2, y - 1)) {
				if(board.getPiece(x + 2, y - 1) == null) {
					board.getTile(x + 2, y - 1).wDanger = true;
				} else {
					if(board.getPiece(x + 2, y - 1).type == 'K' && board.getPiece(x + 2, y - 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x + 2, y - 1).color == c) {
						board.getTile(x + 2, y - 1).wDanger = true;
					}
				}
			}
			if(this.isBound(x - 2, y + 1)) {
				if(board.getPiece(x - 2, y + 1) == null) {
					board.getTile(x - 2, y + 1).wDanger = true;
				} else {
					if(board.getPiece(x - 2, y + 1).type == 'K' && board.getPiece(x - 2, y + 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 2, y + 1).color == c) {
						board.getTile(x - 2, y + 1).wDanger = true;
					}
				}
			}
			if(this.isBound(x - 2, y - 1)) {
				if(board.getPiece(x - 2, y - 1) == null) {
					board.getTile(x - 2, y - 1).wDanger = true;
				} else {
					if(board.getPiece(x - 2, y - 1).type == 'K' && board.getPiece(x - 2, y - 1).color != c) {
						ret.add(new int[] {x, y});
					} else if(board.getPiece(x - 2, y - 1).color == c) {
						board.getTile(x - 2, y - 1).wDanger = true;
					}
				}
			}
		}
		
		return ret;
	}
	
}
