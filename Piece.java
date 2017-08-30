/* Class Piece, used to represent piece. This is an abstract class. 
 * All pieces can move up and right. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

/** Class Piece, used to represent piece. This is an abstract class. 
 * All pieces can move up and right. */
public abstract class Piece {
	protected int xPos;  // col 
	protected int yPos;  // row
	
	protected boolean upMove;  // whether the piece can move up
	protected boolean rightMove;  // whether the piece can go right
	
	public Piece(int x, int y){
		this.xPos = x;
		this.yPos = y;
		this.upMove = true;
		this.rightMove = true;
	}
	
	/** getters and setters */
	public int getxPos() { return xPos; }
	public int getyPos() { return yPos; }
	public void setxPos(int xPos) { this.xPos = xPos; }
	public void setyPos(int yPos) { this.yPos = yPos; }
	
	/** Returns whether the piece can move up */
	public boolean canMoveUp() { return upMove; }
	
	/** Returns whether the piece can go right */
	public boolean canMoveRight() { return rightMove; }
	
	/** Update accessbilities of each direction of the piece according to the board */
	public abstract void updateDir(Board board);
}
