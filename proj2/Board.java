/* Class Board, used to represent the gaming board. 
 * It is used by player to check current game state and hence 
 * give out next move.  
 * 
 * COMP30024 Artificial Intelligence
 * Author: Pei-Yun Sun <peiyuns> 667816
 * Author: Wenqiang Kuang <wenqiangk> 733272
 */
package aiproj.slider;
import java.util.ArrayList;
import java.util.Scanner;

/** Class Board, used to represent the gaming board. 
 * It is used by player to check current game state and hence 
 * give out next move.  */
public class Board {
	
	private int dimension;  // dimension of the board
	private ArrayList<ArrayList<Cell>> cells;  // board has (dimension * dimension) cells
	private ArrayList<Piece> hPieces;  // A list of pieces for Horizontal player
	private ArrayList<Piece> vPieces;  // A list of pieces for Vertical player
	
	/** Constructor */
	Board(String board, int dimension) {
		// init board
		hPieces = new ArrayList<>();
		vPieces = new ArrayList<>();
		cells = new ArrayList<>();
		this.dimension = dimension;
		
		readBoard(board, dimension);  // Read board configuration from String to cells and pieces
	}
	
	/** Get the dimension of board */
	public int getDimension() { return dimension; }
	
	/** Get all the pieces of the player */
	public ArrayList<Piece> getPieces(char player) {
		if(player == 'H') {
			return hPieces;
		} else {
			return vPieces;
		}
	}
	
	/** Read a board configuration from a string with given the board size
	 * Used by the constructor to instantiate a board. */
	private void readBoard(String board, int dimension){
		String[] lines = board.split("\n");  // each row/line of the board
		
		for(int i=0; i < dimension; i++) {
			Scanner scan = new Scanner(lines[i]);
			// current row
			ArrayList<Cell> row = new ArrayList<>();  
			for(int j=0; j < dimension; j++) {
				
				// current cell
				Cell cell = new Cell(i, j);  
				char token = scan.next().charAt(0);
				
				// position from bottom-left corner
				int xPos = j;
				int yPos = dimension - i - 1;
				
				// barriers (not '+')
				if(token != '+') {
					// barrier
					cell.setAccessible(false);
					
					// check if the element is HPiece
					if(token == 'H') {
						Hpiece piece = new Hpiece(xPos, yPos);
						cell.setPiece(piece);
						this.hPieces.add(piece);
					}
					
					// check if the element is VPiece
					if(token == 'V') {
						Vpiece piece = new Vpiece(xPos, yPos);
						cell.setPiece(piece);
						this.vPieces.add(piece);
					}
				}
				// add the cell to row
				row.add(cell);			
			}
			// add the row to cells
			cells.add(row);
			scan.close();
		}
	}
	
	/** Gives whether a cell is accessible given a position in pieces' view (pos from bot-lefts corner) */
	public boolean checkAccessibility(int xPos, int yPos) {
		// Convert to position in cells (from top-left corner)
		int i = dimension - yPos - 1;
		int j = xPos;
		return cells.get(i).get(j).isAccessible();
	}
	
	/** This moves the pieces in board, given the move information of a piece. */
	public void movePiece(int xPos, int yPos, Move.Direction d) {
		// Convert to position in cells  (from top-left corner)
		int i = dimension - yPos - 1;
		int j = xPos;
		
		// Take piece at i,j
		Piece p = cells.get(i).get(j).getPiece();
		
		boolean moveSuccess = false;
		
		// Move the piece
		switch(d) {   // switch dir of mov
		case UP:
			// destination within board
			if(i-1 >= 0) {
				cells.get(i-1).get(j).setPiece(p);  // Move piece
				p.setyPos(yPos + 1);  // set new pos
				moveSuccess = true;
			} else {
				// vPieces out of board
				if(vPieces.remove(p)) {  
					moveSuccess = true;
				}
			}
			break;
		case DOWN:
			// destination within board
			if(i+1 < dimension) {
				cells.get(i+1).get(j).setPiece(p);  // Move piece
				p.setyPos(yPos - 1);  // set new pos
				moveSuccess = true;
			}
			break;
		case LEFT:
			// destination within board
			if(j-1 >= 0) {  
				cells.get(i).get(j-1).setPiece(p);  // Move piece
				p.setxPos(xPos - 1);  // set new pos
				moveSuccess = true;
			}
			break;
		case RIGHT:
			// destination within board
			if(j+1 < dimension) {
				cells.get(i).get(j+1).setPiece(p);  // Move piece
				p.setxPos(xPos + 1);  // set new pos
				moveSuccess = true;
			} else {
				// hPieces out of board
				if(hPieces.remove(p)) {
					moveSuccess = true;
				}
			}
			break;
		}
		
		if(moveSuccess) {  // legal move
			cells.get(i).get(j).clearCell();  // Clear piece from current cell
		}
	}
	
	/** Returns all possible moves of the player */
	public ArrayList<Move> getAllMoves(char player) {
		
		ArrayList<Move> moves = new ArrayList<>();
		ArrayList<Piece> pieces = getPieces(player);
		
		// update moving status of each piece
		for(Piece p: pieces) 
			p.updateDir(this);  
			
		// search better move 1st	
		for(Piece p: pieces) {
			// player H & Right
			if(player == 'H') {	
				if(p.canMoveRight())   
					moves.add(new Move(p.getxPos(), p.getyPos(), Move.Direction.RIGHT));
			// player V & Up
			} else {  
				if(p.canMoveUp())   
					moves.add(new Move(p.getxPos(), p.getyPos(), Move.Direction.UP));
			}
		}
		
		for(Piece p: pieces) {
			int xPos = p.getxPos();
			int yPos = p.getyPos();

			// player H
			if(player == 'H') {
				// Up
				if(p.canMoveUp()) {  
					Move move = new Move(xPos, yPos, Move.Direction.UP);
					moves.add(move);
				}
				// Down
				if(((Hpiece)p).canMoveDown()) {   // only player H can move down
					Move move = new Move(xPos, yPos, Move.Direction.DOWN);
					moves.add(move);
				}
			// player V
			} else {  
				// right
				if(p.canMoveRight()) {  
					Move move = new Move(xPos, yPos, Move.Direction.RIGHT);
					moves.add(move);
				}
				// Left
				if(((Vpiece)p).canMoveLeft()) {   // only player V can move left
					Move move = new Move(xPos, yPos, Move.Direction.LEFT);
					moves.add(move);
				}
			}
		}
		return moves;
	}
	
	/** Convert the board to a string, following the rules of given board sample. */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(2 * dimension * dimension);
		
		for(ArrayList<Cell> row: cells) {
			for(Cell c: row) {
				// first element
				if(row.indexOf(c) == 0) {  
					s.append(getChar(c));
				} 
				// the rest
				else {  
					s.append(' ');
					s.append(getChar(c));
				}
			}
			s.append('\n');
		}
		return s.toString();
	}
	
	/** Return token for a cell, given cell position
	 * Called by ConvertToString() to output a string board representation. */
	public char getChar(Cell c) {
		if(c.isAccessible()) {
			return '+';
		} else if(c.getPiece() != null) {
			if(c.getPiece() instanceof Hpiece) {
				return 'H';
			} else {
				return 'V';
			}
		} else {
			return 'B';
		}
	}
	
	/** Return dist from the nearst Vpiece which is blocking current Hpiece
	 *  if the Hpiece is blocked by V, otherwise return -1 (not blocking by V) */
	public int SearchH_BlockedByV(Piece piece) {  // check if that H is blocked by V
		// Convert to pos in cells
		int i = dimension - piece.yPos - 1;
		int j = piece.xPos;
		
		for(int k = j-1; k >= 0; k--) {  // right of the piece
			Cell c = cells.get(i).get(k);
			char ch = getChar(c);
			if(ch == 'B' || ch == 'V') {  // if the nearest barrier is B or V
				return -1;
			}
			if(ch == 'H') {  // if blocked 
				return j-1-k;
			}
		}
		return -1;
	}
	
	/** Return dist from the nearst Hpiece which is blocking current Vpiece
	 *  if the Vpiece is blocked by H, otherwise return -1 (not blocking by H) */
	public int SearchV_BlockedByH(Piece piece) {  // check if that V is blocked by H
		// Convert to pos in cells
		int i = dimension - piece.yPos - 1;
		int j = piece.xPos;
		
		for(int k = i+1; k < dimension; k++) {   // up of the piece
			Cell c = cells.get(k).get(j);
			char ch = getChar(c);
			if(ch == 'B' || ch == 'H') {  // if the nearest barrier is B or H
				return -1;
			}
			if(ch == 'V') {  // if blocked by H
				return k-i-1;
			}
		}
		return -1;
	}
	
	/** Return whether the Hpiece is horizontally blocked (barrier just on its right) */
	public boolean horizontalBlocked(Piece piece) {
		// Convert to pos in cells
		int i = dimension - piece.yPos - 1;
		int j = piece.xPos;
				
		// if not at the rightmost column and blocked by any barrier (not '+')
		if(j+1<dimension && !cells.get(i).get(j+1).isAccessible()) {
			return true;
		}
		
		// otherwise not blocked
		return false;
	}
	
	/** This return whether the Vpiece is vertically blocked (barrier just upon it) */
	public boolean verticalBlocked(Piece piece) {
		// Convert to pos in cells
		int i = dimension - piece.yPos - 1;
		int j = piece.xPos;
						
		// if not at top and blocked by any barrier (not '+')
		if(i-1>=0 && !cells.get(i-1).get(j).isAccessible()) {
			return true;
		}
		
		// otherwise not blocked
		return false;
	}
	
	/** This returns a cell in board given the position. */
	public Cell getCell(int xPos, int yPos) {
		// Convert to position in cells
		int i = dimension - yPos - 1;
		int j = xPos;
		
		return cells.get(i).get(j);
	}
}
