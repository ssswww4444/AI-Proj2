/* Class Hpiece, inherited from the Class Piece. This class can be
 * instantiated, and one more direction move is added. 
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;

/** Class Hpiece, inherited from the Class Piece. This class can be
 * instantiated, and one more direction move is added. */
public class Hpiece extends Piece{
	
	private boolean downMove;  // whether the piece can move down
	
	/** Constructor */
	public Hpiece(int x, int y) {
		super(x, y);
		this.downMove = true;  // init down move
	}

	/** Update accessbilities of each direction of the piece according to the board */
	@Override
	public void updateDir(Board board) {
		// init all dir
		rightMove = true;
		upMove = true;
		downMove = true;
		
		// position from bottom-left corner
		int right = xPos + 1;
		int up = yPos + 1;
		int down = yPos - 1;
		
		// right, not out of board or not accessible
		if(right != board.getDimension() && !board.checkAccessibility(right, yPos)) {  
			rightMove = false;
		}
		
		// up, out of range or not accessible
		if(up >= board.getDimension() || !board.checkAccessibility(xPos, up)) {
			upMove = false;
		}
		
		// down, out of range or not accessible
		if(down < 0 || !board.checkAccessibility(xPos, down)) {
			downMove = false;
		}
	}
	
	/** This returns whether or not the piece could go down. */
	public boolean canMoveDown() {
		return downMove;
	}
}
