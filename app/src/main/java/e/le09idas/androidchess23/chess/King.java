package e.le09idas.androidchess23.chess;

import java.util.ArrayList;

import e.le09idas.androidchess23.Game;
import e.le09idas.androidchess23.NewGame;
import e.le09idas.androidchess23.R;

/**
 * King inherits from Piece. It also keeps track of whether 
 * it is in check and whether it has moved. It also contains 
 * a move to castle when certain conditions are met.
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */

public class King extends Piece{
	
	boolean inCheck = false;
	boolean hasMoved = false;
	int[][] cP = {{2,0}, {6, 0}, {2, 7}, {6, 7}};//The castling spots, c1, g1, c8, g8
	int[] cPaths = {2, 3, 5, 6};//the files which kings have to pass
	
	/**
	 * King takes in the same input as any Piece.
	 * 
	 * @param x x coordinate of piece
	 * @param y y coordinate of piece
	 * @param c Color of king
	 * @param t Characte for piece
	 */
	public King(int x, int y, char c, char t){
		
		super(x, y, c, t);
		if(c == 'w'){
			this.resId = R.drawable.white_king;
		} else {
			this.resId = R.drawable.black_king;
		}
	}
	
	/**
	 * checkMove() sees whether the King can move in its 3X3 square perimeter; 
	 * it also can castle if special conditions are met using {@link #checkCastle(int, int, Board)}.
	 */
	public boolean checkMove(int xO, int yO, int xD, int yD, Board board) {

		//king can move omni-directionally 1
		//king not only has to consider collisions with pieces but also danger zones
		// if any of the destinations move the king to a castle spot
		if(((xD == cP[0][0] && yD == cP[0][1]) ||
		   (xD == cP[1][0] && yD == cP[1][1]) ||
		   (xD == cP[2][0] && yD == cP[2][1]) ||
		   (xD == cP[3][0] && yD == cP[3][1]))){
			if(checkCastle(xD, yD, board)){//if one of the castling positions are requested; this is called
				System.out.println("This move was a castling move");
				return true;
			}
		}
				
		
		if(Math.abs(xO - xD) > 1 || Math.abs(yO - yD) > 1)
			return false;
		if(this.color == 'w'){
			
			if((this.isBound(xD, yD) && //on board
				!board.getTile(xD, yD).wDanger) && //is NOT in danger
				((board.getPiece(xD, yD) == null || //is empty OR
				(board.getPiece(xD,  yD).color != 'w')))){// is a black piece
				if(!hasMoved)
					hasMoved = !hasMoved;
				return true;
			}
		}else{
			
			if((this.isBound(xD, yD) &&  
			   !board.getTile(xD, yD).bDanger) && 
				((board.getPiece(xD, yD) == null || 
				(board.getPiece(xD,  yD).color != 'b')))){
				if(!hasMoved)
					hasMoved = !hasMoved;
				return true;
			}
			
		}
		return false;
		
	}
	
	/**
	 * 
	 */
	public boolean canMove(Board board) {
		
		int[][] spot = {{this.x, this.y + 1},//i.e., right 1, left 1 
				{this.x, this.y - 1},
				{this.x + 1, this.y},
				{this.x - 1, this.y},
				{this.x + 1, this.y + 1},
				{this.x + 1, this.y - 1},
				{this.x - 1, this.y + 1},
				{this.x - 1, this.y - 1}};
		
		if(this.color == 'w')//for white
			for(int i = 0; i < 8; i++)
				if((this.isBound(spot[i][0], spot[i][1])&& //tile is on board
						!board.getTile(spot[i][0], spot[i][1]).wDanger) && //tile isn't dangerous to king
						((board.getPiece(spot[i][0], spot[i][1]) == null || //spot is empty OR
						(board.getPiece(spot[i][0], spot[i][1]).color != 'w'))))//spot has enemy piece
					return true;
		if(this.color == 'b')//for black
			for(int i = 0; i < 8; i++)
				if((this.isBound(spot[i][0], spot[i][1]) &&
						!board.getTile(spot[i][0], spot[i][1]).bDanger) && 
						((board.getPiece(spot[i][0], spot[i][1]) == null || 
						(board.getPiece(spot[i][0], spot[i][1]).color != 'b'))))
					return true;
			
		return false;
	}
		
	/**
	 * checkCastle() sees whether the conditions are met to castle; if they are, the function 
	 * moves the Rook to its position; the king then gets moved by {@link NewGame#move(int, int, int, int)}
 	 *
	 * @param x The xCoord of the possible rook
	 * @param y The yCoord of the possible rook
	 * @param board A reference object for the king and allows it to move the rook
	 * @return true when successful
	 */
	boolean checkCastle(int x, int y, Board board){
		NewGame.special = -1;
		Game.special = -1;
		if(this.hasMoved  || this.inCheck) {//make sure the king hasn't moved

			return false;
		}

		
		int rX = 0;
		int rY = 0;
		if(x == cP[0][0] && y == cP[0][1]){//c1
			rX = 0; rY = 0;
		}else if(x == cP[1][0] && y == cP[1][1]){//g1
				rX = 7; rY = 0;
		}else if(x == cP[2][0] && y == cP[2][1]){//c8
					rX = 0; rY = 7;
		}else if(x == cP[3][0] && y == cP[3][1]){//g8
					rX = 7; rY = 7;
		}else{

			return false;//can't castle
		}
		
		Piece rook = board.getPiece(rX, rY);//possible rook gotten
		
		if(rook == null) {//checks if it is a piece
			;
				return false;
		}
		
		Rook cRook = (Rook)rook;//cast as a rook
		
		if(cRook.hasMoved || cRook.color != this.color) {//check to see if the rook has moved and is the same color

			return false;
		}

		
		if(this.color == 'w'){//white castling
			
			if(x == cP[0][0] && y == cP[0][1]){//queen side
			
				if(rook.x == 0 && rook.y == 0){
					
					if(board.getPiece(cPaths[0], this.y) != null || board.board[this.y][cPaths[0]].wDanger) {//checks c1
						Game.special = -1;
						NewGame.special = -1;
						return false;
					}
					
					if(board.getPiece(cPaths[1], this.y) != null || board.board[this.y][cPaths[1]].wDanger) {//checks d1
						Game.special = -1;
						NewGame.special = -1;
						return false;
					}
				}
				Game.special = 1;
				NewGame.special = 1;
				NewGame.move(0, 0, x + 1, y);
				Game.move(0, 0, x + 1, y);

				return true;

			}
			
			if(x == cP[1][0] && y == cP[1][1]){//king side
			
				if(rook.x == 7 && rook.y == 0){
					
					if(board.getPiece(cPaths[2], this.y) != null || board.board[this.y][cPaths[2]].wDanger) {//checks f1
						Game.special = -1;
						NewGame.special = -1;
						return false;
					}
					if(board.getPiece(cPaths[3], this.y) != null || board.board[this.y][cPaths[3]].wDanger) {//checks g1
						Game.special = -1;
						NewGame.special = -1;
						return false;
					}
				}
				System.out.println("Moving white rook");
				Game.special = 1;
				NewGame.special = 1;
				NewGame.move(7, 0, x - 1, y);
				Game.move(7, 0, x - 1, y);

				return true;
			}
		
		}else{
			if(this.color == 'b'){//black castling
				if(x == cP[2][0] && y == cP[2][1]){//queen side
				
					if(rook.x == 0 && rook.y == 7){
						
						if(board.getPiece(cPaths[0], this.y) != null || board.board[this.y][cPaths[0]].bDanger) {//checks c8
							Game.special = -1;
							NewGame.special = -11;
							return false;
						}

						
						if(board.getPiece(cPaths[1], this.y) != null || board.board[this.y][cPaths[1]].bDanger) {//checks d8
							NewGame.special = -1;
							Game.special = -1;
							return false;
						}
						
					}
					Game.special = 1;
					NewGame.special = 1;
					NewGame.move(0, 7, x + 1, y);
					Game.move(0, 7, x + 1, y);

					return true;
				}
						
				if(x == cP[3][0] && y == cP[3][1]){//king side
				
					if(rook.x == 7 && rook.y == 7){
						
						if(board.getPiece(cPaths[2], this.y) != null || board.board[this.y][cPaths[2]].bDanger) {//checks f8
							return false;
						}
						
						if(board.getPiece(cPaths[3], this.y) != null || board.board[this.y][cPaths[3]].bDanger) {//checks g8

							return false;
						}
						
					}
					Game.special = 1;
					NewGame.special = 1;
					NewGame.move(7, 7, x - 1, y);//moves rook
					Game.move(7, 7, x - 1, y);

					return true;
				}
			}
		}
		return false;
		
	}
	
	/**
	 * Returns a path to the opposite color's king if it is within the piece's
	 * current line of sight. In the king's case, this should always return as
	 * an empty list since it is illegal to move a king into another king's 
	 * attacking line of sight.
	 * 
	 * @param	board	the current board configuration
	 * 
	 * @return	an ArrayList containing the path to the opposite king 
	 */
	ArrayList<int[]> findDangerZone(Board board){
		
		// the king should always return an empty list, since moving the opposite king into check is illegal
		ArrayList<int[]> ret = new ArrayList<int[]>();
		char c = this.color;
		int x = this.x, y = this.y;
		
		// white side
		if(c == 'w') {
			
			// up right
			if(this.isBound(x + 1, y + 1)) {
				if(board.getPiece(x + 1, y + 1) == null || (board.getPiece(x + 1, y + 1) != null && board.getPiece(x + 1, y + 1).color == c)) {
					board.getTile(x + 1, y + 1).bDanger = true;
				}
			}
			// right
			if(this.isBound(x + 1, y)) {
				if(board.getPiece(x + 1, y) == null || (board.getPiece(x + 1, y) != null && board.getPiece(x + 1, y).color == c)) {
					board.getTile(x + 1, y).bDanger = true;
				}
			}
			// down right
			if(this.isBound(x + 1, y - 1)) {
				if(board.getPiece(x + 1, y - 1) == null || (board.getPiece(x + 1, y - 1) != null && board.getPiece(x + 1, y - 1).color == c)) {
					board.getTile(x + 1, y - 1).bDanger = true;
				}
			}
			// down
			if(this.isBound(x, y - 1)) {
				if(board.getPiece(x, y - 1) == null || (board.getPiece(x, y - 1) != null && board.getPiece(x, y - 1).color == c)) {
					board.getTile(x, y - 1).bDanger = true;
				}
			}
			// down left
			if(this.isBound(x - 1, y - 1)) {
				if(board.getPiece(x - 1, y - 1) == null || (board.getPiece(x - 1, y - 1) != null && board.getPiece(x - 1, y - 1).color == c)) {
					board.getTile(x - 1, y - 1).bDanger = true;
				}
			}
			// left
			if(this.isBound(x - 1, y)) {
				if(board.getPiece(x - 1, y) == null || (board.getPiece(x - 1, y) != null && board.getPiece(x - 1, y).color == c)) {
					board.getTile(x - 1, y).bDanger = true;
				}
			}
			// up left
			if(this.isBound(x - 1, y + 1)) {
				if(board.getPiece(x - 1, y + 1) == null || (board.getPiece(x - 1, y + 1) != null && board.getPiece(x - 1, y + 1).color == c)) {
					board.getTile(x - 1, y + 1).bDanger = true;
				}
			}
			// up
			if(this.isBound(x, y + 1)) {
				if(board.getPiece(x, y + 1) == null || (board.getPiece(x, y + 1) != null && board.getPiece(x, y + 1).color == c)) {
					board.getTile(x, y + 1).bDanger = true;
				}
			}
			
		// black side
		} else {
			
			// up right
			if(this.isBound(x + 1, y + 1)) {
				if(board.getPiece(x + 1, y + 1) == null || (board.getPiece(x + 1, y + 1) != null && board.getPiece(x + 1, y + 1).color == c)) {
					board.getTile(x + 1, y + 1).wDanger = true;
				}
			}
			// right
			if(this.isBound(x + 1, y)) {
				if(board.getPiece(x + 1, y) == null || (board.getPiece(x + 1, y) != null && board.getPiece(x + 1, y).color == c)) {
					board.getTile(x + 1, y).wDanger = true;
				}
			}
			// down right
			if(this.isBound(x + 1, y - 1)) {
				if(board.getPiece(x + 1, y - 1) == null || (board.getPiece(x + 1, y - 1) != null && board.getPiece(x + 1, y - 1).color == c)) {
					board.getTile(x + 1, y - 1).wDanger = true;
				}
			}
			// down
			if(this.isBound(x, y - 1)) {
				if(board.getPiece(x, y - 1) == null || (board.getPiece(x, y - 1) != null && board.getPiece(x, y - 1).color == c)) {
					board.getTile(x, y - 1).wDanger = true;
				}
			}
			// down left
			if(this.isBound(x - 1, y - 1)) {
				if(board.getPiece(x - 1, y - 1) == null || (board.getPiece(x - 1, y - 1) != null && board.getPiece(x - 1, y - 1).color == c)) {
					board.getTile(x - 1, y - 1).wDanger = true;
				}
			}
			// left
			if(this.isBound(x - 1, y)) {
				if(board.getPiece(x - 1, y) == null || (board.getPiece(x - 1, y) != null && board.getPiece(x - 1, y).color == c)) {
					board.getTile(x - 1, y).wDanger = true;
				}
			}
			// up left
			if(this.isBound(x - 1, y + 1)) {
				if(board.getPiece(x - 1, y + 1) == null || (board.getPiece(x - 1, y + 1) != null && board.getPiece(x - 1, y + 1).color == c)) {
					board.getTile(x - 1, y + 1).wDanger = true;
				}
			}
			// up
			if(this.isBound(x, y + 1)) {
				if(board.getPiece(x, y + 1) == null || (board.getPiece(x, y + 1) != null && board.getPiece(x, y + 1).color == c)) {
					board.getTile(x, y + 1).wDanger = true;
				}
			}
			
		}
				
		return ret;
	}

}
