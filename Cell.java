/* Class Cell, used to represent the cells inside a board.  
 * Each cell can contain either a piece or block. Once occupied,
 * the cell is not accessible. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

/** Class Cell, used to represent the cells inside a board.  
 * Each cell can contain either a piece or block. Once occupied,
 * the cell is not accessible. */
public class Cell {
	/* Position from bot-left corner */
	private int xPos;	// col
	private int yPos;	// row
	
	private Piece piece; // null if no piece placed
	private boolean isAccessible;  // not accessible if there is a block or piece

	/** Getters and setters */
	public int getxPos() { return xPos; }
	public void setxPos(int xPos) { this.xPos = xPos; }
	public int getyPos() { return yPos; }
	public void setyPos(int yPos) { this.yPos = yPos; }
	public Piece getPiece() { return piece; }
	public void setPiece(Piece piece) { this.piece = piece; isAccessible = false; }
	public boolean isAccessible() { return isAccessible; }
	public void setAccessible(boolean isAccessible) { this.isAccessible = isAccessible; }
	
	/** Clear the piece from the cell */
	public void clearCell() { piece = null; isAccessible = true; }

	/** Constructor */
	public Cell(int x, int y){
		this.xPos = x;
		this.yPos = y;
		this.isAccessible = true;
		this.piece = null;
	}
}
