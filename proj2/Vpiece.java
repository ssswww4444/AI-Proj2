/* Class Vpiece, inherited from the Class Piece. This class can be
 * instantiated, and one more direction move is added. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

/** Class Vpiece, inherited from the Class Piece. This class can be
 * instantiated, and one more direction move is added. */
public class Vpiece extends Piece{
	
	private boolean leftMove;  // whether the piece can go left
	
	/** Constructor */
	public Vpiece(int x, int y) {
		super(x, y);
		this.leftMove = true;  // init left move
	}

	/** Update accessbilities of each direction of the piece according to the board */
	@Override
	public void updateDir(Board board) {
		// init all dir
		rightMove = true;
		upMove = true;
		leftMove = true;
		
		// position from bottom-left corner
		int left = xPos - 1;
		int up = yPos + 1;
		int right = xPos + 1;
		
		// left, out of range or not accessible
		if(left < 0 || !board.checkAccessibility(left, yPos)) {
			leftMove = false;
		}
		
		// right, out of range or not accessible
		if(right >= board.getDimension() || !board.checkAccessibility(right, yPos)) {
			rightMove = false;
		}
		
		// up, not out of board and not accessible
		if(up != board.getDimension() && !board.checkAccessibility(xPos, up)) {
			upMove = false;
		}
	}
	
	/** Returns whether the piece can go left */
	public boolean canMoveLeft() {
		return leftMove;
	}
}
