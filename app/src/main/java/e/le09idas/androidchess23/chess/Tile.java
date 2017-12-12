package e.le09idas.androidchess23.chess;
/**
 * These compose the board; they hold a Piece
 * They record regions where kings can't move,
 * the character to display for the tile,
 * and records what color square.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */

public class Tile {
	
	public Piece inhabitant;//piece on tile
	public boolean bDanger;//black king can't go here
	public boolean wDanger;//white king can't go here
	boolean color;//b -> black tile; w -> white tile
	
	String symbol;
	
	/**
	 * Tiles are made with a piece (can be null), whether a Tile is dangerous to white or black 
	 * king, a boolean for its color and a string for its symbol
	 * 
	 * @param inhab Piece at Tile
	 * @param bDang Black king danger zone
	 * @param wDang White king danger zone
	 * @param col Color of tile
	 * @param sym Symbol that prints: '## ' for black, '   ' for white, or a to toString of a Piece 
	 * used by {@link #toString()}
	 */
	public Tile(Piece inhab, boolean bDang, boolean wDang, boolean col, String sym){
	
		this.inhabitant = inhab;
		this.bDanger = bDang;
		this.wDanger = wDang;
		this.color = col;
		this.symbol = sym;
		
	}
	
	/**
	 * toString() returns the current symbol to display at the Tile
	 *  @return Current symbol of tile
	 */
	public String toString(){
		return this.symbol;
	}
	
	/**
	 * resymbol() changes tiles' symbol to represent inhabiting piece or tile color if empty
	 */
	public void resymbol(){
		if(this.inhabitant == null){
			if(color){
				this.symbol = "   ";//empyt white Tile
			}else{
				this.symbol = "## ";//empty black Tile
			}
		}else{
			this.symbol = this.inhabitant.toString();
		}
	}
}
