package e.le09idas.androidchess23.chess;

import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * The Board class creates an object that holds Pieces;
 * The board is comprised of Tile instances that hold a 
 * piece, and set flags of whether a king can enter a certain space.
 * A roster for each side is kept to make update the board's dangerZones
 * 
 *@author Nicholas Petriello
 *@author Samuel Uganiza
 */
public class Board {

	Tile[][] board = new Tile[8][8];//the board
	public Piece[][] bPieces = new Piece[2][8];//black roster
	public Piece[][] wPieces = new Piece[2][8];//white roster
	boolean wCanMove = true;//sees if white can move
	boolean bCanMove = true;//sees if black can move
	GridLayout cb;
	
	/**
	 * Board constructor using {@link #initPieces()} and {@link #initBoard()}
	 */
	public Board(GridLayout chessboard){
		cb = chessboard;
		this.initPieces();
		this.initBoard();
	}
	
	/**
	 * initPieces() initializes the rosters of each side.
	 */
	public void initPieces(){
		
		//The major black pieces.
		this.bPieces[0][0] = new Rook(7, 0, 'b', 'R');
		this.bPieces[0][1] = new Knight(7, 1, 'b', 'N');
		this.bPieces[0][2] = new Bishop(7, 2, 'b', 'B');
		this.bPieces[0][3] = new Queen(7, 3, 'b', 'Q');
		this.bPieces[0][4] = new King(7, 4, 'b', 'K');
		this.bPieces[0][5] = new Bishop(7, 5, 'b', 'B');
		this.bPieces[0][6] = new Knight(7, 6, 'b', 'N');
		this.bPieces[0][7] = new Rook(7, 7, 'b', 'R');
		
		//The black pawns.
		for(int b = 0; b < 8; b++){
			this.bPieces[1][b] = new Pawn(6, b, 'b', 'p');
		}
		
		//The white pawnss
		for(int w = 0; w < 8; w++){
			this.wPieces[1][w] = new Pawn(1, w, 'w', 'p');
		}
		
		//The major white pieces.
		this.wPieces[0][7] = new Rook(0, 0, 'w', 'R');
		this.wPieces[0][6] = new Knight(0, 1, 'w', 'N');
		this.wPieces[0][5] = new Bishop(0, 2, 'w', 'B');
		this.wPieces[0][4] = new Queen(0, 3, 'w', 'Q');
		this.wPieces[0][3] = new King(0, 4, 'w', 'K');
		this.wPieces[0][2] = new Bishop(0, 5, 'w', 'B');
		this.wPieces[0][1] = new Knight(0, 6, 'w', 'N');
		this.wPieces[0][0] = new Rook(0, 7, 'w', 'R');	
		
	}
	
	/**
	 * initBoard() puts the pieces from the rosters onto the board.
	 */
	void initBoard(){
		
		//the white side being set up
		this.board[7][0] = new Tile(this.bPieces[0][0], false, false, true, this.bPieces[0][0].toString());
		this.board[7][1] = new Tile(this.bPieces[0][1], false, false, false, this.bPieces[0][1].toString());
		this.board[7][2] = new Tile(this.bPieces[0][2], false, false, true, this.bPieces[0][2].toString());
		this.board[7][3] = new Tile(this.bPieces[0][3], false, false, false, this.bPieces[0][3].toString());
		this.board[7][4] = new Tile(this.bPieces[0][4], false, false, true, this.bPieces[0][4].toString());
		this.board[7][5] = new Tile(this.bPieces[0][5], false, false, false, this.bPieces[0][5].toString());
		this.board[7][6] = new Tile(this.bPieces[0][6], false, false, true, this.bPieces[0][6].toString());
		this.board[7][7] = new Tile(this.bPieces[0][7], false, false, false, this.bPieces[0][7].toString());

		boolean whiteBlack = false;
		for(int b = 0; b < 8; b++){//pawns of rank 7

			if(whiteBlack){
				this.board[6][b] = new Tile(this.bPieces[1][b], false, false, true, this.bPieces[1][b].toString());
				whiteBlack = false;
			}else{
				this.board[6][b] = new Tile(this.bPieces[1][b], false, false, false, this.bPieces[1][b].toString());
				whiteBlack = true;
			}

		}
		whiteBlack = true;
		for(int m = 0; m<8; m++){//spaces of rank 6

			if(whiteBlack){					
				this.board[5][m] = new Tile(null, false, true, true, "   ");
				whiteBlack = false;
			}
			else{					
				this.board[5][m] = new Tile(null, false, true, false, "## ");
				whiteBlack = true;
			}
		}
		whiteBlack = false;
		for(int m = 0; m<8; m++){//spaces of rank 5

			if(whiteBlack){					
				this.board[4][m] = new Tile(null, false, false, true, "   ");
				whiteBlack = false;
			}
			else{					
				this.board[4][m] = new Tile(null, false, false, false, "## ");
				whiteBlack = true;
			}
		}
		whiteBlack = true;
		for(int m = 0; m<8; m++){//spaces of rank 4

			if(whiteBlack){					
				this.board[3][m] = new Tile(null, false, false, true, "   ");
				whiteBlack = false;
			}
			else{					
				this.board[3][m] = new Tile(null, false, false, false, "## ");
				whiteBlack = true;
			}
		}
		whiteBlack = false;
		for(int m = 0; m<8; m++){//spaces of rank 3

			if(whiteBlack){					
				this.board[2][m] = new Tile(null, true, false, true, "   ");
				whiteBlack = false;
			}
			else{					
				this.board[2][m] = new Tile(null, true, false, false, "## ");
				whiteBlack = true;
			}
		}
		whiteBlack = true;
		for(int b = 0; b < 8; b++){//pawns of rank 2

			if(!whiteBlack){
				this.board[1][b] = new Tile(this.wPieces[1][b], false, false, false, this.wPieces[1][b].toString());
				whiteBlack = true;
			}else{
				this.board[1][b] = new Tile(this.wPieces[1][b], false, false, true, this.wPieces[1][b].toString());
				whiteBlack = false;
			}
		
			//the white pieces set up	
			this.board[0][0] = new Tile(this.wPieces[0][7], false, false, false, this.wPieces[0][7].toString());
			this.board[0][1] = new Tile(this.wPieces[0][6], false, false, true, this.wPieces[0][6].toString());
			this.board[0][2] = new Tile(this.wPieces[0][5], false, false, false, this.wPieces[0][5].toString());
			this.board[0][3] = new Tile(this.wPieces[0][4], false, false, true, this.wPieces[0][4].toString());	
			this.board[0][4] = new Tile(this.wPieces[0][3], false, false, false, this.wPieces[0][3].toString());
			this.board[0][5] = new Tile(this.wPieces[0][2], false, false, true, this.wPieces[0][2].toString());
			this.board[0][6] = new Tile(this.wPieces[0][1], false, false, false, this.wPieces[0][1].toString());
			this.board[0][7] = new Tile(this.wPieces[0][0], false, false, true, this.wPieces[0][0].toString());
			
		}

		// updates the ui board
		int i = 0;
		for(int y = 7; y > -1; y--) {
			for(int x = 0; x < 8; x++){
				if(board[y][x].inhabitant != null) {
					((ImageButton) cb.getChildAt(i)).setImageResource(board[y][x].inhabitant.getResId());
				}
				i++;
			}
		}
	}
	
	/**
	 * printBoard() prints the board starting with rank 8 and down.
	 */
	public void printBoard(){
		for(int y = 7; y > -1; y--){
			for(int x = 0; x < 8; x++){
				if(this.board[y][x].inhabitant == null){
					if(this.board[y][x].color == true){
						System.out.print("   ");
					}else{	
						System.out.print("## ");
					}
				}else{
					System.out.print(this.board[y][x].inhabitant.toString() + " ");
				}
			}
			System.out.print("\n");//prints the rank
		}
	}
	
	/**
	 * getTile() is a getter for Board.
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return The tile at board[x][y]
	 */
	public Tile getTile(int x, int y) {
		return this.board[y][x];
	}
	
	/**
	 * getPiece() gets a piece from a Tile.
	 * 
	 * @param x x position
	 * @param y y position
	 * @return Piece held by Tile
	 */
	public Piece getPiece(int x, int y) {
		return this.board[y][x].inhabitant;
	}
	
	/**
	 * updatePawns() updates en passant flags for pawns after turn has passed.
	 * 
	 * @param turn Indicates black or white move; true = white's, false = black's
	 */
	public void updatePawns(boolean turn) {
		
		// white's turn
		Piece piece;
		Pawn pawn;
		if(turn) {
			for(int i = 0;i < 8;i++) {
				piece = this.wPieces[1][i];
				if(piece.type == 'p') {
					pawn = (Pawn)piece;
					if(pawn.enPassant) {
						pawn.enPassant = false;
					}
				}
			}
			
		// black's turn
		} else {
			for(int i = 0;i < 8;i++) {
				piece = this.bPieces[1][i];
				if(piece.type == 'p') {
					pawn = (Pawn)piece;
					if(pawn.enPassant) {
						pawn.enPassant = false;
					}
				}
			}			
		}
	}
	
	/**
	 * 
	 * @param turn
	 */
	void clearDangerZones(boolean turn) {
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				if(turn) {
					this.board[i][j].bDanger = false;
				} else {
					this.board[i][j].wDanger = false;
				}
			}
		}
	}

	/**
	 * updateDangerZones() iterates through the opposite turn's roster and updates the board's danger zones by calling their respective functions
	 * returns a path to the opposite king as an array list of int[] if it finds it, null 
	 * 
	 * @param turn Indicates records black or white move
	 * @return A grid of a Board's danger zones
	 */
	
	public ArrayList<int[]> updateDangerZones(boolean turn) {

		clearDangerZones(turn);
		
		ArrayList<int[]> ret = new ArrayList<int[]>();

		// white's turn, update black king's danger zones
		if(turn) {
			for(int i = 0;i < 2;i++) {
				for(int j = 0;j < 8;j++) {
					if(!this.wPieces[i][j].isCaptured) {
						ArrayList<int[]> temp = wPieces[i][j].findDangerZone(this);
						if(!temp.isEmpty()) {
							ret = temp;
						}
					}
				}
			}
			
		// black's turn, update white king's danger zones
		} else {
			for(int i = 0;i < 2;i++) {
				for(int j = 0;j < 8;j++) {
					if(!this.bPieces[i][j].isCaptured) {
						ArrayList<int[]> temp = bPieces[i][j].findDangerZone(this);
						if(!temp.isEmpty()) {
							ret = temp;
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * getRespondent() returns an array list containing all the pieces by location that can reach any checked space
	 * 
	 * @param checker
	 * @param pathToKing
	 * @return
	 */
	public ArrayList<int[]> getRespondants(boolean checker, ArrayList<int[]> pathToKing){
		
		ArrayList<int[]> ret = new ArrayList<int[]>();
		
		// check every space in the path
		for(int i = 0;i < pathToKing.size();i++) {
			
			int[] coords = pathToKing.get(i);
			
			// iterate through side's roster
			for(int j = 0;j < 2;j++) {
				for(int k = 0;k < 8;k++) {
					
					// check white side's pieces
					if(checker) {
						
						// check for uncaptured pieces
						if(!this.bPieces[j][k].isCaptured) {
							Piece piece = bPieces[j][k];
							
							// check to see if the piece can move to a given space
							if(piece.checkMove(piece.x, piece.y, coords[0], coords[1], this)) {
								ret.add(new int[] {piece.x, piece.y});
							}
							
						}
						
					// check black side's pieces
					} else {
						
						// check for uncaptured pieces
						if(!this.wPieces[j][k].isCaptured) {
							Piece piece = wPieces[j][k];
							
							// check to see if the piece can move to a given space
							if(piece.checkMove(piece.x, piece.y, coords[0], coords[1], this)) {
								ret.add(new int[] {piece.x, piece.y});
							}
							
						}
						
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * stalemate() checks all possible moves of each piece for a turn; if
	 * there are not possible moves, there is stale mate
	 * 
	 * @param turn The current color teams turn
	 * @return A boolean on whether a side can move
	 */
	boolean stalemate(boolean turn){
		
		if(turn){//blacks's turn
			for(Piece piece : this.bPieces[0]) {//checks if the black major pieces can move
				if(piece.isCaptured){
					continue;
				}else if(piece.canMove(this)){
					return false;	
				}
			}
			for(Piece piece : this.bPieces[1]){//checks if pawns can move for black
				if(piece.isCaptured){
					continue;
				}else if(piece.canMove(this)){
					return false;
				}
			}
		
		}else{//white's turn
			for(Piece piece : this.wPieces[0]){//major pieces	
				if(piece.isCaptured){
					continue;
				}else if(piece.canMove(this)){
					return false;
				}
			}	
			for(Piece piece : this.wPieces[1]){//pawns

				if(piece.isCaptured){
					continue;
				}else if(piece.canMove(this)){
					return false;
				}
			}	
		}
		return true;
	}
}
